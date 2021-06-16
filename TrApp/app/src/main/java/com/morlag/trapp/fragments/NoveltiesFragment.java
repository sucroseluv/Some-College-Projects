package com.morlag.trapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.morlag.trapp.R;
import com.morlag.trapp.adapters.NoveltiesAdapter;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.utils.ApiClient;

import java.util.Arrays;

public class NoveltiesFragment extends Fragment {
    RecyclerView mRecyclerView; // RecyclerView в который будем помещать данные
    NoveltiesAdapter mAdapter; // Адаптер, который необходим для работы RecyclerView
        // Мы тоже такой делали но в конце добавлю код

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerView = v.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new FilmFetcher().execute();    // Вызов асинхронного запроса
        return v;
    }

    // Специальный класс для асинхронного выполнения задач,
    // сетевой запрос является тяжелой задачей и её нужно вынести
    // из главного потока
    class FilmFetcher extends AsyncTask<Void,Void,Film[]>{

        @Override
        protected Film[] doInBackground(Void... voids) { // В этом методе операции выполняются асинхронно
            return new ApiClient(getActivity()).getFilms(); // тут происходит запрос к серверу и преобразование результата
        }

        @Override
        protected void onPostExecute(Film[] films) {    // В этом методе операции выполняются синхронно
            super.onPostExecute(films);
            if(films != null) {
                    // Создание адаптера на основе полученных фильмов и установка его в RecyclerView
                NoveltiesAdapter adapter = new NoveltiesAdapter(getActivity(), Arrays.asList(films));
                mRecyclerView.setAdapter(adapter);
            }
        }
    }
}