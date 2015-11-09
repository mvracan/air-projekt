package hr.foi.teamup.handlers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.TeamActivity;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ResponseHandler;
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
        Log.i("hr.foi.teamup.debug", "LoginHandler -- Got response: " + response.toString());
        if(response.getHttpCode() == 200) {

            Person person = new Gson().fromJson(response.getJsonResponse(), Person.class);
            SessionManager manager = SessionManager.getInstance(this.getContext());
            if(manager.createSession(person, "person")) {

                Person sessionPerson = manager.retrieveSession("person", Person.class);
                Log.i("hr.foi.teamup.debug",
                        "LoginHandler -- valid user, created session: " + sessionPerson.toString()
                                + ", proceeding to group activity");
                Intent intent = new Intent(this.getContext(), TeamActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.getContext().startActivity(intent);
                return true;

            } else {
                Toast.makeText(this.getContext(),
                        "Internal application error, please try again", Toast.LENGTH_LONG).show();
                return false;
            }

        } else  {

            Log.i("hr.foi.teamup.debug", "LoginHandler -- invalid credentials sent");
            Toast.makeText(this.getContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
            return false;

        }
    }
}
