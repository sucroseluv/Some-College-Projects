package com.morlag.trapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String PREFERENCES = "preferences";
    private static AppPreferences inst;
    private static SharedPreferences preferences;

    public static AppPreferences getInstance(Context context){
        if(inst == null)
            inst = new AppPreferences(context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE));
        return inst;
    }

    private AppPreferences(SharedPreferences preferences){
        this.preferences = preferences;
    }

    private final String CURRENT_SERVER = "server";
    public String getCurrentServer(){
        if(preferences.contains(CURRENT_SERVER))
            return preferences.getString(CURRENT_SERVER,"");
        return "Server is empty";
    }
    public void setCurrentServer(String server){
        preferences.edit().putString(CURRENT_SERVER,server).apply();
    }

    private final String TOKEN = "token";
    public String getToken(){
        if(preferences.contains(TOKEN))
            return preferences.getString(TOKEN,"");
        return "Server is empty";
    }
    public void setToken(String token){
        preferences.edit().putString(TOKEN,token).apply();
    }
}
