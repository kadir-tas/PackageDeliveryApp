package com.huawei.probation.packagedeliveryapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;

public class GpsUtils {
    private static final String TAG = "GpsUtils";
    private Context context;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationManager locationManager;
    private LocationRequest locationRequest;

    public GpsUtils(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mSettingsClient = LocationServices.getSettingsClient(context);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************
    }

    // method for turn on GPS
    public void turnGPSOn(onGpsListener onGpsListener) {

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (onGpsListener != null) {
                onGpsListener.gpsStatus(true);
            }
        } else {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener((Activity) context, locationSettingsResponse -> {

                        //  GPS is already enable, callback GPS status through listener
                        if (onGpsListener != null) {
                            onGpsListener.gpsStatus(true);
                        }
                    })
                    .addOnFailureListener((Activity) context, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult((Activity) context, AppConstants.GPS_REQUEST);
                                    } catch (IntentSender.SendIntentException sie) {
                                        Log.i(TAG, "PendingIntent unable to execute request.");
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    String errorMessage = "Location settings are inadequate, and cannot be " +
                                            "fixed here. Fix in Settings.";
                                    Log.e(TAG, errorMessage);

                                    Toast.makeText((Activity) context, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public interface onGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }
}