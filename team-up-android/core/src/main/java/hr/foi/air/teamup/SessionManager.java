package hr.foi.air.teamup;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.foi.teamup.model.Person;

/**
 *
 * Created by Tomislav Turek on 05.11.15..
 */
public class SessionManager {

    public static final String SHARED_PREFS_NAME = "hr.foi.air.teamup.session";
    static SessionManager instance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    /**
     * initializes the session manager by opening shared preferences from application context
     * @param context current application context
     */
    private SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public static SessionManager getInstance(Context context) {
        if(instance == null) instance = new SessionManager(context);
        return instance;
    }

    /**
     * creates session object by putting current object to shared preferences
     * @param object object to put to shared prefs
     * @param field field to put object in
     */
    public boolean createSession(Serializable object, String field) {
        return editor.putString(field, new Gson().toJson(object)).commit();
    }


    /**
     * Returns current object stored in session
     * @param field field to retrieve object from
     * @return object stored in session
     */
    public <T extends Serializable> T retrieveSession(String field, Class<T> type) {
        return type.cast(new Gson().fromJson(sharedPreferences.getString(field, null), type));
    }

    /**
     * clears all values from shared preferences
     * @return true if successful, false otherwise
     */
    public boolean destroyAll() {
        return editor.clear().commit();
    }

    /**
     * clears defined value from shared preferences
     * @param field key value to delete
     * @return true if successful, false otherwise
     */
    public boolean destroySession(String field) {
        return editor.remove(field).commit();
    }

}
