package hr.foi.teamup.webservice;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * makes HTTP calls to web service
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceCaller {

    public static final String SERVICE_LOG_TAG = "hr.foi.teamup.debug";
    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_PUT = "PUT";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String HTTP_DELETE = "DELETE";

    /**
     * calls to url with desired method, use object param to send to url
     * @param url url to call
     * @param method method to use
     * @param object object to send
     * @return response in json format
     * @throws IOException when connection cannot open
     */
    public static ServiceResponse call(URL url, String method, Serializable object, String type, String urlEncoded) throws IOException {
        Log.i(SERVICE_LOG_TAG, "ServiceCaller -- initiating");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty(CONTENT_TYPE, type);
        connection.setRequestMethod(method);
        connection.connect();

        Log.d(SERVICE_LOG_TAG, "ServiceCaller -- successfully connected to service: " + url.toString());
        if(object != null) {
            // write
            Log.d(SERVICE_LOG_TAG, "ServiceCaller -- sending object:"
                    + object.toString() + " to " + url.toString());
            OutputStream os = connection.getOutputStream();
            os.write(new Gson().toJson(object).getBytes());
            os.close();
        }else{

            OutputStream os = connection.getOutputStream();
            os.write(urlEncoded.getBytes());
            os.close();

        }

        Log.d(SERVICE_LOG_TAG, "ServiceCaller -- receiving response from service " + url.toString());
        // read
        int code = connection.getResponseCode();
        StringBuilder json = new StringBuilder();
        if(code == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        }
        String cookie = "";
        if(code == 202){
            cookie = connection.getHeaderField("Set-Cookie");
            cookie = cookie.substring(0, cookie.indexOf(';'));
            Log.d(SERVICE_LOG_TAG, "Gettomg cookie" + cookie);
        }
        connection.disconnect();

        Log.d(SERVICE_LOG_TAG, "ServiceCaller -- received response: "
                + json.toString() + " from " + url.toString());
        return new ServiceResponse(cookie, json.toString(),code);
    }
}
