package hr.foi.teamup.handlers;

import android.content.Context;

import java.io.Serializable;

import hr.foi.air.teamup.prompts.LoadingPrompt;
import hr.foi.teamup.webservice.ServiceResponseHandler;

/**
 * used when loading prompt is needed
 * Created by Tomislav Turek on 07.11.15..
 */
public abstract class ResponseHandler implements ServiceResponseHandler {

    private Context context;
    private Object[] args;
    private LoadingPrompt loadingPrompt;

    public ResponseHandler(Context context, Serializable... args) {
        this.context = context;
        this.args = args;
        this.loadingPrompt = new LoadingPrompt(this.context);
    }

    @Override
    public void onPreSend() {
        loadingPrompt.showPrompt();
    }

    @Override
    public void onPostSend() {
        loadingPrompt.hidePrompt();
    }

    public Context getContext() {
        return context;
    }

    public Object[] getArgs() {
        return args;
    }
}
