package com.icloud.cronin.peter.ui.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.application.ICourseApp;
import com.icloud.cronin.peter.data.database.ICourseDatabase;
import com.icloud.cronin.peter.data.model.RaceLocation;
import com.icloud.cronin.peter.util.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String Tag = "MapActivity";
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 12f;
    private boolean mLocationPermissionsGranted = false;

    private GoogleMap mMap;

    UiSettings mapSettings;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private ICourseDatabase database = ICourseApp.iCourseDatabase;
    private List<RaceLocation> raceLocations;
    private String course;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        course = getIntent().getStringExtra(Constants.COURSE).trim();
        course = course.replaceFirst("\\u00A0","");
        getLocationPermissions();
    }

    private void getLocationPermissions() {
        Log.d(Tag, "getLocationPermissions: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean fineLocationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean coarseLocationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (fineLocationGranted && coarseLocationGranted) {
                mLocationPermissionsGranted = true;
                initMap();
            }else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSIONS_REQUEST_CODE);
            }
        }else {
            initMap();
        }
    }

    private void initMap() {
        Log.d(Tag, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(Tag, "moveCamera: moving the camera to: lat:" + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(Tag, "onMapReady: map is ready");
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);
        mapSettings = mMap.getUiSettings();

        if (mLocationPermissionsGranted) {
            mMap.setMyLocationEnabled(true);
            mapSettings.setMyLocationButtonEnabled(true);
            mapSettings.setCompassEnabled(true);
            mapSettings.setZoomControlsEnabled(true);
            getDeviceLocation();
        }

    }

    private void getDeviceLocation() {
        Log.d(Tag, "getDeviceLocation: getting devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                mFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    currentLocation = location;
                                    Log.d(Tag, "onComplete: found location!");
                                    String[] locationInitials = course.split("\\s+");
                                    findLocationByInitials(locationInitials);
                                } else {
                                    Log.d(Tag, "onComplete: current location is null");
                                }
                            }
                        });
//                locationTask.addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful()) {
//                            Log.d(Tag, "onComplete: found location!");
//                            String[] locationInitials = course.split("\\s+");
//                            findLocationByInitials(locationInitials);
//                        } else {
//                            Log.d(Tag, "onComplete: current location is null");
//                        }
//                    }
//                });
            }
        } catch (SecurityException e) {
            Log.d(Tag, "getDeviceLocation: SecurityException:" + e.getMessage());
            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
        }
    }

    private void findLocationByInitials(final String[] locationInitials){
        raceLocations = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(String initial: locationInitials){
                    RaceLocation raceLocation = database.locationDao().findLocationByMarker(initial);
                    if(raceLocation != null) {
                        raceLocations.add(raceLocation);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateLocationsOnMap(raceLocations);
                    }
                });
            }
        }).start();
    }

    private void updateLocationsOnMap(List<RaceLocation> raceLocations) {
        moveCamera(new LatLng(Double.parseDouble(raceLocations.get(0).getLatitude()),
                Double.parseDouble(raceLocations.get(0).getLongitude())), DEFAULT_ZOOM);

        List<RaceLocation> duplicateLocations = new ArrayList<>();
        Set<RaceLocation> uniqueLocations = new HashSet<>();

        for(RaceLocation raceLocation: raceLocations){
            if(!uniqueLocations.add(raceLocation)){
                duplicateLocations.add(raceLocation);
                uniqueLocations.remove(raceLocation);
            }
        }

        System.out.println("duplicateLocations = " + duplicateLocations);
        System.out.println("uniqueLocations = " + uniqueLocations);

        for(RaceLocation raceLocation: uniqueLocations){
            double latitude = Double.parseDouble(raceLocation.getLatitude());
            double longitude = Double.parseDouble(raceLocation.getLongitude());
            setMarker(latitude,longitude, raceLocation.getMarkerCharacter().toUpperCase());
        }

        for(RaceLocation location: duplicateLocations){
            setUpDuplicateLocations(location);
        }

    }

    private void setUpDuplicateLocations(RaceLocation clusterLocation) {

        double lat = Double.parseDouble(clusterLocation.getLatitude());
        double lng = Double.parseDouble(clusterLocation.getLongitude());

        for (int i = 0; i < 2; i++) {
            double offset = i * 0.00002;
            lat = lat + offset;
            lng = lng + offset;
            setMarker(lat,lng, clusterLocation.getMarkerCharacter().toUpperCase());
        }
    }

    private void setMarker(double latitude, double longitude, String title) {
        HashMap<String,Float> distanceAndBearingMap = calculateDistanceAndBearing(new LatLng(latitude,longitude));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .snippet("Distance : "+distanceAndBearingMap.get("DISTANCE")
                        + " metres, Bearing: "+distanceAndBearingMap.get("BEARING"))
                .icon(getIcon(title)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(Tag, "onRequestPermissionsResult: called");
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        mLocationPermissionsGranted = false;
                        Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                mLocationPermissionsGranted = true;
                initMap();
            }
        }
    }

    private BitmapDescriptor getIcon(String title){
        switch (title){
            case "A":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_a);
            case "B":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_b);
            case "C":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_c);
            case "D":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_d);
            case "E":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_e);
            case "F":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker60_f);
            case "G":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_g);
            case "H":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_h);
            case "I":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_i);
            case "J":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_j);
            case "K":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_k);
            case "M":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_m);
            case "N":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_n);
            case "O":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_o);
            case "P":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_p);
            case "Q":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_q);
            case "R":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_r);
            case "S":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_s);
            case "T":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_t);
            case "U":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_u);
            case "V":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_v);
            case "W":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_w);
            case "X":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker_x);
            case "Z":
                return BitmapDescriptorFactory.fromResource(R.drawable.mapmarker60_z);
            default:
                return null;
        }
    }

    private HashMap<String,Float> calculateDistanceAndBearing(LatLng latLng2) {
        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(currentLocation.getLatitude());
        loc1.setLongitude(currentLocation.getLongitude());

        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);

        //Range from -180 to 180
        float bearing = loc1.bearingTo(loc2);

        //Distance in meteres
        float distance = loc1.distanceTo(loc2);

        HashMap<String,Float> distanceBearingMap = new HashMap<>();
        distanceBearingMap.put("BEARING",bearing);
        distanceBearingMap.put("DISTANCE",distance);
        return distanceBearingMap;
    }


}
