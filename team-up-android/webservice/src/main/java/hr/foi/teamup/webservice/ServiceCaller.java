package hr.foi.teamup.webservice;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
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

    /**
     * calls to url with desired method, use object param to send to url
     * @param url url to call
     * @param method method to use
     * @param object object to send
     * @return response in json format
     * @throws IOException when connection cannot open
     */
    public static ServiceResponse call(URL url, String method, Serializable object) throws IOException {
        Log.i("hr.foi.teamup.debug", "ServiceCaller -- initiating");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod(method);
        connection.connect();

        Log.i("hr.foi.teamup.debug", "ServiceCaller -- successfully connected to service: " + url.toString());
        if(object != null) {
            // write
            Log.i("hr.foi.teamup.debug", "ServiceCaller -- sending object:"
                    + object.toString() + " to " + url.toString());
            OutputStream os = connection.getOutputStream();
            os.write(new Gson().toJson(object).getBytes());
            os.close();
        }

        Log.i("hr.foi.teamup.debug", "ServiceCaller -- receiving response from service " + url.toString());
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
        connection.disconnect();

        Log.i("hr.foi.teamup.debug", "ServiceCaller -- received response: "
                + json.toString() + " from " + url.toString());
        return new ServiceResponse(code, json.toString());
    }

}
