package hr.foi.teamup.handlers;

import android.util.Log;

import java.util.Map;

import hr.foi.teamup.stomp.ListenerSubscription;

/**
 * Created by paz on 14.12.15..
 */
public class StompMessageHandler implements ListenerSubscription {

    public StompMessageHandler() {
    }


    @Override
    public void onMessage(Map<String, String> headers, String body) {

        Log.i("poruka : ", body);

    }

    @Override
    public void onPreSend() {

    }

    @Override
    public void onPostSend() {

    }
}
