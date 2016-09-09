package com.apaza.moises.visitsucre.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.apaza.moises.visitsucre.database.DaoMaster;
import com.apaza.moises.visitsucre.database.DaoSession;

public class DataBaseHandler {
    public static String TAG = "VISIT_SUCRE_DATA_BASE_HANDLER";
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private static DataBaseHandler handler;

    private DataBaseHandler(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, ContractVisitSucre.NAME_DB, null);
        try{
            SQLiteDatabase db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            ProviderSucre.setDaoSession(daoSession);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Log.d(TAG, "DATA BASE STARTED");
        }
    }

    public static DataBaseHandler getInstance(Context context){
        if(handler == null)
            handler = new DataBaseHandler(context);
        return  handler;
    }

    public DaoSession getDaoSession(){
        daoSession = daoMaster.newSession();
        return daoSession;
    }
}
