package com.morlag.trapp;

import com.morlag.trapp.model.Film;

import java.util.ArrayList;
import java.util.List;

public class TemporarySingleton {
    private TemporarySingleton(){
        mFilmList = new ArrayList<>();
    }

    private static TemporarySingleton impl;
    public static TemporarySingleton getImpl(){
        if(impl==null)
            impl = new TemporarySingleton();
        return impl;
    }
    List<Film> mFilmList;
    public void addFilm(Film film){
        mFilmList.add(film);
    }
    public Film getFilmById(int id){
        for(Film f : mFilmList){
            if(f.getId()==id)
                return f;
        }
        return null;
    }
    public List<Film> getFilms(){
        return mFilmList;
    }
}
