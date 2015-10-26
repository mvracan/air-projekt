package hr.foi.teamup.webservice;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceParams {

    private String url;
    private String method;
    private Serializable object;
    private ServiceResponseHandler handler;

    public ServiceParams(String url, String method, Serializable object, ServiceResponseHandler handler) {
        this.url = url;
        this.method = method;
        this.object = object;
        this.handler = handler;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Serializable getObject() {
        return object;
    }

    public void setObject(Serializable object) {
        this.object = object;
    }

    public ServiceResponseHandler getHandler() {
        return handler;
    }

    public void setHandler(ServiceResponseHandler handler) {
        this.handler = handler;
    }

    @Override
    public String toString() {
        return this.url + " " + this.method;
    }
}
