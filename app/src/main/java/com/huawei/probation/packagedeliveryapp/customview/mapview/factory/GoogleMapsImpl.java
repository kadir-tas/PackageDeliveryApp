package com.huawei.probation.packagedeliveryapp.customview.mapview.factory;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnMapMarkerClickListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnMapReadyListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnPathsReadyListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnPolylineClickListener;
import com.huawei.probation.packagedeliveryapp.network.model.Paths;
import com.huawei.probation.packagedeliveryapp.util.CustomInfoWindowAdapter;

import java.io.UnsupportedEncodingException;

public class GoogleMapsImpl extends BaseMaps implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private OnMapReadyListener myOnMapAsyncListener;

    GoogleMapsImpl(Context context) {
        super(context);
        mapView = new MapView(context);
    }


    @Override
    public View getMapView() {
        return mapView;
    }

    @Override
    public void onCreate(Bundle bundle) {
        mapView.onCreate(bundle);
    }

    @Override
    public void getMapAsync(OnMapReadyListener onMapReadyListener) {
        this.myOnMapAsyncListener = onMapReadyListener;
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        this.myOnMapAsyncListener.onMapReady();
    }

    @Override
    public void setOnInfoWindowClickListener(final OnMapMarkerClickListener onMapMarkerClickListener) {
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
            }
        });
    }

    @Override
    public void moveCamera(Float latitude, Float longitude, Float zoomRatio) {
        super.moveCamera(latitude, longitude, zoomRatio);
        LatLng nyGoogle = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(nyGoogle, zoomRatio));
    }

    @Override
    public void animateCamera(Float latitude, Float longitude, Float zoomRatio) {
        super.animateCamera(latitude, longitude, zoomRatio);
        LatLng nyGoogle = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(nyGoogle, zoomRatio));
    }

    @Override
    public void setMyLocationEnabled(Boolean myLocationEnabled) {
    }

    @Override
    public void drawCircle(double lat, double lon, double radius, int color) {

    }



    @Override
    public void drawPolyline() {

    }

    @Override
    public void drawDronePolyline(double latDevice, double lonDevice, double lat, double lon, int color, float width) {

    }

    @Override
    public void drawCourierPolyline(Paths paths, double latDevice, double lonDevice, double lat, double lon, int color, float width, String markerId) {

    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        mapView.onSaveInstanceState(bundle);
    }

    @Override
    public void onStart() {
        mapView.onStart();
    }

    @Override
    public void onResume() {
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
    }

    @Override
    public void onStop() {
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
    }

    @Override
    public void setCustomInfoWindowAdapter(CustomInfoWindowAdapter customInfoWindowAdapter) {

    }

    @Override
    public void setMinZoomPreference(float minZoom) {

    }

    @Override
    public void setMarkerClickListener(OnMapMarkerClickListener onMapMarkerClickListener) {

    }

    @Override
    public void setPolylineClickListener(OnPolylineClickListener onPolylineClickListener) {

    }

    @Override
    public void cleanMapPolylines() {

    }

    @Override
    public double calculationByDistance(double latStart, double lonStart, double latEnd, double lonEnd) {
        return 0;
    }

    @Override
    public void clearSiteMarkers() {

    }

    @Override
    public void searchNearBakeries(double lat, double lon, int circleRadius) throws UnsupportedEncodingException {

    }

    @Override
    public void setPathsReadyListener(OnPathsReadyListener onPathsReadyListener) {

    }

    @Override
    public void getDirections(double latdevice, double lonDevice, double lat, double lon) {

    }
}
