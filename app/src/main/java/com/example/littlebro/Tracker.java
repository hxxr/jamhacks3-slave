package com.example.littlebro;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public final class Tracker {
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;

    // Runs upon receiving location updates.
    private Runnable onReceive = new Runnable() { public void run() { } };
    // Whether we are receiving data or not.
    private boolean receiving = false;
    // Latitude of recorded location (degrees).
    private double ly = 0;
    // Longitude of recorded location (degrees).
    private double lx = 0;

    private Tracker(Context c) {
        int permissionCheck = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((AppCompatActivity)c, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 75);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(c);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null || !receiving)
                return;
            Location location = locationResult.getLastLocation();
            ly = location.getLatitude();
            lx = location.getLongitude();
            onReceive.run();
        }
    };




    // Get latitude in degrees.
    public double getLatitude() { return ly; }

    // Get longitude in degrees.
    public double getLongitude() { return lx; }

    // Enable.
    public void begin() { receiving = true; }

    // Disable.
    public void cease() { receiving = false; }

    // Set onReceive.
    public void setOnReceive(Runnable R) { onReceive = R; }




    // Singleton stuff.
    private static Tracker i = null;
    public static synchronized Tracker getInstance() { return i; }
    public static synchronized void initialize(Context c) { i = new Tracker(c); }
}