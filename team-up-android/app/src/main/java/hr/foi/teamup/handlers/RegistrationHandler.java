package hr.foi.teamup.handlers;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import hr.foi.air.teamup.Logger;
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

    public RegistrationHandler(Activity activity, Serializable... args) {
        super(activity, args);
    }

    @Override
    public boolean handleResponse(ServiceResponse response) {
        Credentials credentials = (Credentials) getArgs()[0];
        Logger.log("Deserialized arguments: " + credentials.toString(), getClass().getName(), Log.DEBUG);

        if(response.getHttpCode() == 200) {
            Logger.log("Successfully registered user, logging in now...", getClass().getName(), Log.DEBUG);
            // login
            LoginHandler loginHandler = new LoginHandler(getActivity());
            ServiceParams params = new ServiceParams(
                    getActivity().getString(hr.foi.teamup.webservice.R.string.person_login_path),
                    ServiceCaller.HTTP_POST, credentials);
            new ServiceAsyncTask(loginHandler).execute(params);
            return true;
        } else {
            Logger.log("Registration failed, server returned code "
                    + response.getHttpCode(), getClass().getName(), Log.WARN);
            // show fail
            Toast.makeText(getActivity(),
                    "Registration failed, please try again (" + response.getHttpCode() + ")",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
