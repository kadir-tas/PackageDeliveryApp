package com.huawei.probation.packagedeliveryapp.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Paths {
    @SerializedName("duration")
    private Double duration;

    @SerializedName("durationText")
    private String durationText;

    @SerializedName("durationInTraffic")
    private Integer durationInTraffic;

    @SerializedName("distance")
    private Double distance;

    @SerializedName("startLocation")
    private LatLngData startLocation;

    @SerializedName("startAddress")
    private String startAddress;

    @SerializedName("distanceText")
    private String distanceText;

    @SerializedName("steps")
    private List<Steps> steps;

    @SerializedName("endLocation")
    private LatLngData endLocation;

    @SerializedName("endAddress")
    private String endAddress;

    public Paths(Double duration,
                 String durationText,
                 Integer durationInTraffic,
                 Double distance,
                 LatLngData startLocation,
                 String startAddress,
                 String distanceText,
                 List<Steps> steps,
                 LatLngData endLocation,
                 String endAddress) {

        this.duration = duration;
        this.durationText = durationText;
        this.durationInTraffic = durationInTraffic;
        this.distance = distance;
        this.startLocation = startLocation;
        this.startAddress = startAddress;
        this.distanceText = distanceText;
        this.steps = steps;
        this.endLocation = endLocation;
        this.endAddress = endAddress;

    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public Integer getDurationInTraffic() {
        return durationInTraffic;
    }

    public void setDurationInTraffic(Integer durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public LatLngData getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLngData startLocation) {
        this.startLocation = startLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public LatLngData getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLngData endLocation) {
        this.endLocation = endLocation;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }
}
