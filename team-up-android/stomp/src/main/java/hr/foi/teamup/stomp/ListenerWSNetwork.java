package hr.foi.teamup.stomp;

/**
 * Listener for web socket connection status
 * Created by paz on 08.12.15..
 */
public interface ListenerWSNetwork {
    void onState(int state);
}

