package hr.foi.teamup.webservice;

import java.io.Serializable;

/**
 * defines service response
 * Created by Maja Vracan on 26.10.2015..
 */
public class ServiceResponse {

    int httpCode;
    String cookie;
    String jsonResponse;

    public ServiceResponse() {
    }

    /**
     * default constructor
     * @param httpCode http code
     * @param jsonResponse response in json
     */
    public ServiceResponse(int httpCode, String jsonResponse) {
        this.httpCode = httpCode;
        this.jsonResponse = jsonResponse;
    }

    /**
     * default auth constructor
     * @param cookie auth cookie
     * @param jsonResponse response in json
     * @param httpCode http code
     */
    public ServiceResponse(String cookie, String jsonResponse, int httpCode) {
        this.cookie = cookie;
        this.jsonResponse = jsonResponse;
        this.httpCode = httpCode;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    @Override
    public String toString() {
        return this.httpCode + " " + this.jsonResponse;
    }
}
