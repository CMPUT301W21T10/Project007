package com.example.project007;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * AllMapViewFragment
 * generate location view
 * click to select location
 */
//this frag is design to show all trails location (if it has)
//we will catch all trails location data in this class
public class AllMapViewFragment extends Fragment {

    ArrayList<Location> locationArrayList;
    ArrayList<String> trailsTitleList;

    //disable menu in frag
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    //disable menu in frag

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //receive data from activity
                TrailsActivity activity = (TrailsActivity) getActivity();
                locationArrayList = activity.sendLocationData();
                trailsTitleList = activity.sendTrailsTitleData();
                if(locationArrayList.size()==0){
                    Toast.makeText(getActivity(),"Sorry, there are no location to show :(",Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < locationArrayList.size(); i++){
                    Location location = locationArrayList.get(i);
                    String TrailsTitle = trailsTitleList.get(i);
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng currentLoc = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions().title(TrailsTitle).position(currentLoc));
                }
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Toast.makeText(getActivity(),"Sorry, this map is view only :(",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }
}