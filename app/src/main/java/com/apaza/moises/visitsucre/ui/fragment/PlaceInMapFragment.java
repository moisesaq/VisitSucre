package com.apaza.moises.visitsucre.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.apaza.moises.visitsucre.database.Place;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;
import com.apaza.moises.visitsucre.global.Global;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class PlaceInMapFragment extends BaseFragment implements OnMapReadyCallback, LocationListener, View.OnClickListener,
                                                            GoogleMap.OnCameraChangeListener{
    public static final String TAG = "PLACE IN MAP FRAGMENT";
    private static final String ID_PLACE = "idPlace";
    private long idPlace;

    private OnPlaceInMapFragmentListener onPlaceInMapFragmentListener;

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

    public static PlaceInMapFragment newInstance(long idPlace) {
        PlaceInMapFragment fragment = new PlaceInMapFragment();
        Bundle args = new Bundle();
        args.putLong(ID_PLACE, idPlace);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnPlaceInMapFragmentListener(OnPlaceInMapFragmentListener listener){
        this.onPlaceInMapFragmentListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            idPlace = getArguments().getLong(ID_PLACE, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_place_in_map, container, false);
        setupView();
        getCurrentLocation();
        //loadMarker();
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
        LatLng latLng = new LatLng(Utils.latitudeDefault, Utils.longitudeDefault);
        if(location != null){
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        moveToLocation(latLng);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLocation:
                /*LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                moveToLocation(latLng);*/
                loadMarker();
                break;
        }
    }

    private void loadMarker(){
        if(idPlace > 0)
            addMarker();
        else
            prepareSelectionMode();
    }

    private void prepareSelectionMode(){
        LatLng latLng = new LatLng(Utils.latitudeDefault, Utils.longitudeDefault);
        moveToLocation(latLng);

        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(false);
        this.googleMap.setOnCameraChangeListener(this);
        //mapFragment.getView();
    }

    private void moveToLocation(LatLng latLng){
        try{
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Your location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addMarker(){
        Place place = Global.getDataBaseHandler().getDaoSession().getPlaceDao().loadDeep(idPlace);
        LatLng latLng = new LatLng(Utils.latitudeDefault, Utils.longitudeDefault);//LatLng(place.getLatitude(), place.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .title(place.getName())
                .position(latLng);
        showMarker(latLng);
        this.googleMap.addMarker(markerOptions);
    }

    private void showMarker(LatLng latLng){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(12)
                //.bearing(90)
                .tilt(45)
                .build();
        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
    public void onDetach() {
        super.onDetach();
        onPlaceInMapFragmentListener = null;
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

    /*GOOGLE MAPS - CAMERA CHANGELISTENER*/
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LatLng centerLocation = cameraPosition.target;
        searchLocation(centerLocation);
    }

    private void searchLocation(final LatLng latLng){
        final Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        new AsyncTask<Void, Void, List<Address>>(){
            @Override
            public void onPreExecute(){
                txtLatitude.setText("Searching...");
            }

            @Override
            public List<Address> doInBackground(Void... params){
                List<Address> list = null;
                try{
                    list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return list;
            }

            @Override
            public void onPostExecute(List<Address> result){
                if(result != null && result.size() > 0){
                    Address address = result.get(0);
                    txtLatitude.setText("Result: " + address.getAddressLine(0) + " - " +address.getLatitude());
                }else{
                    txtLatitude.setText("No result :(");
                }
            }
        }.execute();
    }

    public interface OnPlaceInMapFragmentListener {
        void onPlaceLocaled(Address address);
    }
}
