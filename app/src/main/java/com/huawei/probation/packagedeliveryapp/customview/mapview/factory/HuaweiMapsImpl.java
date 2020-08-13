package com.huawei.probation.packagedeliveryapp.customview.mapview.factory;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CircleOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.model.Polyline;
import com.huawei.hms.maps.model.PolylineOptions;
import com.huawei.hms.site.api.SearchResultListener;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.SearchServiceFactory;
import com.huawei.hms.site.api.model.Coordinate;
import com.huawei.hms.site.api.model.LocationType;
import com.huawei.hms.site.api.model.NearbySearchRequest;
import com.huawei.hms.site.api.model.NearbySearchResponse;
import com.huawei.hms.site.api.model.SearchStatus;
import com.huawei.hms.site.api.model.Site;
import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnMapMarkerClickListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnMapReadyListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnPathsReadyListener;
import com.huawei.probation.packagedeliveryapp.customview.mapview.listener.OnPolylineClickListener;
import com.huawei.probation.packagedeliveryapp.network.model.LatLngData;
import com.huawei.probation.packagedeliveryapp.network.model.Paths;
import com.huawei.probation.packagedeliveryapp.network.model.Steps;
import com.huawei.probation.packagedeliveryapp.network.model.requests.DirectionsRequest;
import com.huawei.probation.packagedeliveryapp.network.model.responses.DirectionsResponse;
import com.huawei.probation.packagedeliveryapp.network.repo.DirectionsBaseRepo;
import com.huawei.probation.packagedeliveryapp.util.CustomInfoWindowAdapter;
import com.huawei.probation.packagedeliveryapp.util.DirectionType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.huawei.probation.packagedeliveryapp.map.MapsActivity.isTrackOrder;

public class HuaweiMapsImpl extends BaseMaps implements OnMapReadyCallback, HuaweiMap.OnMyLocationButtonClickListener, HuaweiMap.OnPolylineClickListener, HuaweiMap.OnMarkerClickListener  {
    private MapView mapView;

    private HuaweiMap map;
    private OnMapReadyListener onMapReadyListener;
    private Context context;

    private Polyline mPolylineDrone;
    private Polyline mPolylineNormal;

    private Marker mMarker;
    private OnMapMarkerClickListener onMapMarkerClickListener;
    private OnPolylineClickListener onPolylineClickListener;
    private List<Site> siteBakeryList;
    private List<Marker> mSiteMarkerList = new ArrayList<>();
    private OnPathsReadyListener onPathsReadyListener;

