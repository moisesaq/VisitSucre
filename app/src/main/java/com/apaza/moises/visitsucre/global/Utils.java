package com.apaza.moises.visitsucre.global;

import android.content.Context;
import android.graphics.Color;

import com.apaza.moises.visitsucre.R;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.Locale;

public class Utils {

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

}
