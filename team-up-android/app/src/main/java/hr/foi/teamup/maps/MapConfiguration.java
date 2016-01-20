package hr.foi.teamup.maps;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import hr.foi.air.teamup.Logger;

/**
 *
 * Created by paz on 20.01.16..
 */
public class MapConfiguration implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private hr.foi.teamup.model.Location userLocation;
    private LocationCallback callback;
    private Activity activity;

    public MapConfiguration(LocationCallback callback,Activity activity) {
        this.callback = callback;
        this.activity = activity;
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {


        Logger.log("Location changed ", getClass().getName(), Log.DEBUG);

        userLocation = new hr.foi.teamup.model.Location();
        userLocation.setLat(location.getLatitude());
        userLocation.setLng(location.getLongitude());

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
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }



    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public LocationRequest getmLocationRequest() {
        return mLocationRequest;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }
}


