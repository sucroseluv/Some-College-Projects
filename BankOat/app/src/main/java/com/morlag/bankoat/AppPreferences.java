package com.morlag.bankoat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {
    private static final String PREF_TOKEN = "token";
    private static final String PREF_SERVER = "server";
    public static String getToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_TOKEN, null);
    }
    public static void setToken(Context context, String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_TOKEN, token)
                .apply();

    }
    public static String getServer(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SERVER, null);
    }
    public static void setServer(Context context, String server) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SERVER, server)
                .apply();
    }


}
