package com.morlag.bankoat;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;


public class BankApiUser {
    public static final String TAG = "BankApiUser";
    public static final Uri uriStart = new Uri.Builder()
            .scheme("http")
            .encodedAuthority("94.137.0.138:13512")
            .appendPath("api")
            .build();
    public static final Uri uriWithoutApiStart = new Uri.Builder()
            .scheme("http")
            .encodedAuthority("94.137.0.138:13512")
            .build();

    public User getUserInfo(Context context){
        URLContentFetcher fetcher = new URLContentFetcher();
        Uri uri = uriStart.buildUpon()
                .appendPath("user")
                .build();
        String json = "";
        try {
            json = fetcher.getString(context,uri);
        } catch (Exception ex) {
            Log.d(TAG, "getUserInfo: fetcher");
        }

        JSONObject object = null;
        try {
            object = new JSONObject(json).getJSONObject("response");
        }
        catch (Exception ex){
            Log.d(TAG, "getUserInfo: json");
        }
        User user = new User();

        if(object==null)
            return null;

        try {
            user.setBalance(object.getInt("balance"));
            user.setId(object.getInt("id"));
            user.setPhone(object.getString("phone"));
            user.setFirstname(object.getString("firstname"));
            user.setLastname(object.getString("lastname"));
            user.setMiddlename(object.getString("middlename"));
            user.setPicture(object.getString("picture"));
        }
        catch (Exception ex) {
            Log.d(TAG, "getUserInfo: object");
        }
        return user;
    }
    public String requestReceiver(Context context, int sum){
        URLContentFetcher fetcher = new URLContentFetcher();
        Uri uri = uriStart.buildUpon()
                .appendPath("user")
                .appendPath("transfer")
                .appendPath("receiver")
                .appendQueryParameter("amount",String.valueOf(sum))
                .build();
        String json = "";
        try {
            json = fetcher.getString(context,uri,"POST");
        } catch (Exception ex) {
            Log.d(TAG, "requestReceiver: fetcher");
        }

        JSONObject object = null;
        try {
            object = new JSONObject(json).getJSONObject("response");
        }
        catch (Exception ex){
            Log.d(TAG, "requestReceiver: json");
        }

        if(object==null)
            return null;

        String s = null;
        try {s = object.getString("key");}
        catch (Exception ex) {
            Log.d(TAG, "requestReceiver: key",ex);
        }

        return s;
    }
    public String requestSender(Context context, int sum){
        URLContentFetcher fetcher = new URLContentFetcher();
        Uri uri = uriStart.buildUpon()
                .appendPath("user")
                .appendPath("transfer")
                .appendPath("sender")
                .appendQueryParameter("amount",String.valueOf(sum))
                .build();
        String json = "";
        try {
            json = fetcher.getString(context,uri,"POST");
        } catch (Exception ex) {
            Log.d(TAG, "requestSender: fetcher");
        }

        JSONObject object = null;
        try {
            object = new JSONObject(json).getJSONObject("response");
        }
        catch (Exception ex){
            Log.d(TAG, "requestSender: json");
        }

        if(object==null)
            return null;

        String s = null;
        try {s = object.getString("key");}
        catch (Exception ex) {
            Log.d(TAG, "requestSender: key",ex);
        }

        return s;
    }
    public Transaction getTransaction(Context context, String transactionCode){
        URLContentFetcher fetcher = new URLContentFetcher();
        Uri uri = uriStart.buildUpon()
                .appendPath("user")
                .appendPath("transfer")
                .appendPath(transactionCode)
                .build();
        String json = "";
        try {
            json = fetcher.getString(context,uri);
        } catch (Exception ex) {
            Log.d(TAG, "getTransaction: fetcher");
        }

        JSONObject object = null;
        try {
            object = new JSONObject(json).getJSONObject("response");
        }
        catch (Exception ex){
            Log.d(TAG, "getTransaction: json");
        }

        if(object==null)
            return null;

        Transaction transaction = new Transaction();
        try {
            transaction.setCode(transactionCode);
            transaction.setType(object.getString("type"));
            transaction.setAmount(object.getString("amount"));
            object = object.getJSONObject("user");
            if(object == null)
                throw new Exception();

            transaction.setFirstname(object.getString("firstname"));
            transaction.setLastname(object.getString("lastname"));
            transaction.setMiddlename(object.getString("middlename"));
            transaction.setPhone(object.getString("phone"));
        }
        catch (Exception ex) {
            Log.d(TAG, "getTransaction: transaction",ex);
        }

        return transaction;
    }
    public Operation[] getOperations(Context context, int max){
        URLContentFetcher fetcher = new URLContentFetcher();
        Uri uri = uriStart.buildUpon()
                .appendPath("user")
                .appendPath("operations")
                .appendPath(String.valueOf(max))
                .build();
        String json = "";
        try {
            json = fetcher.getString(context,uri);
        } catch (Exception ex) {
            Log.d(TAG, "getOperations: fetcher");
        }

        JSONArray object = null;
        try {
            object = new JSONObject(json).getJSONArray("response");
        }
        catch (Exception ex){
            Log.d(TAG, "getOperations: json");
        }

        if(object==null)
            return null;

        Operation[] operations = new Operation[max];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            for(int i = 0; i < object.length(); i++){
                JSONObject obj = object.getJSONObject(i);
                if(obj == null)
                    throw new Exception();

                operations[i] = new Operation();
                operations[i].amount = obj.getInt("amount");
                operations[i].title = obj.getString("title");
                operations[i].picture = obj.getString("picture");
                operations[i].type = obj.getInt("type");
                operations[i].date = format.parse (obj.getString("date"));
            }
        }
        catch (Exception ex) {
            Log.d(TAG, "getTransaction: transaction",ex);
        }

