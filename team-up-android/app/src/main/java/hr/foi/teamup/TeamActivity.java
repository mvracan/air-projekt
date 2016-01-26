package hr.foi.teamup;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.air.teamup.nfcaccess.NfcBeamMessageCallback;
import hr.foi.air.teamup.nfcaccess.NfcForegroundDispatcher;
import hr.foi.air.teamup.nfcaccess.NfcNotAvailableException;
import hr.foi.air.teamup.nfcaccess.NfcNotEnabledException;
import hr.foi.air.teamup.prompts.AlertPrompt;
import hr.foi.air.teamup.prompts.InputPrompt;
import hr.foi.teamup.fragments.LocationFragment;
import hr.foi.teamup.fragments.TeamFragment;
import hr.foi.teamup.fragments.TeamHistoryFragment;
import hr.foi.teamup.handlers.ActiveTeamHandler;
import hr.foi.teamup.handlers.CodeCaller;
import hr.foi.teamup.handlers.JoinGroupHandler;
import hr.foi.teamup.handlers.MemberCookieHandler;
import hr.foi.teamup.maps.LocationCallback;
import hr.foi.teamup.maps.MarkerClickHandler;
import hr.foi.teamup.maps.MapConfiguration;
import hr.foi.teamup.model.Location;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.stomp.ListenerSubscription;
import hr.foi.teamup.stomp.TeamConnection;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.SimpleResponseHandler;

