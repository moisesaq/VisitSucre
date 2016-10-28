package com.apaza.moises.visitsucre.global;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class VisitSucreApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupConfigFacebook();
    }

    private void setupConfigFacebook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
