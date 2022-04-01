package com.nehad.winmarkettrackingapplication.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class LanguageManager  extends AppCompatActivity {
    private Context ct ;
    private SharedPreferences sharedPreferences;


    public LanguageManager(Context ctx) {
        ct = ctx;
        sharedPreferences = ct.getSharedPreferences("LANG" ,Context.MODE_PRIVATE);
    }
    public String getLang() {
        return sharedPreferences.getString("lang", "ar");
    }


    public void updateResources(String code ){

        Locale locale  = new Locale(code);
        Locale.setDefault(locale);

        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setLang(code);

    }
    public void setLang(String code ) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang", code);
        editor.commit();

    }


}

