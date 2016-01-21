package hr.foi.teamup.handlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.webservice.ServiceResponse;

/**
 *
 * Created by paz on 26.11.15..
 */
public class TeamCreateHandler extends ResponseHandler {

    public TeamCreateHandler(Activity activity, Serializable... args) {
        super(activity, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Team team = (Team) getArgs()[0];
        Logger.log("Deserialized arguments: " + team.toString(), getClass().getName(), Log.DEBUG);

        if(response.getHttpCode() == 200) {
            Logger.log("Successfully created team", getClass().getName(), Log.DEBUG);
            // login

            SessionManager manager= SessionManager.getInstance(getActivity());
            manager.createSession(team, SessionManager.TEAM_INFO_KEY);

            getActivity().finish();

            return true;
        } else {
            Logger.log("Creating team failed " + response.getHttpCode(),
                    getClass().getName(), Log.WARN);
            // show fail
            Toast.makeText(getActivity(),
                    "Team creation failed, please try again (" + response.getHttpCode() + ")",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}