public class TeamActivity extends NfcForegroundDispatcher implements NavigationView.OnNavigationItemSelectedListener,
        LocationCallback {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    private Person client;
    private double teamRadius;
    private TeamConnection socket;
    private String teamId;
    private static final String USER_CHANNEL_PATH = "/user/queue/messages";
    private static final String GROUP_PATH = "/topic/team/";
    private TeamFragment teamFragment;
    private LocationFragment locationFragment;
    private NavigationView navigationView;
    private Person panicPerson;
    private MapConfiguration mapConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        // instantiate location listeners
        mapConfiguration = new MapConfiguration(this, this);
        mapConfiguration.buildGoogleApiClient();
        mapConfiguration.createLocationRequest();

        // navigation menu
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // panic button
        FloatingActionButton panicButton = (FloatingActionButton) findViewById(R.id.panic_button);
        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.send("/app/team/" + teamId + "/panic/" + client.getIdPerson(), null);
            }
        });

        // log out button
        Button logOut = (Button) findViewById(R.id.log_out_button);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.log("Logging out user...");
                signOut();
            }
        });

        // set main fragments
        teamFragment = new TeamFragment();
        locationFragment = new LocationFragment();
        locationFragment.setCallback(callbackMarkerClick);

        exchangeFragments(teamFragment);

        // get current user
        client = SessionManager.getInstance(getApplicationContext())
                .retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);

        // get current team (does not need to exist)
        ServiceParams params = new ServiceParams(
                getString(hr.foi.teamup.webservice.R.string.team_person_path) + client.getIdPerson(),
                ServiceCaller.HTTP_POST, null
        );
        new ServiceAsyncTask(new ActiveTeamHandler(this, activeTeamCaller)).execute(params);

        // starts nfc foreground dispatcher
        try {
            startNfcAdapter();
            setNfcDispatchCallback(callback);
        } catch (NfcNotAvailableException e) {
            Toast.makeText(this, "NFC is not turned on", Toast.LENGTH_LONG).show();
        } catch (NfcNotEnabledException e) {
            Toast.makeText(this, "NFC is not supported", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (socket != null) {
            socket.send("/app/updateLocation", location);
        }
    }

    /**
     * gets team id and subscribes him to team channel
     */
    NfcBeamMessageCallback callback = new NfcBeamMessageCallback() {
        @Override
        public void onMessageReceived(String message) {
            Logger.log(message);
            // become a member
            new ServiceAsyncTask(new MemberCookieHandler(TeamActivity.this, memberCaller)).execute(
                    new ServiceParams("/team/" + message + "/person/" + client.getIdPerson(),
                            ServiceCaller.HTTP_POST, null));
        }
    };

    /**
     * makes call to service to get the auth cookie
     */
    public void getCookie() {
        new ServiceAsyncTask(new JoinGroupHandler(this, joinGroupCaller)).execute(new ServiceParams(
                "/login",
                ServiceCaller.HTTP_POST, ServiceCaller.X_WWW_FORM_URLENCODED, null,
                "username=" + client.getCredentials().getUsername() +
                        "&password=" + client.getCredentials().getPassword()));
    }

    /**
     * gets user cookie after auth
     */
    CodeCaller memberCaller = new CodeCaller() {
        @Override
        public void call(boolean positive) {
            getCookie();
        }
    };

    /**
     * remote code that gets sent to active team handler to handle
     * authentication cookies and navigation menu items
     */
    CodeCaller activeTeamCaller = new CodeCaller() {
        @Override
        public void call(boolean positive) {
            if(positive) {
                Team sessionTeam = SessionManager.getInstance(getApplicationContext())
                        .retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);
                teamId = String.valueOf(sessionTeam.getIdTeam());
                teamRadius = sessionTeam.getRadius();

                setNavigationMenuItems(R.menu.team_exist_menu);
                getCookie();
            } else {
                setNavigationMenuItems(R.menu.menu);
                teamFragment.setViewLayout(R.layout.layout_empty_data);
            }
        }
    };

    /**
     * joins to group through subscription
     */
    CodeCaller joinGroupCaller = new CodeCaller() {
        @Override
        public void call(boolean positive) {
            HashMap<String, ListenerSubscription> subscriptionChannels = new HashMap<>();
            String cookie = SessionManager.getInstance(getApplicationContext()).retrieveSession(SessionManager.COOKIE_KEY, String.class);

            if (cookie != null) {
                subscriptionChannels.put(USER_CHANNEL_PATH, subscriptionUserLost);
                subscriptionChannels.put(GROUP_PATH + teamId, subscription);

                socket = new TeamConnection(subscriptionChannels, cookie);
                socket.start();
            }
        }
    };

    /**
     * stomp message callback
     */
    ListenerSubscription subscription = new ListenerSubscription() {
        @Override
        public void onMessage(Map<String, String> headers, String body) {
            Logger.log(body);
            Type listType = new TypeToken<ArrayList<Person>>() {
            }.getType();

            final ArrayList<Person> persons = new Gson().fromJson(body, listType);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    teamFragment.updateList(persons);
                    locationFragment.setUserLocations(persons, teamRadius);
                }
            });
        }
    };

    /**
     * stomp panic message callback
     */
    ListenerSubscription subscriptionUserLost = new ListenerSubscription() {
        @Override
        public void onMessage(Map<String, String> headers, String body) {

            Logger.log(body);
            panicPerson = new Gson().fromJson(body, Person.class);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // make him red if fragment is visible

                    if (locationFragment.isVisible()) {
                        locationFragment.paintPerson(panicPerson, BitmapDescriptorFactory.HUE_RED);
                    }

                    // vibrate
                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(2000);

                    // issue notification
                    NotificationCompat.Builder mBuilder = setNotification(setNotificationMessage(client, panicPerson));

                    int mNotificationId = 1;
                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    // set pending intent launch
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), -1, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pendingIntent);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());

                }
            });
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setNavigationMenuItems(int menuId) {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(menuId);
    }

    @Override
    public void onBackPressed() {
        mDrawer.closeDrawers();
        signOut();
    }

    /**
     * ask for sign out
     */
    private void signOut() {
        DialogInterface.OnClickListener signOutListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SessionManager.getInstance(getApplicationContext()).destroyAll();
                dialog.dismiss();
                if (socket != null) socket.finish();
                for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                    getFragmentManager().popBackStack();
                }
                TeamActivity.super.onBackPressed();
            }
        };
        AlertPrompt signOutPrompt = new AlertPrompt(this);
        signOutPrompt.prepare(R.string.log_out_question, signOutListener,
                R.string.log_out, null, R.string.cancel);
        signOutPrompt.showPrompt();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_team_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.open_map) {
            if(teamId != null && TextUtils.isEmpty(teamId)) {
                exchangeFragments(locationFragment);
            } else {
                Toast.makeText(this,
                        getString(R.string.please_join), Toast.LENGTH_LONG).show();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * exchanges fragments
     * @param fragment fragment that goes in foreground
     */
    private void exchangeFragments(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // handling navigation items
        if (menuItem.getItemId() == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
        } else if (menuItem.getItemId() == R.id.code) {
            final InputPrompt prompt = new InputPrompt(this);
            prompt.prepare(R.string.join_group, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new ServiceAsyncTask(new SimpleResponseHandler() {
                        @Override
                        public boolean handleResponse(ServiceResponse response) {
                            if(response.getHttpCode() == 200) callback.onMessageReceived(response.getJsonResponse());
                            return true;
                        }
                    }).execute(new ServiceParams(getString(R.string.team_path)
                            + client.getIdPerson() + "/code/" + prompt.getInput(), ServiceCaller.HTTP_GET, null));
                }
            }, R.string.join, null, R.string.cancel).showPrompt();
        } else if (menuItem.getItemId() == R.id.nfc) {
            startActivity(new Intent(this, BeamActivity.class));
        } else if (menuItem.getItemId() == R.id.history) {
            exchangeFragments(new TeamHistoryFragment());
        } else if (menuItem.getItemId() == R.id.new_group) {
            startActivity(new Intent(getApplicationContext(), CreateTeamActivity.class));
        } else if (menuItem.getItemId() == R.id.home) {
            exchangeFragments(teamFragment);
        } else if (menuItem.getItemId() == R.id.leave_group) {
            socket.finish();
            new ServiceAsyncTask(null).execute(new ServiceParams(getString(hr.foi.teamup.webservice.R.string.team_path) + teamId + "/leave/" + client.getIdPerson(),
                    ServiceCaller.HTTP_POST, null));
            teamFragment.setViewLayout(R.layout.layout_empty_data);
            setNavigationMenuItems(R.menu.menu);

        }

        mDrawer.closeDrawers();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapConfiguration.getGoogleApiClient() != null && mapConfiguration.getGoogleApiClient().isConnected())
            mapConfiguration.stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapConfiguration.getGoogleApiClient() != null && mapConfiguration.getGoogleApiClient().isConnected()) {
            mapConfiguration.startLocationUpdates();
        }
    }

    /**
     * sets the notification message (panic or client)
     * @param client user using app
     * @param panic user that panics
     * @return notification message
     */
    protected String setNotificationMessage(Person client, Person panic){

        if(client.getIdPerson() == panic.getIdPerson())
            return "Please come back to group area!";

        return "User " + panic.getName() + " is panicking, go find this person!";

    }

    /**
     * handles red marker click
     */
    MarkerClickHandler callbackMarkerClick = new MarkerClickHandler(){

        @Override
        public void onMarkerClick(String... args){
            String username = args[0];
            if(socket != null)
                socket.send("/app/team/"+teamId+"/calmUser",username);

        }
    };

    /**
     * creates notification
     * @param notification notification text
     * @return notification builder
     */
    protected NotificationCompat.Builder setNotification(String notification){

        return new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(notification);
    }


}
