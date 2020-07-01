package com.huawei.probation.packagedeliveryapp.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

public class GPSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            assert locationManager != null;
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //isGPSEnabled = true;
                Toast.makeText(context, "Gps turned ON", Toast.LENGTH_SHORT).show();


            } else {
                //isGPSEnabled = false;
                Toast.makeText(context, "Gps turned OFF", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
        }
    }
}