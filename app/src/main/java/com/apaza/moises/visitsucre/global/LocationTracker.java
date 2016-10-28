package com.apaza.moises.visitsucre.global;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.apaza.moises.visitsucre.ui.base.BaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class LocationTracker implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private final static String TAG = "LOCATION TRACKER";

    private BaseActivity baseActivity;

    /*LOCATION API*/
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location lastLocation;

    /*CODE REQUEST*/
    public static final int REQUEST_LOCATION = 1;
    public static final int REQUEST_CHECK_SETTINGS = 2;

    private static LocationTracker locationTracker;

    private LocationTracker(BaseActivity baseActivity){
        this.baseActivity = baseActivity;
        buildGoogleApiClient();
        createLocationRequest();
        buildRequestSettingsLocation();
        checkLocationSettings();
    }

    public static LocationTracker getInstance(BaseActivity baseActivity){
        if(locationTracker == null)
            locationTracker = new LocationTracker(baseActivity);
        return locationTracker;
    }

    public void connect(){
        if (googleApiClient.isConnected())
            startLocationUpdates();
    }

    public void disconnect() {
        if (googleApiClient.isConnected())
            stopLocationUpdates();
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(baseActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .enableAutoManage(baseActivity, this)
                .build();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest()
                .setInterval(Utils.UPDATE_INTERVAL)
                .setFastestInterval(Utils.UPDATE_FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildRequestSettingsLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .setAlwaysShow(true);
        locationSettingsRequest = builder.build();
    }

    private void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, locationSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d(TAG, "Los ajustes de ubicación satisfacen la configuración.");
                        //startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.d(TAG, "Los ajustes de ubicación no satisfacen la configuración. " +
                                    "Se mostrará un diálogo de ayuda.");
                            status.startResolutionForResult(baseActivity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.d(TAG, "El Intent del diálogo no funcionó.");
                            // Sin operaciones
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG, "Los ajustes de ubicación no son apropiados.");
                        break;

                }
            }
        });
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private Location getLastLocation() {
        if (ActivityCompat.checkSelfPermission(baseActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(baseActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            manageDeniedPermission();
            return null;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        return lastLocation;
    }

    private void processLastLocation() {
        getLastLocation();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(baseActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(baseActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            manageDeniedPermission();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void manageDeniedPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(baseActivity, Manifest.permission.ACCESS_FINE_LOCATION)){
            Global.showToastMessage("Required permissions");
        }else {
            ActivityCompat.requestPermissions(baseActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    /*GOOGLE CLIENT API - CONNECTION CALL BACKS*/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        processLastLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspend");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Global.showToastMessage("Connection error code: " + connectionResult.getErrorCode());
    }

    /*GOOGLE CLIENT API - LOCATION LISTENER*/
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, String.format("New location: (%s, %s)",
                location.getLatitude(), location.getLongitude()));
        lastLocation = location;
    }

    /*RESULT CALL BACK*/
    @Override
    public void onResult(@NonNull Status status) {
        if(status.isSuccess()){
            Log.d(TAG, "Detect activity started");
        }else {
            Log.d(TAG, "Error in start/remove detect activity " + status.getStatusMessage());
        }
    }
}
