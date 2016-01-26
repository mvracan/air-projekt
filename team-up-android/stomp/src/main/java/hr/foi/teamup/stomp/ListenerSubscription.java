package hr.foi.teamup.stomp;
import java.util.Map;

/**
 *
 * Created by paz on 08.12.15..
 */

public interface ListenerSubscription {
    void onMessage(Map<String, String> headers, String body);

}
