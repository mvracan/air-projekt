package hr.foi.teamup.stomp;



import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import hr.foi.teamup.model.TeamMessage;

/**
 *
 * Created by paz on 14.12.15..
 */
public class TeamConnection extends Thread implements Runnable {

    public final String websocketConnection="ws://46.101.115.78:8080";
    Stomp client;
    Map<String,String> headersSetup = new HashMap<>();
    HashMap <String, ListenerSubscription> subscriptions;
    String cookie;

    public TeamConnection( HashMap<String, ListenerSubscription> subscriptions, String cookie) {
        this.subscriptions = subscriptions;
        this.cookie = cookie;
    }

    @Override
    public void run() {

        headersSetup.put("Cookie", cookie);
        Log.i("connect", "going to connect to stomp with cookie" + cookie);
        Log.i("connect", "going to connect to stomp");

        client = new Stomp(websocketConnection + "/team", headersSetup, new ListenerWSNetwork() {
            @Override
            public void onState(int state) {
                Log.i("State :", Integer.toString(state));
            }
        });

        client.connect();

        Log.i("connect - headers ", client.getHeaders().get("Cookie"));

        for(Map.Entry<String, ListenerSubscription> entry : subscriptions.entrySet()) {

            client.subscribe(new Subscription(entry.getKey(),entry.getValue()));

        }


    }

    public <T> void send(String dest, T message){

        String m = new Gson().toJson(message);
        Log.i("Stomp message", m);
        this.client.send(dest,null, m);

    }

}
