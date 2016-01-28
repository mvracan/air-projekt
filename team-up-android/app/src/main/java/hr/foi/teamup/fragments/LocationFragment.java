package hr.foi.teamup.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
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

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.air.teamup.prompts.AlertPrompt;
import hr.foi.teamup.R;
import hr.foi.teamup.maps.MarkerClickHandler;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.model.Team;

/*
 * User locations map fragment
 * Created by paz on 19.01.16..
 */
public class LocationFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    GoogleMap mMap;
    MarkerClickHandler callback;
    private static final float PANIC = 0.8f;
    CameraUpdate zoomCamera;
    Person creator;


    private volatile float zoom = 25;

    public void setCallback(MarkerClickHandler callback) {
        this.callback = callback;
    }

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
            creator = teamMembers.get(0);
            LatLng creatorPosition = new LatLng(creator.getLocation().getLat(), creator.getLocation().getLng());

            // zoom to default position
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(creatorPosition);

            zoomCamera = CameraUpdateFactory.zoomTo(this.zoom);
            mMap.moveCamera(center);
            mMap.animateCamera(zoomCamera);

            // draw radius
            CircleOptions teamRadius = new CircleOptions()
                    .center(creatorPosition)
                    .radius(radius)
                    .strokeWidth(2)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.parseColor("#500084d3"));

            mMap.addCircle(teamRadius);


            for (Person p : teamMembers) {

                Logger.log("PANIC JE :" + p.getPanic());
               if(p.getPanic() == 1)
                   paintPerson(p, BitmapDescriptorFactory.HUE_RED);
               else if(p == creator)
                   paintPerson(p, BitmapDescriptorFactory.HUE_GREEN);
               else paintPerson(p, BitmapDescriptorFactory.HUE_VIOLET);

            }
        }
    }

    /**
     * paints the marker based on user status
     * @param p person that is defined in the marker
     * @param marker color
     */
    public void paintPerson(Person p, float marker){

        Marker markerShow = mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLocation().getLat(),
                p.getLocation().getLng())).title(p.getName() + " " + p.getSurname())
                .icon(BitmapDescriptorFactory.defaultMarker(marker))
                .snippet(p.getCredentials().getUsername()));

        if(marker == BitmapDescriptorFactory.HUE_RED) {
            markerShow.setAlpha(PANIC);
            markerShow.showInfoWindow();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment mMapFragment = (com.google.android.gms.maps.MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        zoomCamera = CameraUpdateFactory.zoomTo(this.zoom);
        mMap.animateCamera(zoomCamera);
        mMap.setOnCameraChangeListener(zoomListener);

        if(isCreator())
            mMap.setOnMarkerClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        // set zoom listener again to override outzooming
        mMap.setOnCameraChangeListener(zoomListener);
    }

    /**
     * Calm down user with Prompts dialog
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        if(marker.getAlpha() != PANIC)
           return false;

        final String panicUser = marker.getSnippet();
        Logger.log("Marker data: " + panicUser);

        DialogInterface.OnClickListener signOutListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                callback.onMarkerClick(panicUser);

            }
        };

        AlertPrompt signOutPrompt = new AlertPrompt(getActivity());
        signOutPrompt.prepare(R.string.calmdown, signOutListener,
                R.string.yes, null, R.string.cancel);
        signOutPrompt.showPrompt();



        return true;
    }

    /**
     * Check creator role of user in current team
     * @return
     */
    public boolean isCreator(){

        SessionManager manager = SessionManager.getInstance(getActivity());

        Team t = manager.retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);
        Person p = manager.retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);

        return (t.getCreator().getIdPerson() == p.getIdPerson());
    }



}



