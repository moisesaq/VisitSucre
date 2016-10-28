package com.apaza.moises.visitsucre.global;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.google.android.gms.location.DetectedActivity;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static final int CODE_PERMISSION_INTERNET = 99;
    public static final String[] PERMISSION_INTERNET = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};

    public static final int CODE_PERMISSION_CAMERA = 100;
    public static final String[] PERMISSION_CAMERA = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final int CODE_PERMISSION_LOCATION = 200;
    public static final String[] PERMISSION_PASSENGER = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};

    public static String DIRECTORY_IMAGES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/visitSucre/";
    public static final int REQUEST_CAMERA = 0;
    public static final int SELECT_FILE = 1;

    public static final double latitudeDefault = -34.60386351802505;
    public static final double longitudeDefault = -58.38021799928402;

    //LANGUAGE
    public static final String LANGUAGE_SPANISH = "es";
    public static final String LANGUAGE_ENGLISH = "en";

    /*LOCATION API*/
    public static final String BROADCAST_ACTION = "broadcast-action";
    public static final String ACTIVITY_KEY = "activites-key";
    public static final long ACTIVITY_RECOGNITION_INTERVAL = 0;
    public static final long UPDATE_INTERVAL = 10000;
    public static final long UPDATE_FASTEST_INTERVAL = UPDATE_INTERVAL / 2;

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

    public static boolean hasPermission(String[] permissions){
        int cont = 0;
        for (String permission: permissions) {
            if(ActivityCompat.checkSelfPermission(Global.getContext(), permission) == PackageManager.PERMISSION_GRANTED)
                cont++;
        }

        return cont == permissions.length;
    }

    public static boolean resultPermission(int[] grantResults){
        int cont = 0;
        for(int i = 0; i < grantResults.length; i++){
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED )
                cont++;
        }
        return cont == grantResults.length;
    }

    public static void showToastMessage(String message){
        Toast.makeText(Global.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static String getStringActivity(int type) {
        switch (type) {
            case DetectedActivity.IN_VEHICLE:
                return "Vehículo";
            case DetectedActivity.ON_BICYCLE:
                return "Bicicleta";
            case DetectedActivity.ON_FOOT:
                return "Caminando o corriendo";
            case DetectedActivity.RUNNING:
                return "Corriendo";
            case DetectedActivity.STILL:
                return "Sin movimiento";
            case DetectedActivity.TILTING:
                return "Inclinación brusca";
            case DetectedActivity.UNKNOWN:
                return "Desconocido";
            case DetectedActivity.WALKING:
                return "Caminando";
            default:
                return "Tipo no idenficado";
        }
    }

    public static int getActivityIcon(int type) {
        switch (type) {
            case DetectedActivity.STILL:
                return R.drawable.ic_still;
            case DetectedActivity.WALKING:
                return R.drawable.ic_walk;
            case DetectedActivity.RUNNING:
                return R.drawable.ic_run;
            default:
                return R.drawable.ic_question;
        }
    }

}
