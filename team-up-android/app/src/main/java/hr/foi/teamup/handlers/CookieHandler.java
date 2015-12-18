package hr.foi.teamup.handlers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.StompActivity;
import hr.foi.teamup.TeamActivity;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceResponse;

/**
 * Created by paz on 12.12.15..
 */
public class CookieHandler extends ResponseHandler {

    public CookieHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

        if (response.getHttpCode() == 202) {

            Log.i("COOKIE ", response.getCookie());

            SessionManager manager = SessionManager.getInstance(this.context);
            manager.createSession(response.getCookie(),SessionManager.COOKIE);

            Intent intent = new Intent(this.context, StompActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(intent);

            return true;

        } else {
            // http code different from 200 OK
            Logger.log("LoginHandler -- invalid credentials sent", Log.WARN);
            Toast.makeText(this.context, "Invalid credentials", Toast.LENGTH_LONG).show();
            return false;
        }

    }
}

