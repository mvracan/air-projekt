package hr.foi.teamup.handlers;

import android.content.Context;

import java.io.Serializable;

import hr.foi.air.teamup.prompts.LoadingPrompt;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.ServiceResponseHandler;

/**
 *
 * Created by Tomislav Turek on 07.11.15..
 */
public abstract class ResponseHandler implements ServiceResponseHandler {

    Context context;
    Serializable[] args;
    LoadingPrompt loadingPrompt;

    public ResponseHandler(Context context, Serializable... args) {
        this.context = context;
        this.args = args;
        this.loadingPrompt = new LoadingPrompt(this.context);
        this.loadingPrompt.showPrompt();
    }
}
