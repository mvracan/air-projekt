package hr.foi.teamup.webservice;

import android.content.Context;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 07.11.15..
 */
public abstract class ResponseHandler implements ServiceResponseHandler {

    Context context;
    Serializable[] args;

    public ResponseHandler(Context context, Serializable... args) {
        this.context = context;
        this.args = args;
    }

    public Context getContext() {
        return context;
    }

    public Serializable[] getArgs() {
        return args;
    }
}
