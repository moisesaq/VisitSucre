package com.apaza.moises.visitsucre.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AuthenticationService extends Service{
    private VisitSucreAuthenticator visitSucreAuthenticator;

    @Override
    public void onCreate(){
        visitSucreAuthenticator = new VisitSucreAuthenticator(this);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return visitSucreAuthenticator.getIBinder();
    }
}
