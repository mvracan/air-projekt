package hr.foi.air.teamup;

import android.util.Log;

/**
 * logging class
 * Created by Tomislav Turek on 24.11.15..
 */
public class Logger {

    public static final String LOG_TAG = "hr.foi.teamup.debug";
    private static final String SEPARATOR = " -- ";
    private static final String DEFAULT_CLASS = "Object";

    /**
     * logs message to android monitor as information level and default filename
     * @param message message to log
     */
    public static void log(String message) {
        log(message, Log.INFO);
    }

    /**
     * logs to android monitor as information level for passed filename
     * @param message message to log
     * @param object class in which logging occured
     */
    public static void log(String message, String object) {
        log(message, object, Log.INFO);
    }

    /**
     * logs to android monitor for default filename
     * @param message message to log
     * @param level logging level
     */
    public static void log(String message, int level) {
        log(message, DEFAULT_CLASS, level);
    }

    /**
     * logs message to android monitor
     * @param message message to log
     * @param object class in which logging occured
     * @param level log level
     */
    public static void log(String message, String object, int level) {
        message = object + SEPARATOR + message;
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
