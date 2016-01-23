package hr.foi.teamup.handlers;

import android.app.Activity;

import java.io.Serializable;

import hr.foi.air.teamup.prompts.LoadingPrompt;
import hr.foi.teamup.webservice.ServiceResponseHandler;

/**
 * used when loading prompt is needed
 * Created by Tomislav Turek on 07.11.15..
 */
public abstract class ResponseHandler implements ServiceResponseHandler {

    private Activity activity;
    private Object[] args;
    private LoadingPrompt loadingPrompt;

    public ResponseHandler(Activity activity, Serializable... args) {
        this.activity = activity;
        this.args = args;
        this.loadingPrompt = new LoadingPrompt(this.activity);
    }

    @Override
    public void onPreSend() {
        loadingPrompt.showPrompt();
    }

    @Override
    public void onPostSend() {
        loadingPrompt.hidePrompt();
    }

    public Activity getActivity() {
        return activity;
    }

    public Object[] getArgs() {
        return args;
    }
}
