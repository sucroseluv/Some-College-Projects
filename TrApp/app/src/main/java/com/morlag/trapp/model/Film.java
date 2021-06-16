package com.morlag.trapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;

public class Film {
    public static final String TAG = "Film";

    private int id;
    private String name;
    private String description;
    private Genre[] genres;
    private Comment[] comments;
    private float rating;

    public Film(int id, String name, String description, Genre[] genres, Comment[] comments, float rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.genres = genres;
        this.comments = comments;
        this.rating = rating;
    }

    public String getGenresStr(){
        String genresStr = "";
        for(Genre genre : genres){
            genresStr += genre.getName()+", ";
        }
        if(genresStr.length()!=0){
            genresStr = String.format("%s.",genresStr.substring(0,genresStr.length()-2));
            genresStr = String.format("%s%s",Character.toUpperCase(genresStr.charAt(0)),
                    genresStr.toLowerCase().substring(1));
        }
        return genresStr;
    }

    public static Film[] getFilmsFromJson(String json) {
        JSONArray array = null;
        try {
            array = new JSONArray(json);    // Преобразование строки в массив json-объектов
            Film[] films = new Film[array.length()];
            for(int i = 0; i < films.length; i++){  // Перебор по json-объектам
                JSONObject o = array.getJSONObject(i);  // и преобразование их в объекты Film
                Film film = getFilmFromJSONObject(o);
                films[i] = film;
            }
            return films;
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }

        return null;
    }
    public static Film getFilmFromJson(String json){
        try {
            return getFilmFromJSONObject(new JSONObject(json));
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }
        return null;
    }
    public static Film getFilmFromJSONObject(JSONObject object){
        try {   // Парсинг из элементов json-объекта в поля объекта Film
            int id = object.getInt("id");
            String name = object.getString("name");
            String description = object.getString("description");
            Genre[] genres = Genre.getGenresFromJSONArray(object.getJSONArray("genres"));
            Comment[] comments = Comment.getCommentsFromJSONArray(object.getJSONArray("comments"));
            Film film = new Film(id,name,description,genres,comments,4.9f);
            return film;
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }
}
