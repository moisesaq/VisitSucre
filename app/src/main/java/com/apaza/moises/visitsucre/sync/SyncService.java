package com.apaza.moises.visitsucre.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
<<<<<<< HEAD
=======
import android.util.Log;
>>>>>>> 0948feacd57ce90e6b793f77fcd10366e1ef4582

public class SyncService extends Service{

    private static SyncAdapter syncAdapter = null;
    private final static Object lock = new Object();
    @Override
    public void onCreate(){
<<<<<<< HEAD
        synchronized (lock){
            if(syncAdapter == null){
=======
        Log.d("SYNC SERVICE", "ON CREATE SYNC SERVICE");
        synchronized (lock){
            if(syncAdapter == null){
                Log.d("SYNC SERVICE", "INSTANCE SYNC SERVICE");
>>>>>>> 0948feacd57ce90e6b793f77fcd10366e1ef4582
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
