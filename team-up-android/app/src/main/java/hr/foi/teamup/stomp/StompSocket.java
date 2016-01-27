package hr.foi.teamup.stomp;

import java.util.HashMap;

/**
 * wraps stomp socket to group join algorithm
 * Created by Tomislav Turek on 26.01.16..
 */
public class StompSocket {

    private static TeamConnection socket;
    private HashMap<String, ListenerSubscription> subscriptionChannels;

    /**
     * default constructor
     */
    public StompSocket() {
        subscriptionChannels = new HashMap<>();
    }

    /**
     * adds a subscription channel, should be called before
     * subscribe method so it can function properly
     * @param ls callback that handles messages when channel receives message
     * @param path url path to subscription
     */
    public void addSubscriptionChannel(ListenerSubscription ls, String path) {
        subscriptionChannels.put(path, ls);
    }

    /**
     * removes designated subscription channel, should be called
     * before subscribe method, if person is already subscribed when
     * this method is called then the socket should be refreshed by
     * calling the subscribe method again
     * @param path url path to subscription
     */
    public void removeSubscriptionChannel(String path) {
        subscriptionChannels.remove(path);
    }

    /**
     * subscribes person to added subscription channels, note that
     * person should be authenticated with stomp authentication
     * beforehand to transfer cookie
     * @param cookie authentication cookie
     */
    public void subscribe(String cookie) {
        if(socket != null)
            socket.finish();
        socket = new TeamConnection(subscriptionChannels, cookie);
        socket.start();
    }

    /**
     * sends message to desired destination, socket should be in a
     * started state through subscribe method
     * @param destination url where the message is sent
     * @param content message content
     */
    public <T> void send(String destination, T content) {
        if(socket != null)
            socket.send(destination, content);
    }

    /**
     * returns socket state
     * @return true if active, false otherwise
     */
    public static boolean isActive() {
        return socket != null;
    }

    /**
     * sends stomp stop and closes socket
     */
    public void close() {
        if(socket != null) {
            socket.finish();
            socket = null;
        }
    }
}
