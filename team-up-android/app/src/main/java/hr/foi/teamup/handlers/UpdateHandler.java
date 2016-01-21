package hr.foi.teamup.handlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceResponse;

/**
 * handles profile updates
 * Created by Tomislav Turek on 07.11.15..
 */
public class UpdateHandler extends ResponseHandler {

    public UpdateHandler(Activity activity, Serializable... args) {
        super(activity, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Person user = (Person) getArgs()[0];
        Logger.log("Deserialized arguments: " + user.toString(), getClass().getName(), Log.DEBUG);

        if(response.getHttpCode() == 200) {
            Logger.log("Successfully updated user", getClass().getName());

            // update session
            SessionManager manager= SessionManager.getInstance(getActivity());
            manager.destroySession(SessionManager.PERSON_INFO_KEY);
            manager.createSession(user, SessionManager.PERSON_INFO_KEY);
            Toast.makeText(getActivity(),
                    "Update successful", Toast.LENGTH_LONG).show();

            getActivity().finish();

            return true;
        } else {
            Logger.log("Update failed, server returned code "
                    + response.getHttpCode(), getClass().getName(), Log.WARN);
            // show fail
            Toast.makeText(getActivity(),
                    "Update failed, please try again ("+response.getHttpCode()+")",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
