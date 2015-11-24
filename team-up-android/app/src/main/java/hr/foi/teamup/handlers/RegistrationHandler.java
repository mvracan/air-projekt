package hr.foi.teamup.handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;

/**
 * handles registration calls
 * Created by Tomislav Turek on 07.11.15..
 */
public class RegistrationHandler extends ResponseHandler {

    public RegistrationHandler(Context context, Serializable... args) {
        super(context, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Credentials credentials = (Credentials) this.args[0];
        Log.i("hr.foi.teamup.debug", "RegistrationHandler -- deserialized arguments: " + credentials.toString());

        if(response.getHttpCode() == 200) {
            Log.i("hr.foi.teamup.debug", "RegistrationHandler -- successfully registered user, logging in now...");
            // login
            LoginHandler loginHandler = new LoginHandler(this.context);
            ServiceParams params = new ServiceParams(
                    context.getString(hr.foi.teamup.webservice.R.string.person_login_path),
                    ServiceCaller.HTTP_POST, credentials);
            new ServiceAsyncTask(loginHandler).execute(params);
            return true;
        } else {
            Log.w("hr.foi.teamup.debug",
                    "RegistrationHandler -- registration failed, server returned code " + response.getHttpCode());
            // show fail
            Toast.makeText(this.context,
                    "Registration failed, please try again (" + response.getHttpCode() + ")",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
