package com.huawei.probation.packagedeliveryapp.customview.mapview;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.customview.mapview.factory.MapFactory;
import com.huawei.probation.packagedeliveryapp.customview.mapview.factory.Maps;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnMapAsyncListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnMapMarkerClickListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnMapReadyListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnPathsReadyListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnPolylineClickListener;
import com.huawei.probation.packagedeliveryapp.network.model.Paths;
import com.huawei.probation.packagedeliveryapp.util.CheckServicesAvailable;
import com.huawei.probation.packagedeliveryapp.util.CustomInfoWindowAdapter;

import java.io.UnsupportedEncodingException;

public class HuaweiGoogleMapView extends RelativeLayout implements OnMapReadyListener {

    Context context;
    Maps myMap;

    OnMapAsyncListener onMapAsyncListener;

    public HuaweiGoogleMapView(Context context) {
        super(context);
        this.context = context;
        inflateLayout();
    }

    public HuaweiGoogleMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflateLayout();
    }

    private void inflateLayout() {
        inflate(context, R.layout.huawei_google_map_view, this);
        RelativeLayout rootView = findViewById(R.id.rl_root_huawei_google_map_view);

        myMap = MapFactory.createAndGetMap(getContext(), CheckServicesAvailable.getAvailableService(context));
        myMap.getMapView().setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        rootView.addView(myMap.getMapView());
    }

    public void onCreate(Bundle bundle) {
        myMap.onCreate(bundle);
    }

    public void getMapAsync(OnMapAsyncListener onMapAsyncListener) {
        this.onMapAsyncListener = onMapAsyncListener;
        myMap.getMapAsync(this);
    }

    public void addMarker(String title, String snippet, Float latitude, Float longitude, int drawableResource, float anchorMarker) {
        myMap.addMarker(title, snippet, latitude, longitude, drawableResource, anchorMarker);
    }

    public void setOnInfoWindowClickListener(OnMapMarkerClickListener onMapMarkerClickListener) {
        myMap.setOnInfoWindowClickListener(onMapMarkerClickListener);
    }

    public void moveCamera(Float latitude, Float longitude, Float zoomRatio) {
        myMap.moveCamera(latitude, longitude, zoomRatio);
    }

    public void animateCamera(Float latitude, Float longitude, Float zoomRatio) {
        myMap.animateCamera(latitude, longitude, zoomRatio);
    }

    public void setMyLocationEnabled(Boolean myLocationEnabled) {
        myMap.setMyLocationEnabled(myLocationEnabled);
    }

    public void drawCircle(double lat, double lon, double radius, int color){
        myMap.drawCircle(lat, lon, radius, color);
    }

    public void drawPolyline(){
        myMap.drawPolyline();
    }

    public void drawDronePolyline(double latDevice, double lonDevice, double lat, double lon, int color, float width){
        myMap.drawDronePolyline(latDevice, lonDevice, lat, lon, color, width);
    }

    public void drawCourierPolyline(Paths paths, double latDevice, double lonDevice, double lat, double lon, int color, float width, String markerId){
        myMap.drawCourierPolyline(paths, latDevice, lonDevice, lat, lon, color, width, markerId);
    }

    public void clear() {
        myMap.clear();
    }

    public Parcelable onSaveInstanceState(Bundle bundle) {
        myMap.onSaveInstanceState(bundle);
        return super.onSaveInstanceState();
    }

    public void onStart() {
        myMap.onStart();
    }

    public void onStop() {
        myMap.onStop();
    }


    public void onPause() {
        myMap.onPause();
    }

    public void onResume() {
        myMap.onResume();
    }

    public void onDestroy() {
        myMap.onDestroy();
    }

    public void onLowMemory() {
        myMap.onLowMemory();
    }

    @Override
    public void onMapReady() {
        onMapAsyncListener.onMapReady(this);

    }

    public void setCustomInfoWindowAdapter(CustomInfoWindowAdapter customInfoWindowAdapter) {
        myMap.setCustomInfoWindowAdapter(customInfoWindowAdapter);
    }

    public void setMinZoomPreference(float minZoom) {
        myMap.setMinZoomPreference(minZoom);
    }

    public void setMarkerClickListener(OnMapMarkerClickListener onMapMarkerClickListener) {
        myMap.setMarkerClickListener(onMapMarkerClickListener);
    }

    public void cleanMapPolylines() {
        myMap.cleanMapPolylines();
    }

    public void setPolylineClickListener(OnPolylineClickListener onPolylineClickListener) {
        myMap.setPolylineClickListener(onPolylineClickListener);
    }

    public double calculationByDistance(double latitude, double longitude, double latitude1, double longitude1) {
        return myMap.calculationByDistance(latitude,longitude,latitude1,longitude1);
    }

    public void clearSiteMarkers() {
        myMap.clearSiteMarkers();
    }

    public void searchNearBakeries(double lat, double lon, int circleRadius) throws UnsupportedEncodingException {
        myMap.searchNearBakeries(lat,lon, circleRadius);
    }

    public void setPathsReadyListener(OnPathsReadyListener onPathsReadyListener) {
        myMap.setPathsReadyListener(onPathsReadyListener);
    }

    public void getDirections(double latdevice, double lonDevice, double lat, double lon) {
        myMap.getDirections(latdevice,lonDevice,lat,lon);
    }
}
