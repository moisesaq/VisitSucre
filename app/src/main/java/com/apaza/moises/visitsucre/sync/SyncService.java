package com.apaza.moises.visitsucre.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class SyncService extends Service{

    private static SyncAdapter syncAdapter = null;
    private final static Object lock = new Object();
    @Override
    public void onCreate(){
        Log.d("SYNC SERVICE", "ON CREATE SYNC SERVICE");
        synchronized (lock){
            if(syncAdapter == null){
                Log.d("SYNC SERVICE", "INSTANCE SYNC SERVICE");
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
