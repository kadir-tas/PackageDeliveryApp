package com.huawei.probation.packagedeliveryapp.manager.location.factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.huawei.probation.packagedeliveryapp.manager.location.listener.LocationListener;

import java.io.UnsupportedEncodingException;

public class GoogleLocationServiceImpl extends BaseLocations {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback mLocationCallback;

    GoogleLocationServiceImpl(Activity activity) {
        super(activity, "GoogleLocationServiceImpl");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void subscribewLocationUpdates(final LocationListener locationListener) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (this.mLocationCallback == null) {

            this.mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        try {
                            locationListener.onLocationUpdate(locationResult.getLastLocation());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            fusedLocationProviderClient
                    .requestLocationUpdates(mLocationRequest, this.mLocationCallback, Looper.getMainLooper())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            log("Subscribed location updates with Huawei Services");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            logError("Subscribe process is fail with Huawei Services " + e.getMessage());
                        }
                    });
        } else {
            logError("You are already subscribed location updates");
        }
    }

    @Override
    public void unsubscribeLocationUpdates() {
        if (this.mLocationCallback == null) return;
        else {
            fusedLocationProviderClient.removeLocationUpdates(this.mLocationCallback)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            log("Unsubscribed location updates with Huawei Services");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            logError("Unsubscribe process is fail with Huawei Services " + e.getMessage());
                        }
                    });
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getLastKnownLocation(final LocationListener locationListener) {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            logError("Location is null");
                            return;
                        }
                        try {
                            locationListener.onLocationUpdate(location);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        logError("getLastKnownLocation process is fail with Huawei Services " + e.getMessage());
                    }
                });
    }
}
