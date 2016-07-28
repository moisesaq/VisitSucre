package com.apaza.moises.visitsucre.global;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.apaza.moises.visitsucre.provider.HandlerDBVisitSucre;

public class Global {
    public static String urlCategory = "http://192.168.1.42:3000/api/categories";
    public static String urlPlace = "http://192.168.1.42:3000/api/places";
    public static String urlPlaceFind = "http://192.168.1.42:3000/api/place/find";
    public static String urlCategoryFind = "http://192.168.1.42:3000/api/category/find";
    public static String urlImage = "http://vignette2.wikia.nocookie.net/ultradragonball/images/2/28/543px-MajinBuuFatNV.png/revision/latest?cb=20110330215918";

    private static Activity context;
    private static ApiRest apiRest;

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

    public static ApiRest getApiRest(){
        if(apiRest == null)
            apiRest = ApiRest.getInstance(context);
        return apiRest;
    }

    public static HandlerDBVisitSucre getHandlerDBVisitSucre(){
        if(handlerDBVisitSucre == null)
            handlerDBVisitSucre = HandlerDBVisitSucre.getInstance(context);
        return handlerDBVisitSucre;
    }
}
