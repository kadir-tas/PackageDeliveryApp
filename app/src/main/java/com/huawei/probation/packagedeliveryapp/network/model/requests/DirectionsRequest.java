package com.huawei.probation.packagedeliveryapp.network.model.requests;

import com.google.gson.annotations.SerializedName;
import com.huawei.probation.packagedeliveryapp.network.model.LatLngData;

public class DirectionsRequest {
    @SerializedName("origin")
    private LatLngData origin;

    @SerializedName("destination")
    private LatLngData destination;

    public DirectionsRequest(LatLngData origin, LatLngData destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public LatLngData getOrigin() {
        return origin;
    }

    public void setOrigin(LatLngData origin) {
        this.origin = origin;
    }

    public LatLngData getDestination() {
        return destination;
    }

    public void setDestination(LatLngData destination) {
        this.destination = destination;
    }
}
