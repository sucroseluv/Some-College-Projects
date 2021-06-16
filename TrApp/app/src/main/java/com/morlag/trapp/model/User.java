package com.morlag.trapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public static final String TAG = "User";
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public static User getUserFromJson(String json){
        try {
            return getUserFromJSONObject(new JSONObject(json));
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }
        return null;
    }
    public static User getUserFromJSONObject(JSONObject object){
        try {
            int id = object.getInt("id");
            String name = object.getString("name");
            return new User(id,name);
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }
        return null;
    }
    public static User[] getUserFromJSONArray(JSONArray array){
        try {
            User[] comments = new User[array.length()];
            for(int i = 0; i < comments.length; i++){
                comments[i] = getUserFromJSONObject(array.getJSONObject(i));
            }
            return comments;
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
