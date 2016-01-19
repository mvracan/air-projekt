package hr.foi.teamup;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import hr.foi.teamup.fragments.TeamFragment;
import hr.foi.teamup.fragments.TeamHistoryFragment;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.model.TeamMessage;
import hr.foi.teamup.stomp.ListenerSubscription;
import hr.foi.teamup.stomp.TeamConnection;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.SimpleResponseHandler;

public class TeamActivity extends NfcForegroundDispatcher implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    private Person client;
    HashMap<String,ListenerSubscription> subscriptionChannels;
    TeamConnection socket;
    String cookie;
    String teamId;
    String USER_CHANNEL_PATH = "/user/queue/messages";
    String GROUP_PATH = "/topic/team/";
    TeamFragment teamFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        // navigation menu
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.drawer_open,  R.string.drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button logOut = (Button) findViewById(R.id.log_out_button);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.log("Logging out user...");
                signOut();
            }
        });

        client = SessionManager.getInstance(getApplicationContext())
                .retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);

        // set current team for the first time
        if(savedInstanceState == null) {
            Logger.log("First time");
            teamFragment = new TeamFragment();
            exchangeFragments(teamFragment, "currentteam");
        }

        getActiveTeam();

        // starts foreground dispatcher
        try {
            startNfcAdapter();
            setNfcDispatchCallback(callback);
        } catch (NfcNotAvailableException e) {
            Toast.makeText(this, "NFC is not turned on", Toast.LENGTH_LONG).show();
        } catch (NfcNotEnabledException e) {
            Toast.makeText(this, "NFC is not supported", Toast.LENGTH_LONG).show();
        }

    }

    // gets team id and subscribes him
    NfcBeamMessageCallback callback=new NfcBeamMessageCallback() {
        @Override
        public void onMessageReceived(String message) {
            Logger.log(message);
            teamId = message;
            // become a member
            new ServiceAsyncTask(memberHandler).execute(
                    new ServiceParams("/team/" + teamId + "/person/" + client.getIdPerson(),
                            ServiceCaller.HTTP_POST, null));
        }
    };

    public void getCookie(){

        new ServiceAsyncTask(handler).execute(new ServiceParams(
                "/login",
                ServiceCaller.HTTP_POST, "application/x-www-form-urlencoded", null, "username=" + client.getCredentials().getUsername() +
                "&password=" + client.getCredentials().getPassword()));

    }

    // cookie authentication
    SimpleResponseHandler memberHandler = new SimpleResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

            if (response.getHttpCode() == 200) {
                    getCookie();
                return true;

            } else {
                // http code different from 200 OK
                Logger.log("LoginHandler -- invalid credentials sent", Log.WARN);
                Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    };

    SimpleResponseHandler activeTeamHandler = new SimpleResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

            if(response.getHttpCode() == 200) {

                // convert json to person object
                Team team = new Gson().fromJson(response.getJsonResponse(), Team.class);
                // save team to session
                SessionManager manager = SessionManager.getInstance(getApplicationContext());
                if(manager.createSession(team, SessionManager.TEAM_INFO_KEY)) {

                    Team sessionTeam = manager.retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);
                    Logger.log("Valid user and team, created session: " + sessionTeam.toString()
                            + ", in teamactivity", getClass().getName(), Log.DEBUG);
                    teamId = String.valueOf(sessionTeam.getIdTeam());

                    getCookie();
                    
                    return true;
                }else{

                    Logger.log("Error in creating team session ", getClass().getName(), Log.DEBUG);
                    return false;
                }


            }  else {

                Toast.makeText(getApplicationContext(), "Currently without team! ", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    } ;

    // after authetication, joins to group
    SimpleResponseHandler handler = new SimpleResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

            if (response.getHttpCode() == 202) {

                Log.i("COOKIE ", response.getCookie());

                SessionManager manager = SessionManager.getInstance(getApplicationContext());
                manager.createSession(response.getCookie(), SessionManager.COOKIE_KEY);
                cookie= response.getCookie();

                if(cookie!=null) {
                    joinGroup();
                    //ping();
                }else {
                    Log.i("no cookie", "nocokkie");
                }

                return true;

            } else {
                // http code different from 200 OK
                Logger.log("LoginHandler -- invalid credentials sent", Log.WARN);
                Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    };

    // pings for team members every second
    public void ping(){

        TeamMessage message = new TeamMessage();

        Person test= SessionManager.getInstance(this).retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);
        message.setMessage(test);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        socket.send("/app/team/"+teamId,message);

    }

    // stomp message callback
    ListenerSubscription subscription=new ListenerSubscription() {
        @Override
        public void onMessage(Map<String, String> headers, String body) {
            Logger.log(body);
            Type listType = new TypeToken<ArrayList<Person>>() {}.getType();

            final ArrayList<Person> persons = new Gson().fromJson(body, listType);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    teamFragment.updateList(persons);
                }
            });
        }
    };

    /**
     * joins to group through subscription
     */
    private void joinGroup(){

        subscriptionChannels = new HashMap<>();
        cookie = SessionManager.getInstance(getApplicationContext()).retrieveSession(SessionManager.COOKIE_KEY, String.class);

        if(cookie != null) {
            subscriptionChannels.put(USER_CHANNEL_PATH, subscription);
            subscriptionChannels.put(GROUP_PATH + teamId, subscription);

            socket = new TeamConnection(subscriptionChannels, cookie);
            socket.start();
        }
    }

    /**
     * get current team if exists
     */
    protected void getActiveTeam(){
        SessionManager manager=SessionManager.getInstance(getApplicationContext());


        Person user = manager.retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);

        ServiceParams params = new ServiceParams(
                getString(hr.foi.teamup.webservice.R.string.team_person_path)+user.getIdPerson(),
                ServiceCaller.HTTP_POST, null
                );

        new ServiceAsyncTask(activeTeamHandler).execute(params);


    }

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

    @Override
    public void onBackPressed() {
        mDrawer.closeDrawers();
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            signOut();
        }
    }

    // ask for sign out
    private void signOut() {
        DialogInterface.OnClickListener signOutListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SessionManager.getInstance(getApplicationContext()).destroyAll();
                dialog.dismiss();
                socket.finish();
                for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                    getFragmentManager().popBackStack();
                }
                socket.stopThread();
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
        if(item.getItemId() == R.id.open_map) {
            // TODO open map
            // FragmentTransaction
            // FragmentManager
            // fragmentManager.replace(bla, bla);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * exchanges fragments
     * @param fragment fragment that goes in foreground
     * @param name back stack name
     */
    private void exchangeFragments(Fragment fragment, String name) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_frame, fragment);
        getFragmentManager().popBackStack();
        transaction.addToBackStack(name);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // handling navigation items
        if (menuItem.getItemId()==R.id.profile){
            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
        } else if (menuItem.getItemId()==R.id.code){
            InputPrompt prompt = new InputPrompt(this);
            prompt.prepare(R.string.join_group, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Logger.log("Stisnul je da");
                }
            },R.string.join, null, R.string.cancel);
            prompt.showPrompt();
        } else if (menuItem.getItemId()==R.id.nfc){
            startActivity(new Intent(this, BeamActivity.class));
        } else if (menuItem.getItemId()==R.id.history){
            exchangeFragments(new TeamHistoryFragment(), "teamhistory");
        } else if (menuItem.getItemId()==R.id.new_group){
            startActivity(new Intent(getApplicationContext(), CreateTeamActivity.class));
        }

        mDrawer.closeDrawers();
        return true;
    }
}
