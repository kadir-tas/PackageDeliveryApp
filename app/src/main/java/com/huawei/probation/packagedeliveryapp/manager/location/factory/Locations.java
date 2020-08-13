package com.huawei.probation.packagedeliveryapp.manager.location.factory;


import com.huawei.probation.packagedeliveryapp.manager.location.listener.LocationListener;

public interface Locations {
    void subscribewLocationUpdates(LocationListener locationListener);
    void unsubscribeLocationUpdates();
    void getLastKnownLocation(LocationListener locationListener);
}
