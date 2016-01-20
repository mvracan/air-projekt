package hr.foi.teamup.maps;

import hr.foi.teamup.model.Location;

/**
 * used when location is changed to run some code
 * Created by paz on 20.01.16..
 */
public interface LocationCallback {

    void onLocationChanged(Location location);

}
