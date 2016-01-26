package hr.foi.teamup.stomp;

import android.util.Log;

import hr.foi.air.teamup.Logger;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.SimpleResponseHandler;

/**
 *
 * Created by Tomislav Turek on 26.01.16..
 */
public class StompAuthentication {

    public String cookie;

    /**
     * authenticates the user for stomp transfer
     * @param client person that will be authenticated
     */
    public void authenticate(Person client) {
        new ServiceAsyncTask(new SimpleResponseHandler() {
            @Override
            public boolean handleResponse(ServiceResponse response) {
                Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

                if (response.getHttpCode() == 202) {
                    cookie = response.getCookie();
                    return true;
                } else {
                    Logger.log("Cookie fail", Log.WARN);
                    return false;
                }
            }
        }).execute(new ServiceParams(
                "/login",
                ServiceCaller.HTTP_POST, ServiceCaller.X_WWW_FORM_URLENCODED, null,
                "username=" + client.getCredentials().getUsername() +
                        "&password=" + client.getCredentials().getPassword()));
    }

    public String getCookie() {
        return cookie;
    }
}
