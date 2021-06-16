package com.morlag.trapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Genre {
    public static final String TAG = "Genre";
    private int id;
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Genre getGenreFromJSONObject(JSONObject object){
        try {
            int id = object.getInt("id");
            String name = object.getString("name");
            return new Genre(id,name);
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }
        return null;
    }
    public static Genre[] getGenresFromJson(String json){
        try {
            return getGenresFromJSONArray(new JSONArray(json));
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }
        return null;
    }
    public static Genre[] getGenresFromJSONArray(JSONArray array){
        try {
            Genre[] genres = new Genre[array.length()];
            for(int i = 0; i < genres.length; i++){
                genres[i] = getGenreFromJSONObject(array.getJSONObject(i));
            }
            return genres;
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
