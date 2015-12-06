package com.andrewtorr.venuemenu;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    @Bind(R.id.add_marker_button)
    FloatingActionButton addMarkerFab;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ButterKnife.bind(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        context = getApplicationContext();

        Log.d("context", context.toString());

        //addMarkerFab = new FloatingActionButton(getApplicationContext());
        addMarkerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location location; //location
                try {
                    location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                    if (location != null) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
                        mMap.addMarker(markerOptions);
                    }
                } catch (SecurityException e) {
                    Log.e("Error:", e.getLocalizedMessage());
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(4);
        mMap.setMyLocationEnabled(true);

        BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.mipmap.trollface);
        GroundOverlay groundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                .image(image)
                .position(new LatLng(39.773194, -86.158391), 500)
                .transparency((float) 0.5));

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("Map Activity", "marker clicked!");
                return false;
            }
        });

        Location location; //location
        try {
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(15)                   // Sets the zoom
                        .bearing(45)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } catch (SecurityException e) {
            Log.e("Error:", e.getLocalizedMessage());
        }
    }
}
