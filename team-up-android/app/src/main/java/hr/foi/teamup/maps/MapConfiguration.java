package hr.foi.teamup.maps;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import hr.foi.air.teamup.Logger;

/**
 * implements the map and location interfaces
 * Created by paz on 20.01.16..
 */
public class MapConfiguration implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private LocationCallback callback;
    private Activity activity;

    public MapConfiguration(LocationCallback callback,Activity activity) {
        this.callback = callback;
        this.activity = activity;
    }

    public void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(activity.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {
        Logger.log("Location changed ", getClass().getName(), Log.DEBUG);

        // change location to known type
        hr.foi.teamup.model.Location userLocation = new hr.foi.teamup.model.Location();
        userLocation.setLat(location.getLatitude());
        userLocation.setLng(location.getLongitude());

        // handle user location
        callback.onLocationChanged(userLocation);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }
}


