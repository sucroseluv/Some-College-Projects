package com.morlag.trapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.morlag.trapp.R;
import com.morlag.trapp.adapters.HistoryAdapter;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.utils.ApiClient;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeWithActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("История посещений");
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        new HistoryTask().execute();
    }

    class HistoryTask extends AsyncTask<Void,Void, Film[]>{

        @Override
        protected Film[] doInBackground(Void... voids) {
            return new ApiClient(HistoryActivity.this).getHistory();
        }

        @Override
        protected void onPostExecute(Film[] films) {
            recycler.setAdapter(new HistoryAdapter(Arrays.asList(films)));
        }
    }
}