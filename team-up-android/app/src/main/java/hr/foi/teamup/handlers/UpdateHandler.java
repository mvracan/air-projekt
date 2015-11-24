package hr.foi.teamup.handlers;

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

    public UpdateHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Person user = (Person) this.args[0];
        Logger.log("UpdateHandler -- deserialized arguments: " + user.toString(), Log.DEBUG);

        if(response.getHttpCode() == 200) {
            Logger.log("UpdateHandler -- successfully updated user");

            // update session
            SessionManager manager= SessionManager.getInstance(this.context);
            manager.destroySession(SessionManager.PERSON_INFO_KEY);
            manager.createSession(user, SessionManager.PERSON_INFO_KEY);
            Toast.makeText(this.context,
                    "Update successful", Toast.LENGTH_LONG).show();
            // finish()
            return true;
        } else {
            Logger.log("UpdateHandler -- update failed, server returned code "
                    + response.getHttpCode(), Log.WARN);
            // show fail
            Toast.makeText(this.context,
                    "Update failed, please try again ("+response.getHttpCode()+")",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
