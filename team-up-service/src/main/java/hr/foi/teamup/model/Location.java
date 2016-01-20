/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.model;

import hr.foi.teamup.utilities.Distance;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author paz
 */
@Embeddable
public class Location implements Serializable {
    
    @Column(name="longitude")
    private double lat;
    
    @Column(name="latitude")
    private double lng;

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
    
    /**
     * calculates distance between this and l
     * @param l second location
     * @return distance
     */
    public double distanceTo(Location l) {
        double lat1 = this.getLat();
        double lat2 = l.getLat();
        double lon1 = this.getLng();
        double lon2 = l.getLng();
        
        double dLat = Distance.toRad(lat2-lat1);
        double dLon = Distance.toRad(lon2-lon1); 
        
        double a = Math.sin(dLat/2)
                * Math.sin(dLat/2)
                + Math.cos(Distance.toRad(lat1))
                * Math.cos(Distance.toRad(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2); 
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        
        return c * Distance.EARTH_RADIUS;
    }
    
}
