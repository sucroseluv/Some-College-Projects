package com.morlag.trapp.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Login {
    private static final String TAG = "Login";

    public Login(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public boolean success;
    public String token;

    public static Login getLoginFromJson(String json){
        try {   // Парсинг из элементов json-объекта в поля объекта Film
            JSONObject object = new JSONObject(json);
            boolean success = object.getBoolean("success");
            String token = object.getString("token");
            Login res = new Login(success,token);
            return res;
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }
        return null;
    }
}
