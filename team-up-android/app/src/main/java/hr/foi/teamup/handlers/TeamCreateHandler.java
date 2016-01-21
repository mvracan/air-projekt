package hr.foi.teamup.handlers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.TeamActivity;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.webservice.ServiceResponse;

/**
 *
 * Created by paz on 26.11.15..
 */
public class TeamCreateHandler extends ResponseHandler {

    public TeamCreateHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Team team = (Team) getArgs()[0];
        Logger.log("Deserialized arguments: " + team.toString(), getClass().getName(), Log.DEBUG);

        if(response.getHttpCode() == 200) {
            Logger.log("Successfully created team", getClass().getName(), Log.DEBUG);
            // login

            SessionManager manager= SessionManager.getInstance(getContext());
            manager.createSession(team, SessionManager.TEAM_INFO_KEY);



            return true;
        } else {
            Logger.log("Creating team failed " + response.getHttpCode(),
                    getClass().getName(), Log.WARN);
            // show fail
            Toast.makeText(getContext(),
                    "Team creation failed, please try again (" + response.getHttpCode() + ")",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}