package hr.foi.teamup.model;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 25.10.15..
 */
public class Location implements Serializable {

    double lat;
    double lng;

    public Location() {
    }

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return this.lat + " " + this.lng;
    }
}
