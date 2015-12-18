package hr.foi.teamup.webservice;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class ServiceParams {

    private String url;
    private String method;
    private String type;
    private Serializable object;
    private String urlEncoded;

    public ServiceParams(String url, String method, Serializable object) {
        this.url = url;
        this.method = method;
        this.object = object;
    }


    public ServiceParams(String url, String method, String type, Serializable object) {
        this.url = url;
        this.method = method;
        this.type = type;
        this.object = object;
    }

    public ServiceParams(String url, String method, String type, Serializable object, String urlEncoded) {
        this.url = url;
        this.method = method;
        this.type = type;
        this.object = object;
        this.urlEncoded = urlEncoded;
    }

    public String getUrlEncoded() {
        return urlEncoded;
    }

    public void setUrlEncoded(String urlEncoded) {
        this.urlEncoded = urlEncoded;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return this.url + " " + this.method;
    }

}
