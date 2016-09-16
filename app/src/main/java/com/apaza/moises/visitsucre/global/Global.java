package com.apaza.moises.visitsucre.global;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.apaza.moises.visitsucre.deprecated.HandlerDBVisitSucre;
import com.apaza.moises.visitsucre.provider.DataBaseHandler;

public class Global {
    private static Activity context;
    private static VolleySingleton volleySingleton;

    private static HandlerDBVisitSucre handlerDBVisitSucre;

    public static void setContext(Activity context){
        Global.context = context;
        DataBaseHandler.getInstance(context);
    }

    public static Activity getContext(){
        return context;
    }

    public static VolleySingleton getVolleySingleton(){
        if(volleySingleton == null)
            volleySingleton = VolleySingleton.getInstance(context);
        return volleySingleton;
    }

    public static HandlerDBVisitSucre getHandlerDBVisitSucre(){
        if(handlerDBVisitSucre == null)
            handlerDBVisitSucre = HandlerDBVisitSucre.getInstance(context);
        return handlerDBVisitSucre;
    }

    public static DataBaseHandler getDataBaseHandler(){
        return DataBaseHandler.getInstance(context);
    }

    public static void showMessage(String message){
        View view = getContext().findViewById(android.R.id.content);
        if(view != null)
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showToastMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
