package com.huawei.probation.packagedeliveryapp.map;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.SettingsClient;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.Circle;
import com.huawei.hms.maps.model.CircleOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.model.Polyline;
import com.huawei.hms.maps.model.PolylineOptions;
import com.huawei.hms.maps.util.LogM;
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
import com.huawei.probation.packagedeliveryapp.network.model.LatLngData;
import com.huawei.probation.packagedeliveryapp.network.model.Paths;
import com.huawei.probation.packagedeliveryapp.network.model.Steps;
import com.huawei.probation.packagedeliveryapp.network.model.requests.DirectionsRequest;
import com.huawei.probation.packagedeliveryapp.network.model.responses.DirectionsResponse;
import com.huawei.probation.packagedeliveryapp.network.repo.DirectionsBaseRepo;
import com.huawei.probation.packagedeliveryapp.util.AppConstants;
import com.huawei.probation.packagedeliveryapp.util.CustomInfoWindowAdapter;
import com.huawei.probation.packagedeliveryapp.util.DirectionType;
import com.huawei.probation.packagedeliveryapp.util.GpsUtils;
import com.huawei.probation.packagedeliveryapp.util.receiver.GPSBroadcastReceiver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * map activity entrance class
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, HuaweiMap.OnMyLocationButtonClickListener, HuaweiMap.OnPolylineClickListener, HuaweiMap.OnMarkerClickListener {

    private static final String TAG = "MapViewDemoActivity";

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static boolean isDrone = false;
    public static boolean isTrackOrder = false;

    private static final int REQUEST_CODE = 101;
    private static final float DEFAULT_ZOOM = 9f;
    private static final float MIN_ZOOM = 4f;
    private static final int REQUEST_FREQUENCY_FACTOR = 100;

    private static final double CIRCLE_RADIUS = 20000;

    private HuaweiMap hmap;

    private MapView mMapView;

    private Marker mMarker;

    private Circle mCircle;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private Location mLocation;
    private LocationSettingsRequest.Builder builder;

    private boolean isGPS = false;
    private boolean mIsRestore;
    private boolean isLocationPermissionGranted;

    private SettingsClient mSettingsClient;
    private GPSBroadcastReceiver mGpsBroadcastReceiver;

    private String mDurationRoundedNormal;
    private String mDistanceRoundedNormal;
    private String mDurationRoundedDrone;
    private String mDistanceRoundedDrone;

    private LatLng mLatLngDevice;

    private List<Site> siteBakeryList;

    private static final String[] RUNTIME_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};
    private Polyline mPolylineNormal;
    private Polyline mPolylineDrone;
    private List<Marker> mSiteMarkerList = new ArrayList<>();
    private Paths mPath;

    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogM.d(TAG, "map onCreate:");

        setContentView(R.layout.activity_maps);
        mIsRestore = savedInstanceState != null;
        mGpsBroadcastReceiver = new GPSBroadcastReceiver();
        if (!mGpsBroadcastReceiver.isOrderedBroadcast()) {
            registerReceiver(mGpsBroadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        }

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        setUpMap(savedInstanceState);

    }

    private void setUpMap(Bundle savedInstanceState) {
        //Calls the below onMapReady function to init location
        // get mapView by layout view
        mMapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(HuaweiMap map) {
        Log.d(TAG, "onMapReady: ");
        if (hmap != null) {
            return;
        }
        hmap = map;
        hmap.setMinZoomPreference(MIN_ZOOM);
        hmap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
        hmap.setOnPolylineClickListener(MapsActivity.this);
        hmap.setOnMarkerClickListener(MapsActivity.this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        new GpsUtils(this).turnGPSOn(isGPSEnable -> isGPS = isGPSEnable);

        //I checked isGPS in these methods separately for handling the different states. For example, user maybe disable the gps at any time.
        //For the fast initialization of the map
        getLastLocation();

        initLocationRequest();

        initLocationCallback();

        init();
    }

    private void init(){
        if(isLocationPermissionGranted) {

            builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            checkLocationSetting(builder);

            hmap.setMyLocationEnabled(true);
        }
    }

    private void checkLocationSetting(LocationSettingsRequest.Builder builder) {

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                requestLocation();
                return;
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
                    builder1.setTitle("Continious Location Request");
                    builder1.setMessage("This request is essential to get location update continiously");
                    builder1.create();
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            try {
                                resolvable.startResolutionForResult(MapsActivity.this,
                                        AppConstants.REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MapsActivity.this, R.string.toastMsgLocPermission, Toast.LENGTH_LONG).show();
                        }
                    });
                    builder1.show();
                }
            }
        });
    }

    private void initLocationRequest() {
        if(isGPS) {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(30000);
            mLocationRequest.setFastestInterval(10000);
            mLocationRequest.setSmallestDisplacement(30);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    private void initLocationCallback() {
        if(isGPS) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        Toast.makeText(MapsActivity.this, R.string.toastMsgLocPermission, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (locationResult.getLastLocation() != null) {
                        //This automatically saves to the last location I dont need to set mLocation but I set for possible features
                        mLocation = locationResult.getLastLocation();
                    } else {
                        Toast.makeText(MapsActivity.this, R.string.toastMsgLocPermission, Toast.LENGTH_SHORT).show();
                        new GpsUtils(MapsActivity.this).turnGPSOn(isGPSEnable -> isGPS = isGPSEnable);
                    }
                }
            };
        } else{
            Toast.makeText(MapsActivity.this, R.string.toastMsgTurnOnGps, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLastLocation() {
        if(isGPS) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.LOCATION_REQUEST);
                return;
            }
            isLocationPermissionGranted = true;
            Task<Location> task = mFusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null /* && ((location.getAccuracy() > 0 && location.getAccuracy() < 20) || (location.getTime() - System.currentTimeMillis() < 120000))*/) {
                    mLocation = location;
                    try {
                        //check if user comes from main activity for tracking the order then check placed order with drone or moto courier
                        if(isTrackOrder){
                            if(isDrone){
                                drawDronePolyline();
                            }
                            else{
                                drawCourierPolyline();
                            }
                            String title = sharedPref.getString("title", "");
                            int drawableResourceInt = sharedPref.getInt("drawableResourceInt", 0);
                            String json = sharedPref.getString("latlongDevice", "");
                            mLatLngDevice = gson.fromJson(json, LatLng.class);

                            hmap.addMarker(new MarkerOptions().position(mLatLngDevice)
                                    .icon(BitmapDescriptorFactory.fromResource(drawableResourceInt))
                                    .title(title)
                                    .snippet("I am coming!")
                                    .anchorMarker(0.9f,0.9f));
                            hmap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()) , 21.0f) );

                        }else{
                            drawCircle();
                            nearbySearch();
                        }
