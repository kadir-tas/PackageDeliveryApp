package com.huawei.probation.packagedeliveryapp.network.model;

import com.google.gson.annotations.SerializedName;

public class Bounds {
    @SerializedName("southwest")
    private LatLngData southwest;

    @SerializedName("northeast")
    private LatLngData northeast;

    public Bounds(LatLngData southwest, LatLngData northeast) {
        this.southwest = southwest;
        this.northeast = northeast;
    }

    public LatLngData getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LatLngData southwest) {
        this.southwest = southwest;
    }

    public LatLngData getNortheast() {
        return northeast;
    }

    public void setNortheast(LatLngData northeast) {
        this.northeast = northeast;
    }
}
