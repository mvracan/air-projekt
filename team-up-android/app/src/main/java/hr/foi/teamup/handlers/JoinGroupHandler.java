package hr.foi.teamup.handlers;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.webservice.ServiceResponse;

/**
 * after authentication, joins to group
 * Created by maja on 21.01.16..
 */
public class JoinGroupHandler extends ExtendedResponseHandler {

    public JoinGroupHandler(Activity activity, Serializable... args) {
        super(activity, args);
    }

    public JoinGroupHandler(Activity activity, CodeCaller caller, Serializable... args) {
        super(activity, caller, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

        if (response.getHttpCode() == 202) {
            SessionManager manager = SessionManager.getInstance(getActivity());
            manager.createSession(response.getCookie(), SessionManager.COOKIE_KEY);
            String cookie = response.getCookie();

            if (cookie != null) {
                getCaller().call(true);
            } else {
                Log.i("no cookie", "nocokkie");
            }

            return true;

        } else {
            // http code different from 200 OK
            Logger.log("LoginHandler -- invalid credentials sent", Log.WARN);
            Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
