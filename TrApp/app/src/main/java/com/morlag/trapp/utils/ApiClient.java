package com.morlag.trapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.morlag.trapp.BuildConfig;
import com.morlag.trapp.R;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.model.Genre;
import com.morlag.trapp.model.Login;
import com.morlag.trapp.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiClient {
    public static final String TAG = "ApiClient";
    private static final String API = "api";
    private static final String FILMS = "films";
    private static final String FILM = "film";
    private static final String LOGIN = "login";
    private static final String FAVORITES = "favorites";
    private static final String HISTORY = "history";
    private static final String GENRES = "genres";
    private static final String COMMENTS = "comments";
    private static final String ME = "me";
    private static final String REGISTER = "register";
    private static final String PICTURES = "pictures";
    ContentFetcher fetcher;
    Context mContext;
    String url = "";
    public ApiClient(Context context){
        fetcher = ContentFetcher.getInstance(); // Получение объекта ContentFetcher
        mContext = context; // Сохранение объекта контекста
        url = AppPreferences.getInstance(mContext).getCurrentServer();
            // Получение текущего сервера через SharedPreferences (на экзамене не пригодится)
    }
    public Login login(String login, String password){
        URL url = null;
        try {
            url = new URL(String.format("%s/%s/%s", this.url,API,LOGIN)); // Составление URL для запроса
            String data = "{\"login\":\""+login+"\",\"password\":\""+password+"\"}";
            String json = fetcher.getStringContent(url,"POST",AppPreferences.getInstance(mContext).getToken(),data);
            if(json == null)
                return null;
            Login res = Login.getLoginFromJson(json);
            return res;
            /*
            if(res.success)
                AppPreferences.getInstance(mContext).setToken(res.token);
            return res;*/
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }
    public Login register(String login, String password, String name){
        URL url = null;
        try {
            url = new URL(String.format("%s/%s/%s", this.url,API,REGISTER)); // Составление URL для запроса
            String data = "{\"login\":\""+login+"\",\"password\":\""+password+"\",\"name\":\""+name+"\"}";
            String json = fetcher.getStringContent(url,"POST",AppPreferences.getInstance(mContext).getToken(),data);
            if(json == null)
                return null;
            Login res = Login.getLoginFromJson(json);
            return res;
            /*
            if(res.success)
                AppPreferences.getInstance(mContext).setToken(res.token);
            return res;*/
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }
    public Login comment(String login, String password, String name){
        URL url = null;
        try {
            url = new URL(String.format("%s/%s/%s", this.url,API,REGISTER)); // Составление URL для запроса
            String data = "{\"login\":\""+login+"\",\"password\":\""+password+"\",\"name\":\""+name+"\"}";
            String json = fetcher.getStringContent(url,"POST",AppPreferences.getInstance(mContext).getToken(),data);
            if(json == null)
                return null;
            Login res = Login.getLoginFromJson(json);
            return res;
            /*
            if(res.success)
                AppPreferences.getInstance(mContext).setToken(res.token);
            return res;*/
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }
    public boolean tokenIsValid(){
        URL url = null;
        try {
            url = new URL(String.format("%s/%s/%s", this.url,API,ME));
            String json = fetcher.getStringContent(url,"GET",AppPreferences.getInstance(mContext).getToken(),null);
            if(json == null)
                return false;
            User res = User.getUserFromJson(json);
            if(res == null)
                return false;
            return true;
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return false;
    }
    public User getCurrentUser(){
        URL url = null;
        try {
            url = new URL(String.format("%s/%s/%s", this.url,API,ME));
            String json = fetcher.getStringContent(url,"GET",AppPreferences.getInstance(mContext).getToken(),null);
            if(json == null)
                return null;
            User res = User.getUserFromJson(json);
            return res;
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }

    public Film[] getFilms(){
        URL filmURL = null;
        try {
            filmURL = new URL(String.format("%s/%s/%s",url,API,FILMS)); // Составление URL для запроса
            String json = fetcher.getStringContent(filmURL);    // Запрос к серверу через ContentFetcher

            // убрать
            json = PseudoApiPokaDanilaNeHotchetZapuskatServer_getFilms();

            if(json==null)  // Если сервер ничего не вернул, то ничего не вернуть
                return null;
            return Film.getFilmsFromJson(json); // Вызов метода, преобразующего json в массив фильмов
                // (мы такое на практике учились делать (если надо в конце добавил))
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }

    public Film[] getFilms(String name, int genre_id, float rating, Date premiere, int limit, int offset){
        URL filmURL = null;
        try {
            String urlformat = String.format("%s/%s/%s?",url,API,FILMS);
            if(genre_id!=-1)
                urlformat = urlformat + "&genre_id="+genre_id;
            else
                urlformat = urlformat + "&genre_id="+0;
            if(name!=null)
                urlformat = urlformat + "&name="+name;
            if(rating!=-1)
                urlformat = urlformat + "&rating="+rating;
            else
                urlformat = urlformat + "&rating="+0;
            if(premiere!=null)
                urlformat = urlformat + "&premiere="+premiere;
            if(limit != -1)
                urlformat = urlformat + "&limit="+limit;
            else
                urlformat = urlformat + "&limit="+100;
            if(offset != -1)
                urlformat = urlformat + "&offset="+offset;
            else
                urlformat = urlformat + "&offset="+0;
            filmURL = new URL(urlformat);
            String json = fetcher.getStringContent(filmURL);
            if(json==null)
                return null;
            return Film.getFilmsFromJson(json);
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }

    public Bitmap getImage(int id){
        Bitmap retained = RetainImages.getInstance().getImage(id);
        if(retained!=null)
            return retained;

        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(),PseudoApiPokaDanilaNeHotchetZapuskatServer_getDrawableId(id));
        if(bm!=null)
            return bm;

        URL filmURL = null;
        try {
            filmURL = new URL(String.format("%s/%s/%s/%d", url, PICTURES,FILMS, id));
            Bitmap bitmap = fetcher.getBitmapContent(filmURL);
            RetainImages.getInstance().savedImages.put(id,bitmap);
            return bitmap;
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }

    public Film getFilm(int id){
        URL filmURL = null;
        try{
            filmURL = new URL(String.format("%s/%s/%s/%s",url,API,FILMS,id));
            String json = fetcher.getStringContent(filmURL,"GET",AppPreferences.getInstance(mContext).getToken(),null);

            // убрать
            json = PseudoApiPokaDanilaNeHotchetZapuskatServer_getFilmJSON(id);

            if(json == null)
                return null;
            Film film = Film.getFilmFromJson(json);
            return film;
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }

        return null;
    }

    public Film[] getFavoriteFilms() {
        URL filmURL = null;
        try {
            filmURL = new URL(String.format("%s/%s/%s",url,API,FAVORITES));
            String json = fetcher.getStringContent(filmURL,"GET",AppPreferences.getInstance(mContext).getToken(),null);
            if(json==null)
                return null;
            return Film.getFilmsFromJson(json);
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }
    public Film[] addFavoriteFilm(int filmId){
        URL url = null;
        try {
            url = new URL(String.format("%s/%s/%s", this.url,API,FAVORITES)); // Составление URL для запроса
            String data = "{\"film_id\":\""+filmId+"\"}";
            String json = fetcher.getStringContent(url,"POST",AppPreferences.getInstance(mContext).getToken(),data);
            if(json == null)
                return null;
            Film[] films = Film.getFilmsFromJson(json);
            return films;
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }
    public Film[] deleteFavoriteFilm(int filmId){
        URL url = null;
        try {
            url = new URL(String.format("%s/%s/%s", this.url,API,FAVORITES)); // Составление URL для запроса
            String data = "{\"film_id\":\""+filmId+"\"}";
            String json = fetcher.getStringContent(url,"DELETE",AppPreferences.getInstance(mContext).getToken(),data);
            if(json == null)
                return null;
            Film[] films = Film.getFilmsFromJson(json);
            return films;
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }
    public void refreshUserInfo(String login, String password, String name){
        URL url = null;
        try {
            url = new URL(String.format("%s/%s/%s", this.url,API,ME)); // Составление URL для запроса
            String data = "{";
            if(login!=null)
                data += "\"login\":\""+login;
            if(password!=null){
                if(!data.equals("{")) data+=",";
                data+="\"password\":\""+password;
            }
            if(name!=null){
                if(!data.equals("{")) data+=",";
                data+="\"name\":\""+name;
            }
            data+="}";
            String json = fetcher.getStringContent(url,"PUT",AppPreferences.getInstance(mContext).getToken(),data);
            return;
            /*
            if(res.success)
                AppPreferences.getInstance(mContext).setToken(res.token);
            return res;*/
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
    }

    public void sendComment(int filmId, String body){
        URL url = null;
        try {
            url = new URL(String.format("%s/%s/%s", this.url,API,COMMENTS)); // Составление URL для запроса
            String data = "{\"film_id\":\""+filmId+"\",\"body\":\""+body+"\"}";
            String json = fetcher.getStringContent(url,"POST",AppPreferences.getInstance(mContext).getToken(),data);
            return;
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
    }

    public Genre[] getGenres() {
        URL filmURL = null;
        try {
            filmURL = new URL(String.format("%s/%s/%s",url,API,GENRES));
            String json = fetcher.getStringContent(filmURL,"GET",AppPreferences.getInstance(mContext).getToken(),null);
            if(json==null)
                return null;
            return Genre.getGenresFromJson(json);
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }

    public Film[] getHistory() {
        URL filmURL = null;
        try {
            filmURL = new URL(String.format("%s/%s/%s",url,API,HISTORY));
            String json = fetcher.getStringContent(filmURL,"GET",AppPreferences.getInstance(mContext).getToken(),null);
            if(json==null)
                return null;
            return Film.getFilmsFromJson(json);
        }
        catch (MalformedURLException ex){
            Log.d(TAG, "getFilms: MalformedURLException Error");
        }
        return null;
    }

    public String PseudoApiPokaDanilaNeHotchetZapuskatServer_getFilmJSON(int id){
        if(id == 1)
            return "{\"id\":1,\"name\":\"Джанго освобождённый\",\"description\":\"Картина, повлиявшая на Тарантино при создании «Джанго освобождённого», — итальянский вестерн «Джанго». Заглавную роль там исполнил Франко Неро, который ненадолго появляется и у Тарантино. Мировая премьера состоялась 25 декабря 2012 года. Ещё до официальной премьеры некоторые критики, уже увидевшие новый проект Тарантино, включили его в собственные списки лучших фильмов года.\",\"genres\":[{\"id\":1,\"name\":\"Триллер\"},{\"id\":4,\"name\":\"Драма\"}],\"comments\":[]}";
        else
            return "{\"id\":2,\"name\":\"Токийский гуль\",\"description\":\"Манга в жанре тёмного фэнтези авторства Суи Исиды, выпускалась в период с сентября 2011 года по сентябрь 2014 года в журнале Weekly Young Jump издательства Shueisha, cобранные главы были изданы в виде 14 томов.\",\"genres\":[{\"id\":1,\"name\":\"Триллер\"},{\"id\":2,\"name\":\"Аниме\"}],\"comments\":[{\"id\":1,\"body\":\"Хорошое аниме! 1000-7 zxc dedinside\",\"user\":{\"id\":1,\"name\":\"ZXC DEDINSIDE 14 y.o\"}}]}";
    }
    public String PseudoApiPokaDanilaNeHotchetZapuskatServer_getFilms(){
        String json = "[{\"id\":1,\"name\":\"Джанго освобождённый\",\"description\":\"Картина, повлиявшая на Тарантино при создании «Джанго освобождённого», — итальянский вестерн «Джанго». Заглавную роль там исполнил Франко Неро, который ненадолго появляется и у Тарантино. Мировая премьера состоялась 25 декабря 2012 года. Ещё до официальной премьеры некоторые критики, уже увидевшие новый проект Тарантино, включили его в собственные списки лучших фильмов года.\",\"genres\":[{\"id\":1,\"name\":\"Триллер\"},{\"id\":4,\"name\":\"Драма\"}],\"comments\":[]},{\"id\":2,\"name\":\"Токийский гуль\",\"description\":\"Манга в жанре тёмного фэнтези авторства Суи Исиды, выпускалась в период с сентября 2011 года по сентябрь 2014 года в журнале Weekly Young Jump издательства Shueisha, cобранные главы были изданы в виде 14 томов.\",\"genres\":[{\"id\":1,\"name\":\"Триллер\"},{\"id\":2,\"name\":\"Аниме\"}],\"comments\":[{\"id\":1,\"body\":\"Хорошое аниме! 1000-7 zxc dedinside\",\"user\":{\"id\":1,\"name\":\"ZXC DEDINSIDE 14 y.o\"}}]}]";
        return json;
    }
    public int PseudoApiPokaDanilaNeHotchetZapuskatServer_getDrawableId(int id){
        if(id == 1)
            return R.drawable.django;
        else
            return R.drawable.tokgul;
    }
}
