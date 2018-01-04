package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginInteractor {

    private static final String TAG = "LOGIN INTERACTOR";
    private final Context mContext;
    private FirebaseAuth mFirebaseAuth;

    public LoginInteractor(Context context, FirebaseAuth firebaseAuth){
        this.mContext = context;
        if(firebaseAuth != null){
            this.mFirebaseAuth = firebaseAuth;
        }else {
            throw new RuntimeException("FirebaseAuth can not be null");
        }
    }

    public void login(String email, String password, final CallBack callBack){
        /*CHECK INTERNET*/
        if(!isNetworkAvailable()){
            callBack.onNetworkConnectFailed();
            return;
        }

        /*CHECK GOOGLE PLAY SERVICE*/
        if(!isGooglePlayServicesAvailable(callBack)){
            return;
        }

        /*REQUEST FIREBASE AUTHENTICATION*/
        signInUser(email, password, callBack);
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isAvailable());
    }

    private boolean isGooglePlayServicesAvailable(CallBack callBack){
        int statusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext);

        if(GoogleApiAvailability.getInstance().isUserResolvableError(statusCode)){
            callBack.onBeUserResolvableError(statusCode);
            return false;
        }else if(statusCode != ConnectionResult.SUCCESS){
            callBack.onGooglePlayServiceFailed();
            return false;
        }
        return true;
    }

    private void signInUser(String email, String password, final CallBack callBack){
<<<<<<< HEAD
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, task.getResult().toString());
=======
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, task.getResult().toString());
>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
                        if(!task.isSuccessful()){
                            callBack.onAuthFailed(task.getException().toString());
                        }else{
                            callBack.onAuthSuccess();
                        }
                    }
                });
    }

    interface CallBack{
        void onNetworkConnectFailed();
        void onBeUserResolvableError(int errorCode);
        void onGooglePlayServiceFailed();
        void onAuthFailed(String msg);
        void onAuthSuccess();
    }
}
