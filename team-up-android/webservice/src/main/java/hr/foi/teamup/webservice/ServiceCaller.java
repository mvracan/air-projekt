package hr.foi.teamup.webservice;

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
    public static String call(URL url, String method, Serializable object) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod(method);
        connection.connect();

        if(object != null) {
            // write
            OutputStream os = connection.getOutputStream();
            os.write(null); // needs Gson
            os.close();
        }

        // read
        BufferedInputStream bis = (BufferedInputStream) connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
        String line;
        String json = null;
        while((line = reader.readLine()) != null) {
            json += line;
        }
        connection.disconnect();
        return json;
    }

}
