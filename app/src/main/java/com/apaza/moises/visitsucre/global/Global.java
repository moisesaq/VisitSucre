package com.apaza.moises.visitsucre.global;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.apaza.moises.visitsucre.deprecated.HandlerDBVisitSucre;
import com.apaza.moises.visitsucre.provider.DataBaseHandler;
import com.apaza.moises.visitsucre.web.ApiVisitSucreClient;
import com.apaza.moises.visitsucre.web.api.volley.VolleySingleton;

public class Global {
    private static Activity context;
    private static ApiVisitSucreClient apiVisitSucreClient;

    private static HandlerDBVisitSucre handlerDBVisitSucre;

    public static void setContext(Activity context){
        Global.context = context;
        DataBaseHandler.getInstance(context);
    }

    public static Activity getContext(){
        return context;
    }

    public static VolleySingleton getVolleySingleton(){
        return VolleySingleton.getInstance();
    }

    public static HandlerDBVisitSucre getHandlerDBVisitSucre(){
        if(handlerDBVisitSucre == null)
            handlerDBVisitSucre = HandlerDBVisitSucre.getInstance(context);
        return handlerDBVisitSucre;
    }

    public static DataBaseHandler getDataBaseHandler(){
        return DataBaseHandler.getInstance(context);
    }

    public static ApiVisitSucreClient getApiVisitSucreClient(){
        if(apiVisitSucreClient == null)
            apiVisitSucreClient = new ApiVisitSucreClient(Constants.BASE_URL);
        return apiVisitSucreClient;
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
