package com.huawei.probation.packagedeliveryapp.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Routes {

    @SerializedName("paths")
    private List<Paths> paths;

    @SerializedName("bounds")
    private Bounds bounds;

    public Routes(List<Paths> paths, Bounds bounds) {
        this.paths = paths;
        this.bounds = bounds;
    }

    public List<Paths> getPaths() {
        return paths;
    }

    public void setPaths(List<Paths> paths) {
        this.paths = paths;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}
