package hr.foi.teamup.maps;

import hr.foi.teamup.model.Location;

/**
 * used when location is changed to run action based on user location
 * Created by paz on 20.01.16..
 */
public interface LocationCallback {

    void onLocationChanged(Location location);

}
