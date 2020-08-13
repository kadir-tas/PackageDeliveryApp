package com.huawei.probation.packagedeliveryapp.manager.location.listener;

import android.location.Location;

import java.io.UnsupportedEncodingException;

public interface LocationListener {
    void onLocationUpdate(Location location) throws UnsupportedEncodingException;
}
