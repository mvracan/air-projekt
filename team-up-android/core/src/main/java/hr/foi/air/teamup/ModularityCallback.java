package hr.foi.air.teamup;

import android.content.DialogInterface;

/**
 * modular callback to our stomp subscription
 * Created by Tomislav Turek on 27.01.16..
 */
public interface ModularityCallback {

    void onAction(String message);

}
