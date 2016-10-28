package com.apaza.moises.visitsucre.global;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class IFirebaseInstanceIdService extends FirebaseInstanceIdService{

    private static final String TAG = IFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String fbmToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FBM token :" + fbmToken);
    }

    private void sendTokenToServer(){

    }
}
