package hr.foi.teamup.webservice;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * creates and manages web service calls
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceAsyncTask extends AsyncTask<ServiceParams, Void, ServiceResponse> {

    ServiceParams sp;
    static final String mainUrl = "http://46.101.115.78:8080"; //"http://teamup-puding.rhcloud.com"
    SimpleResponseHandler handler;

    /**
     * default constructor
     * @param handler code that is executed on web service response
     */
    public ServiceAsyncTask(SimpleResponseHandler handler) {
        this.handler = handler;
    }

    /**
     * starts the progress dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(handler instanceof ServiceResponseHandler)
            ((ServiceResponseHandler)handler).onPreSend();
    }

    /**
     * initiates calles to web service
     * @param params parameters to use in service call
     * @return service response (http code + json)
     */
    @Override
    protected ServiceResponse doInBackground(ServiceParams... params) {
        sp = params[0];
        ServiceResponse jsonResponse = null;

        Log.d(ServiceCaller.SERVICE_LOG_TAG, "ServiceAsyncTask -- Initiating service call to " + sp.getUrl());
        try {
            URL url = new URL(mainUrl+sp.getUrl());
            String method = sp.getMethod();
            Serializable object = sp.getObject();
            jsonResponse = ServiceCaller.call(url, method, object, sp.getType(), sp.getUrlEncoded());
        } catch (MalformedURLException e) {
            Log.e(ServiceCaller.SERVICE_LOG_TAG, "ServiceAsyncTask -- failed to create URL from string " + sp.getUrl());
        } catch (IOException e) {
            Log.e(ServiceCaller.SERVICE_LOG_TAG, "ServiceAsyncTask -- cannot open connection to " + sp.getUrl());
        }

        return jsonResponse;
    }

    /**
     * calls handler sent through service parameters and stops the progress dialog
     * @param s service response (http code + json)
     */
    @Override
    protected void onPostExecute(ServiceResponse s) {
        if(sp != null && handler != null) {
            Log.i(ServiceCaller.SERVICE_LOG_TAG, "ServiceAsyncTask -- Calling service response handler");
            handler.handleResponse(s);
            if(handler instanceof ServiceResponseHandler)
                ((ServiceResponseHandler)handler).onPostSend();
        } else {
            Log.w(ServiceCaller.SERVICE_LOG_TAG, "ServiceAsyncTask -- Could not call service response handler");
        }
    }
}
