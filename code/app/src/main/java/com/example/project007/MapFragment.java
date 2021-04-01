package com.example.project007;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;


//for current location we going to catch it in trails activity and use database to pass it in here
//https://www.youtube.com/watch?v=UKUb7C3b0us How to Fetch Current Location in Android Studio | CurrentLocation | By Android Coding 2020.09.01


public class MapFragment extends Fragment{

    double returnLatitude;
    double returnLongitude;
    double currentLat;
    double currentLong;
    //GoogleMap initial_googleMap;





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
                currentLat = activity.sendCurrentLat();
                currentLong = activity.sendCurrentLong();
                //MarkerOptions marker = new MarkerOptions().position(new LatLng(currentLat, currentLong)).title("Current pos!");
                LatLng currentLoc = new LatLng(currentLat, currentLong);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 10));
                googleMap.addMarker(new MarkerOptions().title("Current pos!").position(currentLoc));
                Toast.makeText(getActivity(),"Current location selected by default!",Toast.LENGTH_SHORT).show();
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        returnLatitude = latLng.latitude;
                        returnLongitude = latLng.longitude;//return value for location class
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        Bundle location = new Bundle();
                        location.putSerializable("Location", new Location(returnLongitude, returnLatitude));
                        getParentFragmentManager().setFragmentResult("showLocation", location);

                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });



        return view;
    }

}
