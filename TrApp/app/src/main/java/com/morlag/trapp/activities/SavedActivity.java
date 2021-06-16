package com.morlag.trapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.morlag.trapp.R;
import com.morlag.trapp.adapters.SavedAdapter;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.utils.ApiClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SavedActivity extends AppCompatActivity {
    RecyclerView recycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeWithActionBar);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Сохраненные фильмы");
        setContentView(R.layout.activity_saved);
        recycle = findViewById(R.id.recycle);
        recycle.setLayoutManager(new GridLayoutManager(this,2));
        new FavoriteTask().execute();
    }

    public class FavoriteTask extends AsyncTask<Void,Void,Film[]>{

        @Override
        protected Film[] doInBackground(Void... voids) {
            return new ApiClient(SavedActivity.this).getFavoriteFilms();
        }

        @Override
        protected void onPostExecute(Film[] films) {
            recycle.setAdapter(new SavedAdapter(SavedActivity.this, Arrays.asList(films)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            new FavoriteTask().execute();
        }
    }
}