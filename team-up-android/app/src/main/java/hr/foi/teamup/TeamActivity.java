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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.air.teamup.prompts.AlertPrompt;
import hr.foi.air.teamup.prompts.InputPrompt;
import hr.foi.teamup.fragments.TeamFragment;
import hr.foi.teamup.fragments.TeamHistoryFragment;
import hr.foi.teamup.handlers.ActiveTeamHandler;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;

public class TeamActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

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

        // set current team for the first time
        if(savedInstanceState == null) {
            Logger.log("First time");
            exchangeFragments(new TeamFragment(), "currentteam");
        }

        getActiveTeam();


    }


    protected void getActiveTeam(){

        SessionManager manager=SessionManager.getInstance(getApplicationContext());

        ActiveTeamHandler activeTeamHandler = new ActiveTeamHandler(TeamActivity.this);
        Person user = manager.retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);

        ServiceParams params = new ServiceParams(
                getString(hr.foi.teamup.webservice.R.string.team_history_path)+user.getIdPerson(),
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
                for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
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
        if(item.getItemId() == R.id.open_map) {
            // FragmentTransaction
            // FragmentManager
            // fragmentManager.replace(bla, bla);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void exchangeFragments(Fragment fragment, String name) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_frame, fragment);
        getFragmentManager().popBackStack();
        transaction.addToBackStack(name);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId()==R.id.profile){
            Logger.log("Profile clicked");
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
