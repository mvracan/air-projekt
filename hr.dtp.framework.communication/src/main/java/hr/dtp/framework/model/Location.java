package hr.dtp.framework.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author DTP team
 */
@Embeddable
public class Location implements Serializable {
    
    @Column(name="latitude")
    private double lat;
    
    @Column(name="longitude")
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
    
    
}
