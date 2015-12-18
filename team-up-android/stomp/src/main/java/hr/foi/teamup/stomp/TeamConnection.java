package hr.foi.teamup.stomp;



import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.foi.teamup.model.ChatMessage;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Location;
import hr.foi.teamup.model.Person;

/**
 * Created by paz on 14.12.15..
 */
public class TeamConnection extends Thread implements  Runnable {

    public final String websocketConnection="ws://46.101.115.78:8080";
    Stomp client;
    Map<String,String> headersSetup = new HashMap<String,String>();
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

        client = new Stomp(websocketConnection + "/chat", headersSetup, new ListenerWSNetwork() {
            @Override
            public void onState(int state) {
                Log.i("State :", new Integer(state).toString());
            }
        });

        client.connect();

        Log.i("connect - headers ", client.getHeaders().get("Cookie"));

        for(Map.Entry<String, ListenerSubscription> entry : subscriptions.entrySet()) {

            client.subscribe(new Subscription(entry.getKey(),entry.getValue()));

        }


    }

    public void send(String dest, ChatMessage message){

        this.client.send(dest,null, new Gson().toJson(message));

    }

}
