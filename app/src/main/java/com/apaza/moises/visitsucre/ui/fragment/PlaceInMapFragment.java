package com.apaza.moises.visitsucre.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;
import com.apaza.moises.visitsucre.global.Global;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlaceInMapFragment extends BaseFragment implements OnMapReadyCallback, LocationListener, View.OnClickListener {
    public static final String TAG = "PLACE IN MAP FRAGMENT";
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private OnPlaceInMapFragmentListener mListener;

    private View view;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private TextView txtLatitude, txtLongitude;

    private LocationManager locationManager;
    private int TIME_FOR_UPDATE = 10000;
    private int DISTANCE_FOR_UPDATE = 10;
    private Location location;

    public PlaceInMapFragment() {
    }

    public static PlaceInMapFragment newInstance(String param1) {
        PlaceInMapFragment fragment = new PlaceInMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_place_in_map, container, false);
        setupView();
        getCurrentLocation();
        return view;
    }

    private void setupView() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapVisitSucre);
        //if (mapFragment != null)
            mapFragment.getMapAsync(this);
        txtLatitude = (TextView) view.findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) view.findViewById(R.id.txtLongitude);
        Button btnLocation = (Button) view.findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(this);
    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            showGPSEnabled();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);
        showLocation(location);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLocation:
                moveToLocation(location);
                break;
        }
    }

    private void moveToLocation(Location location){
        try{
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Your location"));

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /*LOCATION LISTENER*/
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "CURRENT LOCATION >> Latitude: " + location.getLatitude());
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Global.showMessage("Provide Off");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Global.showMessage("Provide On");
    }

    private void showLocation(Location location){
        if(location != null){
            txtLatitude.setText("Latitude: " + location.getLatitude());
            txtLongitude.setText("Longitude: " + location.getLongitude());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPlaceInMapFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPlaceInMapFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    public void showGPSEnabled(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("GPS Enable");
        dialog.setMessage("You want to activate gps?");
        dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
            }
        });
        dialog.create().show();
    }

    public interface OnPlaceInMapFragmentListener {
        void onPlaceLocaled(Uri uri);
    }
}