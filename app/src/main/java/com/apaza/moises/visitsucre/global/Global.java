package com.apaza.moises.visitsucre.global;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.apaza.moises.visitsucre.provider.HandlerDBVisitSucre;

public class Global {
    public static final String ACCOUNT_TYPE = "com.apaza.moises.visitsucre.account";

    public static final String url = "http://192.168.1.43:3000/";
    public static String urlCategory = url + "api/categories";
    public static String urlPlace = url + "api/places";
    public static String urlPlaceFind = url + "api/place/find";
    public static String urlCategoryFind = url + "api/category/find";
    public static String urlImage = "http://vignette2.wikia.nocookie.net/ultradragonball/images/2/28/543px-MajinBuuFatNV.png/revision/latest?cb=20110330215918";

    private static Activity context;
    private static VolleySingleton volleySingleton;

    private static HandlerDBVisitSucre handlerDBVisitSucre;

    public static void setContext(Activity context){
        Global.context = context;
    }

    public static Activity getContext(){
        return context;
    }

    public static void showMessage(String message){
        View view = getContext().findViewById(android.R.id.content);
        if(view != null)
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
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
}
