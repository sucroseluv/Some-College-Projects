package com.morlag.trapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.morlag.trapp.R;
import com.morlag.trapp.adapters.HistoryAdapter;
import com.morlag.trapp.fragments.FilterFragment;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.utils.ApiClient;

import java.util.Arrays;

public class SearchResult extends AppCompatActivity {
    RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeWithActionBar);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Результат запроса");
        setContentView(R.layout.activity_search_result);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        new FilterTask().execute();
    }
    class FilterTask extends AsyncTask<Void,Void, Film[]> {

        @Override
        protected Film[] doInBackground(Void... voids) {
            String n = SearchResult.this.getIntent().getStringExtra("name");
            int g = SearchResult.this.getIntent().getIntExtra("group",-1);
            float ratingF = SearchResult.this.getIntent().getFloatExtra("rating",-1);

            return new ApiClient(SearchResult.this).getFilms(n,g,ratingF,null,100,0);
        }

        @Override
        protected void onPostExecute(Film[] films) {
            super.onPostExecute(films);
            recycler.setAdapter(new HistoryAdapter(Arrays.asList(films)));
        }
    }
}