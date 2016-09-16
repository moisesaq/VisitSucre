package com.apaza.moises.visitsucre.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

public class SupportMap extends SupportMapFragment{

    private View originalView;
    private TouchableWrapper touchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        originalView = super.onCreateView(inflater, container, savedInstanceState);
        touchView = new TouchableWrapper(getActivity());
        touchView.addView(originalView);
        return touchView;
    }

    @Override
    public View getView(){
        return originalView;
    }

    public void setOnTouchMapListener(TouchableWrapper.OnTouchSupportMapListener onTouchMapListener){
        touchView.setOnTouchSupportMapListener(onTouchMapListener);
    }
}
