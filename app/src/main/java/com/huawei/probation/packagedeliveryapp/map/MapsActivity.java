package com.huawei.probation.packagedeliveryapp.map;

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
import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.Circle;
import com.huawei.hms.maps.model.CircleOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.model.Polyline;
import com.huawei.hms.maps.model.PolylineOptions;
import com.huawei.hms.maps.util.LogM;
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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Queue;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * map activity entrance class
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, HuaweiMap.OnMyLocationButtonClickListener {

    private static final String TAG = "MapViewDemoActivity";

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private static final int REQUEST_CODE = 101;
    private static final float DEFAULT_ZOOM = 9f;
    private static final float MIN_ZOOM = 4f;
    private static final int REQUEST_FREQUENCY_FACTOR = 100;

//    private static final LatLng LAT_LNG = new LatLng(31.2304, 121.4737);
    private static final LatLng LAT_LNG = new LatLng(60, 60);

    Button button;

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

    private static final String[] RUNTIME_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};
    private Polyline mPolyline;

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

        setUpMap(savedInstanceState);


//        mSettingsClient = LocationServices.getSettingsClient(this);


//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//            Log.i(TAG, "sdk < 28 Q");
//            if (ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                String[] strings =
//                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//                ActivityCompat.requestPermissions(this, strings, 1);
//            }
//        } else {
//            if (ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.checkSelfPermission(this,
//                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
//                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
//                ActivityCompat.requestPermissions(this, strings, 2);
//            }
//        }
//
//        if (!hasPermissions(this, RUNTIME_PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE);
//        }


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

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        new GpsUtils(this).turnGPSOn(isGPSEnable -> isGPS = isGPSEnable);

        //I checked isGPS in these methods separately for multiple usage
        //For the fast initialization of the map
        getLastLocation();

        initLocationRequest();

        initLocationCallback();

        init();


//        // mark can be add by HuaweiMap
//        mMarker = hmap.addMarker(new MarkerOptions().position(LAT_LNG)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone_delivery_icon))
//                .clusterable(true));
//
//        mMarker.showInfoWindow();
//
//        // circle can be add by HuaweiMap
//        mCircle = hmap.addCircle(new CircleOptions().center(new LatLng(60, 60)).radius(10000).fillColor(Color.GREEN));


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
                    addMarker();
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
//                    Task<Location> task = mFusedLocationProviderClient.getLastLocation();
//                    task.addOnSuccessListener(location -> {
//                        if (location != null /*&& ((location.getAccuracy() > 0 && location.getAccuracy() < 20) || (location.getTime() - System.currentTimeMillis() < 120000))*/) {
//                            mLocation = location;
//                            Toast.makeText(MapsActivity.this, mLocation.getLatitude() + ", " + mLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//                            startClustering(new LatLng(location.getLatitude(),location.getLongitude()), DEFAULT_ZOOM);
//                        }
//                    });
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
        if(isLocationPermissionGranted)
            getLastLocation();
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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
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

    public void addMarker() {
        if (null != mMarker) {
            mMarker.remove();
        }
        if (null != mPolyline) {
            mPolyline.remove();
            mPolyline = null;
        }
//        MarkerOptions options = new MarkerOptions()
//                .position(getLocationInLatLngRad(100.0,mLocation))
//                .title("Hello Huawei Map")
//                .snippet("This is a snippet!");
//        mMarker = hmap.addMarker(options);

        LatLng latLngDevice = getLocationInLatLngRad(20000.0,mLocation);

        // mark can be add by HuaweiMap
        mMarker = hmap.addMarker(new MarkerOptions().position(latLngDevice)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone))
                .title("BANDO")
                .snippet("I am coming!")
                .anchorMarker(0.9f,0.9f));

//        mMarker.showInfoWindow();

        // circle can be add by HuaweiMap
//        mCircle = hmap.addCircle(new CircleOptions().center(new LatLng(60, 60)).radius(10000).fillColor(Color.GREEN));
        mCircle = hmap.addCircle(new CircleOptions().center(new LatLng(mLocation.getLatitude(),mLocation.getLongitude())).radius(20000).fillColor(Color.TRANSPARENT));

        //Requests for directions and then draws the polyline
        getDirections(latLngDevice.latitude,latLngDevice.longitude,mLocation.getLatitude(),mLocation.getLongitude());

//        mPolyline = hmap.addPolyline(new PolylineOptions()
//                .add(latLngDevice, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
//                .color(Color.BLUE)
//                .width(3));

    }

    private void getDirections(double latitude, double longitude, double latitude1, double longitude1) {

        DirectionsRequest directionRequest = new DirectionsRequest(new LatLngData(latitude, longitude), new LatLngData(latitude1, longitude1));
        DirectionsBaseRepo.getInstance().getDirectionsWithType(DirectionType.BICYCLING.getDirectionString(),directionRequest).enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if(response.body() != null && !response.body().getRoutes().isEmpty() && response.isSuccessful())
                    addPolyLines(response.body().getRoutes().get(0).getPaths().get(0));
                else {
                    Toast.makeText(MapsActivity.this, "No Routes", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addPolyLines(Paths path){
        PolylineOptions options = new PolylineOptions();
        options.add(new LatLng(path.getStartLocation().getLat(), path.getStartLocation().getLng()));
        for(Steps s : path.getSteps()){
            for(LatLngData l : s.getPolyline()){
                options.add(new LatLng(l.getLat(),l.getLng()));
            }
        }
        options.add(new LatLng(path.getEndLocation().getLat(), path.getEndLocation().getLng()));
        options.color(Color.BLUE);
        options.width(10f);
        hmap.addPolyline(options);
    }
}
