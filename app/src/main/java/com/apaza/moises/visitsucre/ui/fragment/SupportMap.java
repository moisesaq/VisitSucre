package com.apaza.moises.visitsucre.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.apaza.moises.visitsucre.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class SupportMap extends Fragment implements OnMapReadyCallback, View.OnTouchListener{

    private View view;
    private MapView mapView;
    private GoogleMap googleMap;

    private OnSupportMapListener onSupportMapListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_support, container, false);
        MapsInitializer.initialize(getActivity());
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public GoogleMap getGoogleMap(){
        return googleMap;
    }

    public void setOnSupportMapListener(OnSupportMapListener listener){
        this.onSupportMapListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(onSupportMapListener != null)
                    onSupportMapListener.onDownMap();
                break;
            case MotionEvent.ACTION_UP:
                if(onSupportMapListener != null)
                    onSupportMapListener.onUpMap();
                break;
        }
        return false;
    }

    public interface OnSupportMapListener{
        void onDownMap();
        void onUpMap();
        //void onLocation(LatLng latLng);
    }
}
