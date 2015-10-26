package hr.foi.teamup.webservice;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceAsyncTask extends AsyncTask<ServiceParams, Void, String> {

    ServiceParams sp;

    @Override
    protected String doInBackground(ServiceParams... params) {
        sp = params[0];
        String jsonResponse = null;

        Log.i("hr.foi.teamup.debug", "ServiceAsyncTask -- Initiating service call to " + sp.toString());
        try {
            URL url = new URL(sp.getUrl());
            String method = sp.getMethod();
            Serializable object = sp.getObject();
            jsonResponse = ServiceCaller.call(url, method, object);
        } catch (MalformedURLException e) {
            Log.e("hr.foi.teamup.debug", "ServiceAsyncTask -- failed to create URL from string " + sp.getUrl());
        } catch (IOException e) {
            Log.e("hr.foi.teamup.debug", "ServiceAsyncTask -- cannot open connection to " + sp.getUrl());
        }

        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        if(sp != null) {
            Log.i("hr.foi.teamup.debug", "ServiceAsyncTask -- Calling service response handler");
            sp.getHandler().handleResponse(s);
        } else {
            Log.w("hr.foi.teamup.debug", "ServiceAsyncTask -- Could not call service response handler");
        }
    }
}
