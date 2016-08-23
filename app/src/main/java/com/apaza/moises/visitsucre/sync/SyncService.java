package com.apaza.moises.visitsucre.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class SyncService extends Service{

    private static SyncAdapter syncAdapter = null;
    private final static Object lock = new Object();
    @Override
    public void onCreate(){
        synchronized (lock){
            if(syncAdapter == null){
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
