package com.morlag.trapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    public static final String TAG = "Comment";
    private int id;
    private String body;
    private User user;

    public Comment(int id, String body, User user) {
        this.id = id;
        this.body = body;
        this.user = user;
    }


    public static Comment getCommentFromJSONObject(JSONObject object){
        try {
            int id = object.getInt("id");
            String body = object.getString("body");
            User user = User.getUserFromJSONObject(object.getJSONObject("user"));
            return new Comment(id,body,user);
        }
        catch (JSONException ex){
            Log.d(TAG, "getFilmFromJson: JSONException Error", ex);
        }
        catch (Exception ex){
            Log.d(TAG, "getFilmFromJson: Error", ex);
        }
        return null;
    }
    public static Comment[] getCommentsFromJSONArray(JSONArray array){
        try {
            Comment[] comments = new Comment[array.length()];
            for(int i = 0; i < comments.length; i++){
                comments[i] = getCommentFromJSONObject(array.getJSONObject(i));
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
