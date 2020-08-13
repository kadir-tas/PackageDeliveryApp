package com.huawei.probation.packagedeliveryapp.customview.mapview.factory;

import android.os.Bundle;
import android.view.View;

import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnMapMarkerClickListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnMapReadyListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnPathsReadyListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnPolylineClickListener;
import com.huawei.probation.packagedeliveryapp.network.model.Paths;
import com.huawei.probation.packagedeliveryapp.util.CustomInfoWindowAdapter;

import java.io.UnsupportedEncodingException;

public interface Maps {
    View getMapView();
    void onCreate(Bundle bundle);
    void getMapAsync(OnMapReadyListener onMapReadyListener);
    void addMarker(String title, String snippet, Float latitude, Float longitude, int drawableResource, float anchorMarker);
    void setOnInfoWindowClickListener(final OnMapMarkerClickListener onMapMarkerClickListener);
    void moveCamera(Float latitude, Float longitude, Float zoomRatio);
    void animateCamera(Float latitude, Float longitude, Float zoomRatio);
    void setMyLocationEnabled(Boolean myLocationEnabled);
    void drawCircle(double lat, double lon, double radius, int color);
    void drawPolyline();
    void drawDronePolyline(double latDevice, double lonDevice, double lat, double lon, int color, float width);
    void drawCourierPolyline(Paths paths, double latDevice, double lonDevice, double lat, double lon, int color, float width, String markerId);
    void clear();
    void onSaveInstanceState(Bundle bundle);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
    void onLowMemory();
    void setCustomInfoWindowAdapter(CustomInfoWindowAdapter customInfoWindowAdapter);
    void setMinZoomPreference(float minZoom);
    void setMarkerClickListener(OnMapMarkerClickListener onMapMarkerClickListener);
    void setPolylineClickListener(OnPolylineClickListener onPolylineClickListener);
    void cleanMapPolylines();
    double calculationByDistance(double latStart, double lonStart, double latEnd, double lonEnd);
    void clearSiteMarkers();
    void searchNearBakeries(double lat, double lon, int circleRadius) throws UnsupportedEncodingException;
    void setPathsReadyListener(OnPathsReadyListener onPathsReadyListener);
    void getDirections(double latdevice, double lonDevice, double lat, double lon);
}
