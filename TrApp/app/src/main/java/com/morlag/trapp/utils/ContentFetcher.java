package com.morlag.trapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ContentFetcher {
    private static final String TAG = "ContentFetcher";
    // Создание синглтон класса, мы это изучали как делать
    private static ContentFetcher inst;
    public static ContentFetcher getInstance(){
        if(inst == null)
            inst = new ContentFetcher();
        return inst;
    }
    private ContentFetcher(){}

    public byte[] getByteContent(URL url, String requestType, String token, String data){
        if(url == null)
            return null;

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();  // Открыте соединения
            if(requestType.equals("POST")||requestType.equals("PUT"))
                connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestMethod(requestType);   // Установка типа запроса (GET, POST и т.д.)
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8"); // Просто нужно
            connection.setRequestProperty("Authorization", token);  // Запись токена в запрос
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();
            if(data != null) {
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os,"UTF-8");
                osw.write(data);
                osw.flush();
                osw.close();
                //byte[] bs = data.getBytes("utf-8");
                //os.write(bs,0,bs.length);
                //os.close();
            }
            InputStream is = null;
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                is = connection.getInputStream();
            else
                is = connection.getErrorStream();
            byte[] bytes = new byte[2048];  // Буферный массив байт
            int readed;
            ByteArrayOutputStream os = new ByteArrayOutputStream(); // Специальный класс для хранения байтов
            while ((readed = is.read(bytes,0,2048)) != -1) { // Пока в потоке будет информация
                if(bytes!=null)
                    os.write(bytes, 0, readed); // Записать её в хранилище
            }
            return os.toByteArray();    // Вернуть массив байт
        }
        catch (IOException ex){ // Обработка ошибок, эту писать не обязательно
            Log.d(TAG, "getByteContent: IOException Error", ex);
        }
        catch (Exception ex) {  // Но одну обработку придется написать
            Log.d(TAG, "getByteContent: Error", ex);
        }
        finally {
            if(connection!=null)
                connection.disconnect();
        }

        return null;
    }
    public byte[] getByteContent(URL url){  // В случае если вызывается контент без типа запроса
        return getByteContent(url,"GET", "empty",null);  // То вызывается тип GET с пустым токеном
    }
    public String getStringContent(URL url){    // Для получения строкового ответа
        byte[] bytes = getByteContent(url); // Используется прошлый метод
        if(bytes != null)
            return new String(bytes); // Преобразуется в строку и отправляется
        return null;
    }
    public String getStringContent(URL url, String requestType, String token, String data){
        byte[] bytes = getByteContent(url,requestType,token,data);
        if(bytes != null)
            return new String(bytes);
        return null;
    }
    public Bitmap getBitmapContent(URL url){
        byte[] data = getByteContent(url);  // Получение байтовой информации
        Bitmap res = BitmapFactory.decodeByteArray(data,0,data.length); // Преобразование её в картинку
        return res;
    }
}