//                        addMarker();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MapsActivity.this, mLocation.getLatitude() + ", " + mLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    if (!mIsRestore) {
                        hmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), DEFAULT_ZOOM));
                    }
                }
            });
        }else {
            Toast.makeText(MapsActivity.this, R.string.toastMsgTurnOnGps, Toast.LENGTH_LONG).show();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.LOCATION_REQUEST);

        } else {
            if(mLocationCallback == null){
                initLocationCallback();
            }
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationPermissionGranted = true;
                    getLastLocation();
                    init();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    isLocationPermissionGranted = false;
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
        if (requestCode == AppConstants.REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                requestLocation();
            }
            else {
                checkLocationSetting(builder);
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void drawCircle() {
        mCircle = hmap.addCircle(new CircleOptions().center(new LatLng(mLocation.getLatitude(),mLocation.getLongitude())).radius(CIRCLE_RADIUS).fillColor(Color.TRANSPARENT));
    }

    private void getDirections(double latitude, double longitude, double latitude1, double longitude1) {

        DirectionsRequest directionRequest = new DirectionsRequest(new LatLngData(latitude, longitude), new LatLngData(latitude1, longitude1));
        DirectionsBaseRepo.getInstance().getDirectionsWithType(DirectionType.BICYCLING.getDirectionString(),directionRequest).enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if(response.body() != null && !response.body().getRoutes().isEmpty() && response.isSuccessful()) {
                    mPath = response.body().getRoutes().get(0).getPaths().get(0);
                    drawCourierPolyline();
                }else {
                    Toast.makeText(MapsActivity.this, "No Routes", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Something went wrong " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void drawDronePolyline(){
        double distanceDrone;
        double durationDrone;

        if(mLatLngDevice == null){
            String json = sharedPref.getString("latlongDevice", "");
            mLatLngDevice = gson.fromJson(json, LatLng.class);

        }else{
            editor = sharedPref.edit();
            String json = gson.toJson(mLatLngDevice);
            editor.putString("latlongDevice", json);
            editor.apply();
        }

        PolylineOptions polylineOptionsDrone = new PolylineOptions();

        distanceDrone = calculationByDistance(mLatLngDevice, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        durationDrone = (distanceDrone / 40) * 60; //minute conversion

        //init these variables to display in the bottom sheet dialog
        mDistanceRoundedDrone = new DecimalFormat("##.##").format(distanceDrone) + " km";
        mDurationRoundedDrone = new DecimalFormat("##.##").format(durationDrone) + " min";


        polylineOptionsDrone.add(mLatLngDevice, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                .color(Color.BLUE)
                .width(10f);

        mPolylineDrone = hmap.addPolyline(polylineOptionsDrone);
        if(!isTrackOrder) mPolylineDrone.setClickable(true);
    }

    void drawCourierPolyline(){
        double distanceNormal = 0;
        double durationNormal = 0;

        if(mPath == null){
            String json = sharedPref.getString("path", "");
            mPath = gson.fromJson(json, Paths.class);

        }else{
            editor = sharedPref.edit();
            String json = gson.toJson(mPath);
            editor.putString("path", json);
            editor.apply();
        }

        PolylineOptions polylineOptionsNormal = new PolylineOptions();
        polylineOptionsNormal.add(new LatLng(mPath.getStartLocation().getLat(), mPath.getStartLocation().getLng()));

        for(Steps s : mPath.getSteps()){
            for(LatLngData l : s.getPolyline()){
                polylineOptionsNormal.add(new LatLng(l.getLat(),l.getLng()));
            }
            distanceNormal += s.getDistance();
            durationNormal += s.getDuration();
        }

        distanceNormal /= 1000;
        durationNormal /= 60;

        //init these variables to display in the bottom sheet dialog
        mDistanceRoundedNormal = new DecimalFormat("##.##").format(distanceNormal) + " km";
        mDurationRoundedNormal = new DecimalFormat("##").format(durationNormal) + " min";

        polylineOptionsNormal.add(new LatLng(mPath.getEndLocation().getLat(), mPath.getEndLocation().getLng()))
                .color(Color.BLUE)
                .width(10f);

        mPolylineNormal = hmap.addPolyline(polylineOptionsNormal);
        if(!isTrackOrder) mPolylineNormal.setClickable(true);
    }

    private void cleanMapPolylines() {
        if(mPolylineNormal != null){
            mPolylineNormal.remove();
            mPolylineNormal = null;
        }
        if(mPolylineDrone != null){
            mPolylineDrone.remove();
            mPolylineDrone = null;
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_time_cost_display, null);

        BottomSheetDialog dialog = new BottomSheetDialog(MapsActivity.this);
        dialog.setContentView(dialogView);

        TextView timeText = dialogView.findViewById(R.id.timeText);
        TextView distanceText = dialogView.findViewById(R.id.distanceText);

        //Color management: first all blue than setting the clicked polyline color to gray
        mPolylineDrone.setColor(Color.BLUE);
        mPolylineDrone.setZIndex(1f);

        mPolylineNormal.setColor(Color.BLUE);
        mPolylineNormal.setZIndex(1f);

        polyline.setColor(Color.GRAY);
        polyline.setZIndex(2f);

        int drawableResourceInt;
        String titleText;

        if(polyline.getId().equals(mPolylineNormal.getId())){
            timeText.setText(mDurationRoundedNormal);
            distanceText.setText(mDistanceRoundedNormal);
            drawableResourceInt = R.drawable.motor_courier;
            titleText = "Courier";

        }else{
            timeText.setText(mDurationRoundedDrone);
            distanceText.setText(mDistanceRoundedDrone);
            drawableResourceInt = R.drawable.drone;
            titleText = "BANDO";
        }

        Button button = dialogView.findViewById(R.id.purchaseButton);
        button.setOnClickListener(v -> {
            //set flags for when order icon clicked from main activity and redirected to map activity
            // to tracking order if isDrone true it means that we should create just a drone way and no anymore request
            isTrackOrder = true;
            if(polyline.getId().equals(mPolylineNormal.getId())) {
                isDrone = false;
            }else if(polyline.getId().equals(mPolylineDrone.getId())){
                isDrone = true;
            }

            // marker for drone in the boundary of circle
            if(mMarker != null){
                mMarker.remove();
            }
            mMarker = hmap.addMarker(new MarkerOptions().position(mLatLngDevice)
                    .icon(BitmapDescriptorFactory.fromResource(drawableResourceInt))
                    .title(titleText)
                    .snippet("I am coming!")
                    .anchorMarker(0.9f,0.9f));

            editor = sharedPref.edit();
            editor.putString("title", titleText);
            editor.putInt("drawableResourceInt", drawableResourceInt);
            editor.apply();

            clearSiteMarkers();
            dialog.cancel();
        });

        dialog.show();
    }

    private void nearbySearch() throws UnsupportedEncodingException {
        NearbySearchRequest request = new NearbySearchRequest();
        request.setPoiType(LocationType.BAKERY);
        request.setLocation(new Coordinate(mLocation.getLatitude(), mLocation.getLongitude()));
        request.setRadius((int) CIRCLE_RADIUS);  // meter
        request.setPageIndex(1);

        SearchService searchService = SearchServiceFactory.create(MapsActivity.this,
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
        if(!mSiteMarkerList.isEmpty()){
            clearSiteMarkers();
        }
        for(Site s : siteBakeryList){
            mSiteMarkerList.add(hmap.addMarker(new MarkerOptions().position(new LatLng(s.getLocation().getLat(),s.getLocation().getLng()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.baked_goods))
                    .title(s.getName())
                    .snippet(Double.toString(s.getDistance()))
                    .anchorMarker(0.9f,0.9f)));
        }
    }

    private void clearSiteMarkers() {
        for(Marker m : mSiteMarkerList){
            m.remove();
        }
        mSiteMarkerList.clear();
    }

    protected static LatLng getLocationInLatLngRad(double radiusInMeters, Location currentLocation) {
        double x0 = currentLocation.getLongitude();
        double y0 = currentLocation.getLatitude();

        Random random = new Random();

        // Convert radius from meters to degrees.
        double radiusInDegrees = radiusInMeters / 111320f;

        // Get a random distance and a random angle.
        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        // Get the x and y delta values.
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Compensate the x value.
        double new_x = x / Math.cos(Math.toRadians(y0));

        double foundLatitude;
        double foundLongitude;

        foundLatitude = y0 + y;
        foundLongitude = x0 + new_x;

        return new LatLng(foundLatitude, foundLongitude);
    }

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
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
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if(!mGpsBroadcastReceiver.isOrderedBroadcast()){
            registerReceiver(mGpsBroadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        }
//        if(isLocationPermissionGranted)
//            getLastLocation();


//        if(!isGPS)
//            new GpsUtils(this).turnGPSOn(isGPSEnable -> isGPS = isGPSEnable);

//        getLastLocation();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
        if(mGpsBroadcastReceiver.isOrderedBroadcast()) {
            unregisterReceiver(mGpsBroadcastReceiver);
        }
        if(mFusedLocationProviderClient != null){
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        if(mFusedLocationProviderClient != null){
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
        unregisterReceiver(mGpsBroadcastReceiver);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mLatLngDevice = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

        //Clean map if there is any polyline
        cleanMapPolylines();

        //Draws drone route/straight polyline
        drawDronePolyline();

        //Requests for direction then draws courier route/polyline
        if(mPath == null || (mMarker != null && !mMarker.getId().equals(marker.getId()))) {
            getDirections(marker.getPosition().latitude, marker.getPosition().longitude, mLocation.getLatitude(), mLocation.getLongitude());
        }
        else{
            drawCourierPolyline();
        }
        return true;
    }
}
