package hr.foi.teamup.stomp;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paz on 09.12.15..
 */
public class StompAsyncTask extends AsyncTask<String, String, Void> {

    String sp;
    static final String mainUrl = "serverurl";
    ListenerSubscription handler;

    public StompAsyncTask(ListenerSubscription handler) {
        this.handler = handler;
    }

    /**
     * starts the progress dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(handler instanceof ListenerSubscription)
            ((ListenerSubscription)handler).onPreSend();
    }

    /**
     * initiates calles to web socket
     * @param params parameters to use in service call
     * @return service response (http code + json)
     */
    @Override
    protected Void doInBackground(String... params) {

        Stomp client=null;


        client=connection();


        subscribeTo(client,params[0]);


        return null;
    }


    private Stomp connection() {
        Map<String,String> headersSetup = new HashMap<String,String>();
        Stomp stomp = new Stomp(mainUrl, headersSetup, new ListenerWSNetwork() {
            @Override
            public void onState(int state) {
            }
        });
        stomp.connect();

        return stomp;
    }

    private void subscribeTo(Stomp stomp, String ... params){


        stomp.subscribe(new Subscription(params[0], new ListenerSubscription() {
            @Override
            public void onMessage(Map<String, String> headers, String body) {

                publishProgress(body);

            }

            @Override
            public void onPreSend() {

            }

            @Override
            public void onPostSend() {

            }
        }));

    }


    //Executed on main UI thread.
    @Override
    protected void onProgressUpdate(String... values) {
        //TODO on main thread show msg in textview

    }


    /**
     * calls handler sent through service parameters and stops the progress dialog
     * @param s service response (http code + json)
     */
    /*
    @Override
    protected void onPostExecute(ServiceResponse s) {
        if(sp != null) {
            Log.i(ServiceCaller.SERVICE_LOG_TAG, "ServiceAsyncTask -- Calling service response handler");
            handler.handleResponse(s);
            if(handler instanceof ServiceResponseHandler)
                ((ServiceResponseHandler)handler).onPostSend();
        } else {
            Log.w(ServiceCaller.SERVICE_LOG_TAG, "ServiceAsyncTask -- Could not call service response handler");
        }
    }
    */
}

