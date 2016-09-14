package com.apaza.moises.visitsucre.global;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static final double latitudeDefault = -34.60386351802505;
    public static final double longitudeDefault = -58.38021799928402;

    //LANGUAGE
    public static final String LANGUAGE_SPANISH = "es";
    public static final String LANGUAGE_ENGLISH = "en";

    public static String getCurrentLanguage() {
        String locale = Locale.getDefault().getLanguage();
        if(locale.equals(LANGUAGE_SPANISH)){
            return LANGUAGE_SPANISH;
        }

        return LANGUAGE_ENGLISH;
    }

    public static LoadToast showMessageTest(Context context, String message){
        LoadToast loadToast = new LoadToast(context);
        loadToast.setText(message);
        loadToast.setTranslationY(150);
        loadToast.setTextColor(R.color.textPrimary).setBackgroundColor(Color.GREEN).setProgressColor(R.color.color_accent);
        return loadToast;
    }

    public static Date getCurrentDate(){
        return Calendar.getInstance().getTime();
    }

    public static String generateCodeUnique(String text){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
        String date = dateFormat.format(new Date());
        return text + "-" + date;
    }

    public static JSONObject cursorParseToJSONObject(Cursor cursor){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ContractVisitSucre.Category.LOGO, cursor.getString(2));
            jsonObject.put(ContractVisitSucre.Category.NAME, cursor.getString(3));
            jsonObject.put(ContractVisitSucre.Category.DATE, cursor.getString(4));
            jsonObject.put(ContractVisitSucre.Category.DESCRIPTION, cursor.getString(5));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }

}
