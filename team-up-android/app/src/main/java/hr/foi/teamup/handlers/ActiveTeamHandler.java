package hr.foi.teamup.handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.webservice.ServiceResponse;

/**
 * gets active group and saves to session (if exists)
 * Created by paz on 26.12.15..
 */
public class ActiveTeamHandler extends ResponseHandler {

    public ActiveTeamHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

        if(response.getHttpCode() == 200) {

            // convert json to person object
            Team team = new Gson().fromJson(response.getJsonResponse(), Team.class);
            // save team to session
            SessionManager manager = SessionManager.getInstance(getContext());
            if(manager.createSession(team, SessionManager.TEAM_INFO_KEY)) {

                Team sessionTeam = manager.retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);
                Logger.log("Valid user and team, created session: " + sessionTeam.toString()
                        + ", in teamactivity", getClass().getName(), Log.DEBUG);

                return true;
            }else{

                Logger.log("Error in creating team session ", getClass().getName(), Log.DEBUG);
                return false;

            }


        }  else {

            Toast.makeText(getContext(),
                    "Currently without team! ", Toast.LENGTH_LONG).show();
            return false;

        }
    }
}
