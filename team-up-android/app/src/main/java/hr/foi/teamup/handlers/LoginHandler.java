package hr.foi.teamup.handlers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.TeamActivity;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceResponse;

/**
 * handles login calls
 * Created by Tomislav Turek on 07.11.15..
 */
public class LoginHandler extends ResponseHandler {

    public LoginHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

        if(response.getHttpCode() == 200) {

            // convert json to person object
            Person person = new Gson().fromJson(response.getJsonResponse(), Person.class);
            // save person to session
            SessionManager manager = SessionManager.getInstance(this.context);
            if(manager.createSession(person, SessionManager.PERSON_INFO_KEY)) {

                Person sessionPerson = manager.retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);
                Logger.log("Valid user, created session: " + sessionPerson.toString()
                                + ", proceeding to group activity", getClass().getName(), Log.DEBUG);
                // start main activity
                Intent intent = new Intent(this.context, TeamActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.context.startActivity(intent);
                return true;

            } else {
                Toast.makeText(this.context,
                        "Internal application error, please try again", Toast.LENGTH_LONG).show();
                return false;
            }

        } else  {
            // http code different from 200 OK
            Logger.log("LoginHandler -- invalid credentials sent", Log.WARN);
            Toast.makeText(this.context, "Invalid credentials", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
