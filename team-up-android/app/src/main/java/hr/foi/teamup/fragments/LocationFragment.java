package hr.foi.teamup.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import hr.foi.air.teamup.Logger;
import hr.foi.teamup.R;
import hr.foi.teamup.model.Person;

/*
 * shows markers on map
 * Created by paz on 19.01.16..
 */
public class LocationFragment extends Fragment {

    GoogleMap mMap;
    LatLng creatorPosition;
    Timer timerPaint;
    private volatile float zoom = 25;

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

    /**
     * used to retain the zoom defined by user
     */
    private GoogleMap.OnCameraChangeListener zoomListener = new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
        if (cameraPosition.zoom != zoom){
            zoom = cameraPosition.zoom;
        }
        }
    };

    /**
     * called from outside activity to set markers
     * @param teamMembers members from the team
     * @param radius team radius
     */
    public void setUserLocations(ArrayList<Person> teamMembers, double radius){
        if(isVisible()) {
            mMap.clear();
            Logger.log("Radius is " + radius);
            Logger.log("Zoom is " + zoom);
            Person creator = teamMembers.get(0);
            LatLng creatorPosition = new LatLng(creator.getLocation().getLat(), creator.getLocation().getLng());

            // zoom to default position
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(creatorPosition);

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(this.zoom);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            // draw radius
            CircleOptions teamRadius = new CircleOptions()
                    .center(creatorPosition)
                    .radius(radius)
                    .strokeWidth(2)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.parseColor("#500084d3"));

            mMap.addCircle(teamRadius);


            // everyone else is painted violet

            for (Person p : teamMembers) {

                if((p == creator ) && (p.getPanics() == 1)){
                    paintPerson(p, 1);
                    Logger.log("ja sam admin u panici");
                }
                else if (p == creator){
                    paintPerson(p, BitmapDescriptorFactory.HUE_GREEN);
                    Logger.log("ja sam admin");
                }
                else if(p.getPanics() != 1)
                  paintPerson(p, BitmapDescriptorFactory.HUE_VIOLET);
                else
                    paintPerson(p, 1);

            }
        }
    }

    /**
     * paints the marker
     * @param p person that is defined in the marker
     * @param marker color
     */
    public void paintPerson(Person p, float marker){

        mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLocation().getLat(),
                p.getLocation().getLng())).title(p.getName() + " " + p.getSurname())
                .icon(BitmapDescriptorFactory.defaultMarker(marker)));
    }

    public void paintPerson(Person p,  int red){

       Marker markerShow = mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLocation().getLat(),
                p.getLocation().getLng())).title(p.getName() + " " + p.getSurname())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        markerShow.showInfoWindow();
    }



    protected  void stopTimer(){

        timerPaint.cancel();
        timerPaint.purge();

    }

    @Override
    public void onPause() {
         super.onPause();

    }



    @Override
    public void onDestroyView() {


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
        // set zoom listener again to override outzooming
        mMap.setOnCameraChangeListener(zoomListener);
    }



    }



