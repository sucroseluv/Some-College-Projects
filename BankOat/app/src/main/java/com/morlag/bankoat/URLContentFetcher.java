package com.morlag.bankoat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.CharBuffer;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class URLContentFetcher {
    public static final String TAG = "URLContentFetcher";

    private byte[] getBytes(Context context,URL url, String method, boolean withoutProperties) throws IOException {
        InputStream stream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            if(!withoutProperties)
                connection.setRequestProperty("Authenticate", AppPreferences.getToken(context));
            stream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while((bytesRead = stream.read(buffer)) > 0)
                baos.write(buffer,0,bytesRead);
            return baos.toByteArray();
        } catch (Exception ex) {
            Log.e(TAG, "getBytes: ", ex);
            return null;
        }
        finally {
            if(stream != null)
                stream.close();
        }
    }
    private byte[] getBytes(Context context,URL url) throws IOException{
        return getBytes(context, url, "GET", false);
    }
    public String getString(Context context,URL url) throws IOException {
        return new String(getBytes(context, url));
    }
    public String getString(Context context,URL url, String method) throws IOException {
        return new String(getBytes(context, url, method, false));
    }
    public String getString(Context context,Uri uri) throws IOException {
        return new String(getBytes(context, new URL(uri.toString())));
    }
    public String getString(Context context, Uri uri,String method) throws IOException {
        return new String(getBytes(context, new URL(uri.toString()),method,false));
    }
    public String getString(Context context, Uri uri,String method,boolean withoutProperties) throws IOException {
        return new String(getBytes(context, new URL(uri.toString()),method,withoutProperties));
    }
    public Bitmap getBitmap(Context context, URL url) throws IOException {
        byte[] array = getBytes(context, url);
        return BitmapFactory.decodeByteArray(array,0,array.length);
    }
    public Drawable getDrawable(Context context, URL url) throws IOException {
        return new BitmapDrawable(context.getResources(),getBitmap(context, url));
    }
}
