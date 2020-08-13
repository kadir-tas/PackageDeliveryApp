package com.huawei.probation.packagedeliveryapp.manager.location;

import android.app.Activity;

import com.huawei.probation.packagedeliveryapp.manager.location.factory.LocationFactory;
import com.huawei.probation.packagedeliveryapp.manager.location.factory.Locations;
import com.huawei.probation.packagedeliveryapp.manager.location.listener.LocationListener;
import com.huawei.probation.packagedeliveryapp.util.CheckServicesAvailable;

public class HuaweiGoogleLocationManager {
    private Activity activity;
    private Locations locationService;

    public HuaweiGoogleLocationManager(Activity activity) {
        this.activity = activity;
        locationService = LocationFactory.getLocationService(activity, CheckServicesAvailable.getAvailableService(activity));
    }

    public void subscribewLocationUpdates(LocationListener locationListener) {
        locationService.subscribewLocationUpdates(locationListener);
    }

    public void unsubscribeLocationUpdates() {
        locationService.unsubscribeLocationUpdates();
    }

    public void getLastKnownLocation(LocationListener locationListener) {
        locationService.getLastKnownLocation(locationListener);
    }
}
