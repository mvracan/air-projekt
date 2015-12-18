package hr.foi.teamup.webservice;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 25.10.15..
 */
public interface ServiceResponseHandler extends SimpleResponseHandler {

    void onPreSend();
    void onPostSend();

}
