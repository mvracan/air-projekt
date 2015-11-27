package hr.foi.air.teamup;

import android.util.Log;

/**
 * logging class
 * Created by Tomislav Turek on 24.11.15..
 */
public class Logger {

    public static final String LOG_TAG = "hr.foi.teamup.debug";

    /**
     * logs message to android monitor as information level
     * @param message message to log
     */
    public static void log(String message) {
        log(message, Log.INFO);
    }

    /**
     * logs message to android monitor
     * @param message message to log
     * @param level log level
     */
    public static void log(String message, int level) {
        switch(level) {
            case Log.INFO:
                Log.i(LOG_TAG, message); break;
            case Log.ERROR:
                Log.e(LOG_TAG, message); break;
            case Log.VERBOSE:
                Log.v(LOG_TAG, message); break;
            case Log.DEBUG:
                Log.d(LOG_TAG, message); break;
            default:
                Log.w(LOG_TAG, message);
        }
    }

}
