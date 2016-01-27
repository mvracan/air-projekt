package hr.foi.teamup.webservice;

/**
 * handles web service responses with ability to
 * execute pre call and post call code
 * Created by Tomislav Turek on 25.10.15..
 */
public interface ServiceResponseHandler extends SimpleResponseHandler {

    void onPreSend();
    void onPostSend();

}
