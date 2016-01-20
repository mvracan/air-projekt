package hr.foi.teamup.fragments;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import hr.foi.air.teamup.Logger;
import hr.foi.teamup.R;
import hr.foi.teamup.TeamActivity;
import hr.foi.teamup.model.Person;


/*
 * Created by paz on 19.01.16..
 */

public class LocationFragment extends Fragment {

    GoogleMap mMap;
    LatLng creatorPosition;
    Timer timerPaint;
    private volatile float ZOOM = 25;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private static View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {

            view = inflater.inflate(R.layout.fragment_map, container, false);

        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    private GoogleMap.OnCameraChangeListener zoomListener = new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            if (cameraPosition.zoom != ZOOM){
                Logger.log("ZOOMAM");
                ZOOM = cameraPosition.zoom;
            }
        }
    };

    public void setUserLocations(ArrayList<Person> teamMembers, double radius){
        if(isVisible()) {

            mMap.clear();
            Log.i(" maps ", " setUserLocations ");

            Log.i(" radius ", "radius is " + radius);

            Log.i(" maps ", "zoom is " + ZOOM);
            Person creator = teamMembers.get(0);


            creatorPosition = new LatLng(creator.getLocation().getLat(),creator.getLocation().getLng());

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(creatorPosition);

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(ZOOM);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);



            CircleOptions teamRadius = new CircleOptions()
                    .center(creatorPosition)
                    .radius(radius)
                    .strokeWidth(2)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.parseColor("#500084d3"));

            mMap.addCircle(teamRadius);

            paintPerson(creator, BitmapDescriptorFactory.HUE_GREEN);

            Logger.log("Paint peron " + creator.getName());

            teamMembers.remove(0);
            for (Person p : teamMembers) {

                Logger.log("Paint peron " + p.getName());
                paintPerson(p, BitmapDescriptorFactory.HUE_VIOLET);

            }
        }

    }


    public void paintPerson(Person p, float marker){

        mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLocation().getLat(),
                p.getLocation().getLng())).title(p.getName() + " " + p.getSurname())
                .icon(BitmapDescriptorFactory.defaultMarker(marker)));
    }

    public void paintPerson(Person p, float marker, int time){

            if(timerPaint != null)
                stopTimer();

            timerPaint = new Timer();


            timerPaint.schedule(new PaintPanicPerson(p, marker), 0, time);

    }

    protected  void stopTimer(){

        timerPaint.cancel();
        timerPaint.purge();

    }

    @Override
    public void onPause() {
        stopTimer();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        stopTimer();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment mMapFragment = (com.google.android.gms.maps.MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();
        mMap.setOnCameraChangeListener(zoomListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.setOnCameraChangeListener(zoomListener);
    }

    class PaintPanicPerson extends TimerTask{

        Person panicPerson;
        float markerPanic;

        public PaintPanicPerson(Person p,float m) {
            this.panicPerson = p;
            this.markerPanic = m;
        }


            @Override
            public void run () {

                getActivity().runOnUiThread(new Runnable() {


                    @Override
                    public void run() {


                        paintPerson(panicPerson, markerPanic);

                    }


                });

            }
        }//End paint class


    }



