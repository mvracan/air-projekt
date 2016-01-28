package hr.foi.teamup.stomp;

import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread class for managing Stomp protocol
 * Created by paz on 14.12.15..
 */
public class TeamConnection extends Thread implements Runnable {

    public final String websocketConnection="ws://46.101.115.78:8080";
    Stomp client;
    Map<String,String> headersSetup = new HashMap<>();
    HashMap <String, ListenerSubscription> subscriptions;
    String cookie;

    boolean active;

    /**
     * Constructor with user subscription channels and JSESSIONID cookie
     * @param subscriptions
     * @param cookie
     */
    public TeamConnection( HashMap<String, ListenerSubscription> subscriptions, String cookie) {
        this.subscriptions = subscriptions;
        this.cookie = cookie;
        this.active = true;
    }
    /**
     * Starts thread, establishes socket connection and subscribe user to subscribe channels
    */
    @Override
    public void run() {


        if(active) {
            headersSetup.put("Cookie", cookie);
            Log.i("connect", "going to connect to stomp with cookie" + cookie);
            Log.i("connect", "going to connect to stomp");

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

            for (Map.Entry<String, ListenerSubscription> entry : subscriptions.entrySet()) {

                client.subscribe(new Subscription(entry.getKey(), entry.getValue()));

            }
        }


    }

    /**
     * Stops thread and disconnect websocket from server
     */
    public void finish() {
        active = false;
        client.disconnect();
    }
    /**
     * Send message through Stomp to destination path
     * @param dest
     * @Param message
     */
    public <T> void send(String dest, T message){

        String m = new Gson().toJson(message);
        Log.i("Stomp message", m);
        this.client.send(dest,null, m);

    }

}
