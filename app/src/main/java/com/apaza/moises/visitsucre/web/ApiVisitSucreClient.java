package com.apaza.moises.visitsucre.web;

import android.location.Address;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.web.api.ApiClient;


public class ApiVisitSucreClient extends ApiClient{

    public ApiVisitSucreClient(String url) {
        super(url);
    }

    public void getGoogleMapStatic(Address address, ImageLoader.ImageListener imageListener){
        Global.getVolleySingleton().getImageLoader().get(prepareUrlStaticMap(address), imageListener);
    }

    private String prepareUrlStaticMap(Address address){
        String txtLocation = address.getLatitude() + "," + address.getLongitude();
        String URL_BASE = "https://maps.googleapis.com/maps/api/staticmap?";

        String URL_STATIC_MAP = "center=" + txtLocation + "&zoom=16&size=600x300&scale=2&maptype=roadmap" +
                "&markers=color:red%7Clabel:P%7C" + txtLocation +
                "&key="+ Global.getContext().getString(R.string.google_key);

        Log.d(TAG, "IMAGE URL >>> " + URL_BASE + URL_STATIC_MAP);
        return URL_BASE + URL_STATIC_MAP;
    }
}