    HuaweiMapsImpl(Context context) {
        super(context);
        this.context = context;
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
        this.onMapReadyListener = onMapReadyListener;
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(HuaweiMap huaweiMap) {
        if (map != null) {
            return;
        }
        map = huaweiMap;
        map.setOnPolylineClickListener(this);
        map.setOnMarkerClickListener(this);
        this.onMapReadyListener.onMapReady();
    }

    @Override
    public void addMarker(String title, String snippet, Float latitude, Float longitude, int drawableResource, float anchorMarker) {
        super.addMarker(title, snippet, latitude, longitude,drawableResource,anchorMarker);
        LatLng nyHuawei = new com.huawei.hms.maps.model.LatLng(latitude, longitude);
        MarkerOptions markerOptionsHuawei = new MarkerOptions().position(nyHuawei);
        if (title != null && !title.isEmpty()) markerOptionsHuawei.title(title);
        if (snippet != null && !snippet.isEmpty()) markerOptionsHuawei.snippet(snippet);
        if (drawableResource != 0) markerOptionsHuawei.icon(BitmapDescriptorFactory.fromResource(drawableResource));
        if (anchorMarker != 0) markerOptionsHuawei.anchorMarker(anchorMarker,anchorMarker);
        mMarker = map.addMarker(markerOptionsHuawei);
    }

    @Override
    public void setCustomInfoWindowAdapter(CustomInfoWindowAdapter customInfoWindowAdapter) {
        map.setInfoWindowAdapter(customInfoWindowAdapter);
    }

    @Override
    public void setOnInfoWindowClickListener(final OnMapMarkerClickListener onMapMarkerClickListener) {
        map.setOnInfoWindowClickListener(marker -> { });
    }

    @Override
    public void setMinZoomPreference(float minZoom) {
        map.setMinZoomPreference(minZoom);
    }

    @Override
    public void moveCamera(Float latitude, Float longitude, Float zoomRatio) {
        super.moveCamera(latitude, longitude, zoomRatio);
        LatLng nyHuawei = new com.huawei.hms.maps.model.LatLng(latitude, longitude);
        map.moveCamera(com.huawei.hms.maps.CameraUpdateFactory.newLatLngZoom(nyHuawei, zoomRatio));
    }

    @Override
    public void animateCamera(Float latitude, Float longitude, Float zoomRatio) {
        super.animateCamera(latitude, longitude, zoomRatio);
        LatLng nyHuawei = new com.huawei.hms.maps.model.LatLng(latitude, longitude);
        map.animateCamera(com.huawei.hms.maps.CameraUpdateFactory.newLatLngZoom(nyHuawei, zoomRatio));
    }

    @Override
    public void setMyLocationEnabled(Boolean myLocationEnabled) {
        map.setMyLocationEnabled(myLocationEnabled);
    }

    @Override
    public void drawCircle(double lat,double lon,double radius, int color) {
        map.addCircle(new CircleOptions().center(new LatLng(lat, lon)).radius(radius).fillColor(color));
    }

    @Override
    public void drawPolyline() {

    }

    @Override
    public void drawDronePolyline(double latDevice, double lonDevice, double lat, double lon, int color, float width) {

        LatLng latLngDevice = new LatLng(latDevice,lonDevice);
        LatLng latLng = new LatLng(lat,lon);
//        if(mLatLngDevice == null){
//            String json = sharedPref.getString("latlongDevice", "");
//            mLatLngDevice = gson.fromJson(json, LatLng.class);
//
//        }else{
//            editor = sharedPref.edit();
//            String json = gson.toJson(mLatLngDevice);
//            editor.putString("latlongDevice", json);
//            editor.apply();
//        }
        PolylineOptions polylineOptionsDrone = new PolylineOptions();
        polylineOptionsDrone.add(latLngDevice, latLng)
                .color(color)
                .width(width);

        mPolylineDrone = map.addPolyline(polylineOptionsDrone);
        if(!isTrackOrder) mPolylineDrone.setClickable(true);
    }

    @Override
    public void drawCourierPolyline(Paths paths, double latDevice, double lonDevice, double lat, double lon, int color, float width, String markerId) {
        PolylineOptions polylineOptionsNormal = new PolylineOptions();
        polylineOptionsNormal.add(new LatLng(paths.getStartLocation().getLat(), paths.getStartLocation().getLng()));

        for (Steps s : paths.getSteps()) {
            for (LatLngData l : s.getPolyline()) {
                polylineOptionsNormal.add(new LatLng(l.getLat(), l.getLng()));
            }
        }

        polylineOptionsNormal.add(new LatLng(paths.getEndLocation().getLat(), paths.getEndLocation().getLng()))
                .color(color)
                .width(width);

        mPolylineNormal = map.addPolyline(polylineOptionsNormal);
        if (!isTrackOrder) mPolylineNormal.setClickable(true);
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
    public double calculationByDistance(double latStart, double lonStart, double latEnd, double lonEnd) {
        int Radius = 6371;// radius of earth in Km
        LatLng StartP = new LatLng(latStart,lonStart);
        LatLng EndP = new LatLng(latEnd,lonEnd);
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    @Override
    public void cleanMapPolylines() {
        if (mPolylineNormal != null) {
            mPolylineNormal.remove();
            mPolylineNormal = null;
        }
        if (mPolylineDrone != null) {
            mPolylineDrone.remove();
            mPolylineDrone = null;
        }

    }

    @Override
    public void clearSiteMarkers() {
        for (Marker m : mSiteMarkerList) {
            m.remove();
        }
        mSiteMarkerList.clear();
    }

    @Override
    public void searchNearBakeries(double lat, double lon, int circleRadius) throws UnsupportedEncodingException {
            NearbySearchRequest request = new NearbySearchRequest();
            request.setPoiType(LocationType.BAKERY);
            request.setLocation(new Coordinate(lat, lon));
            request.setRadius(circleRadius);  // meter
            request.setPageIndex(1);

            SearchService searchService = SearchServiceFactory.create(context,
                    URLEncoder.encode(
                            "CgB6e3x9917QHuu3H/bRUeqZUAGwEJYp1p7NIlhcWubs7Bc6FG7PrnDCWUl+JAw/u91OtDmm6iAnhy6RAvm62JaD",
                            "utf-8"));

            searchService.nearbySearch(request, new SearchResultListener<NearbySearchResponse>() {
                @Override
                public void onSearchResult(NearbySearchResponse nearbySearchResponse) {
                    siteBakeryList = nearbySearchResponse.getSites();
                    addMarkersForNearbyLocations(siteBakeryList);
                }

                @Override
                public void onSearchError(SearchStatus searchStatus) {

                }
            });
    }

    private void addMarkersForNearbyLocations(List<Site> siteBakeryList) {
        clearSiteMarkers();
        for (Site s : siteBakeryList) {
            mSiteMarkerList.add(map.addMarker(new MarkerOptions().position(new LatLng(s.getLocation().getLat(), s.getLocation().getLng()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.baked_goods))
                    .title(s.getName())
                    .snippet(Double.toString(s.getDistance()))
                    .anchorMarker(0.9f, 0.9f)));
        }
    }

    @Override
    public void getDirections(double latitude, double longitude, double latitude1, double longitude1) {

        DirectionsRequest directionRequest = new DirectionsRequest(new LatLngData(latitude, longitude), new LatLngData(latitude1, longitude1));
        DirectionsBaseRepo.getInstance().getDirectionsWithType(DirectionType.BICYCLING.getDirectionString(), directionRequest).enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() != null && !response.body().getRoutes().isEmpty() && response.isSuccessful()) {
                    onPathsReadyListener.onPathsReady(response.body().getRoutes().get(0).getPaths().get(0));
//                    drawCourierPolyline();
                } else {
                    Toast.makeText(context, "No Routes", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(context, "Something went wrong " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void setMarkerClickListener(OnMapMarkerClickListener onMapMarkerClickListener) {
        this.onMapMarkerClickListener = onMapMarkerClickListener;
    }

    @Override
    public void setPolylineClickListener(OnPolylineClickListener onPolylineClickListener) {
        this.onPolylineClickListener = onPolylineClickListener;
    }

    @Override
    public void setPathsReadyListener(OnPathsReadyListener onPathsReadyListener) {
        this.onPathsReadyListener = onPathsReadyListener;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        onMapMarkerClickListener.onMarkerClick(marker.getPosition().latitude, marker.getPosition().longitude,marker.getId());
        return true;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        //Color management: first all blue than setting the clicked polyline color to gray
        mPolylineDrone.setColor(Color.BLUE);
        mPolylineDrone.setZIndex(1f);

        mPolylineNormal.setColor(Color.BLUE);
        mPolylineNormal.setZIndex(1f);

        polyline.setColor(Color.GRAY);
        polyline.setZIndex(2f);

        if (mMarker != null) {
            mMarker.remove();
        }
        onPolylineClickListener.onPolylineClick(polyline.getId(),mPolylineNormal.getId());
    }
}
