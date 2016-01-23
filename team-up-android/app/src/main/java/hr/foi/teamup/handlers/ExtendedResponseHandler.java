package hr.foi.teamup.handlers;

import android.app.Activity;

import java.io.Serializable;

/**
 * extends response handler to be able to call
 * implementation back to the original caller
 * Created by maja on 21.01.16..
 */
public abstract class ExtendedResponseHandler extends ResponseHandler {

    private CodeCaller caller;

    public ExtendedResponseHandler(Activity activity, Serializable... args) {
        super(activity, args);
    }

    public ExtendedResponseHandler(Activity activity, CodeCaller caller, Serializable... args) {
        super(activity, args);
        this.caller = caller;
    }

    public CodeCaller getCaller() {
        return caller;
    }
}
