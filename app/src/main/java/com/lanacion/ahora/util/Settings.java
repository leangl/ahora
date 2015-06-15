package com.lanacion.ahora.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lanacion.ahora.Ahora;

/**
 * Created by Ignacio Saslavsky on 07/02/15.
 * correonano@gmail.com
 */
public class Settings {

    private static final Settings instance = new Settings();
    private SharedPreferences sharedPreferences;

    private Settings() {
        Context context = Ahora.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Settings getInstance() {
        return instance;
    }

}
