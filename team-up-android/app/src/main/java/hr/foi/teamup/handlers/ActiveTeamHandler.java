package hr.foi.teamup.handlers;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.webservice.ServiceResponse;

/**
 * fetch active team from response and get active auth cookie
 * Created by maja on 21.01.16..
 */
public class ActiveTeamHandler extends ExtendedResponseHandler {

    public ActiveTeamHandler(Activity activity, Serializable... args) {
        super(activity, args);
    }

    public ActiveTeamHandler(Activity activity, CodeCaller caller, Serializable... args) {
        super(activity, caller, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

        if (response.getHttpCode() == 200) {

            // convert json to person object
            Team team = new Gson().fromJson(response.getJsonResponse(), Team.class);
            // save team to session
            SessionManager manager = SessionManager.getInstance(getActivity());
            if (manager.createSession(team, SessionManager.TEAM_INFO_KEY)) {

                Team sessionTeam = manager.retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);
                Logger.log("Valid user and team, created session: " + sessionTeam.toString()
                        + ", in teamactivity", getClass().getName(), Log.DEBUG);

                // call parent implementation
                getCaller().call(true);

                return true;
            } else {
                Logger.log("Error in creating team session ", getClass().getName(), Log.DEBUG);
                // call parent implementation

            }
        } else {
            Toast.makeText(getActivity(), "Currently without team! ", Toast.LENGTH_LONG).show();

        }

        getCaller().call(false);
        return false;

    }

}