        return operations;
    }
    public boolean executeTransaction(Context context, String transactionCode){
        URLContentFetcher fetcher = new URLContentFetcher();
        Uri uri = uriStart.buildUpon()
                .appendPath("user")
                .appendPath("transfer")
                .appendPath(transactionCode)
                .appendPath("execute")
                .build();
        String json = "";
        try {
            json = fetcher.getString(context,uri);
        } catch (Exception ex) {
            Log.d(TAG, "executeTransaction: fetcher");
        }

        JSONObject object = null;
        try {
            object = new JSONObject(json);
        }
        catch (Exception ex){
            Log.d(TAG, "executeTransaction: json");
        }

        if(object==null)
            return false;

        Boolean b = false;
        try {
            b = object.getBoolean("error");
        }
        catch (Exception ex) {
            Log.d(TAG, "executeTransaction: error",ex);
        }

        return !b;
    }

    public String loginStepOne(Context context, String phone){
        URLContentFetcher fetcher = new URLContentFetcher();
        Uri uri = uriStart.buildUpon()
                .appendPath("login-step-1")
                .appendQueryParameter("phone", phone)
                .build();
        String json = "";
        try {
            json = fetcher.getString(context,uri,"POST",true);
        } catch (Exception ex) {
            Log.d(TAG, "loginStepOne: fetcher");
        }

        JSONObject object = null;
        try {
            object = new JSONObject(json).getJSONObject("response");
        }
        catch (Exception ex){
            Log.d(TAG, "loginStepOne: json");
        }

        if(object==null)
            return null;

        String s = null;
        try {s = object.getString("auth_token");}
        catch (Exception ex) {
            Log.d(TAG, "loginStepOne: auth_token",ex);
        }

        return s;
    }
    public String loginStepTwo(Context context, String phone, String auth_token, String code){
        URLContentFetcher fetcher = new URLContentFetcher();
        Uri uri = uriStart.buildUpon()
                .appendPath("login-step-2")
                .appendQueryParameter("phone", phone)
                .appendQueryParameter("auth_token", auth_token)
                .appendQueryParameter("code", code)
                .build();
        String json = "";
        try {
            json = fetcher.getString(context,uri,"POST",true);
        } catch (Exception ex) {
            Log.d(TAG, "loginStepTwo: fetcher");
        }

        JSONObject object = null;
        try {
            object = new JSONObject(json).getJSONObject("response");
        }
        catch (Exception ex){
            Log.d(TAG, "loginStepTwo: json");
        }

        if(object==null)
            return null;

        String s = null;
        try {s = object.getString("android_token");}
        catch (Exception ex) {
            Log.d(TAG, "loginStepTwo: auth_token",ex);
        }

        return s;
    }
}
