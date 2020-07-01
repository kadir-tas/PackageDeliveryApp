package com.huawei.probation.packagedeliveryapp.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Steps {
    @SerializedName("duration")
    private Double duration;

    @SerializedName("orientation")
    private Double orientation;

    @SerializedName("durationText")
    private String durationText;

    @SerializedName("distance")
    private Double distance;

    @SerializedName("startLocation")
    private LatLngData startLocation;

    @SerializedName("instruction")
    private String instruction;

    @SerializedName("action")
    private String action;

    @SerializedName("distanceText")
    private String distanceText;

    @SerializedName("endLocation")
    private LatLngData endLocation;

    @SerializedName("polyline")
    private List<LatLngData> polyline;

    @SerializedName("roadName")
    private String roadName;

    public Steps(Double duration, Double orientation,
                 String durationText,
                 Double distance,
                 LatLngData startLocation,
                 String instruction,
                 String action,
                 String distanceText,
                 LatLngData endLocation,
                 List<LatLngData> polyline,
                 String roadName) {

        this.duration = duration;
        this.orientation = orientation;
        this.durationText = durationText;
        this.distance = distance;
        this.startLocation = startLocation;
        this.instruction = instruction;
        this.action = action;
        this.distanceText = distanceText;
        this.endLocation = endLocation;
        this.polyline = polyline;
        this.roadName = roadName;

    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getOrientation() {
        return orientation;
    }

    public void setOrientation(Double orientation) {
        this.orientation = orientation;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public LatLngData getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLngData endLocation) {
        this.endLocation = endLocation;
    }

    public List<LatLngData> getPolyline() {
        return polyline;
    }

    public void setPolyline(List<LatLngData> polyline) {
        this.polyline = polyline;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
}
