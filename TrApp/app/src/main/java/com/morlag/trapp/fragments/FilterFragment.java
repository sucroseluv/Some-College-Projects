package com.morlag.trapp.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.morlag.trapp.R;
import com.morlag.trapp.activities.SearchResult;
import com.morlag.trapp.adapters.SavedAdapter;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.model.Genre;
import com.morlag.trapp.utils.ApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FilterFragment extends Fragment {
    CheckBox lblGenre;
    NestedScrollView listGenres;
    EditText keyword;
    SeekBar seekBar;
    TextView rating;
    Button search;
    float ratingF = 3.5f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter,container,false);
        lblGenre = v.findViewById(R.id.lblGenre);
        listGenres = v.findViewById(R.id.listGenres);
        seekBar = v.findViewById(R.id.seekBar);
        rating = v.findViewById(R.id.rating);
        keyword = v.findViewById(R.id.editKeyword);
        search = v.findViewById(R.id.search);
        lblGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lblGenre.isChecked())
                    listGenres.setVisibility(View.VISIBLE);
                else
                    listGenres.setVisibility(View.GONE);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ratingF = (float)progress/100.0f;
                rating.setText(String.valueOf(ratingF));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = keyword.getText().toString();
                if(n.equals(""))
                    n = null;
                int g = -1;
                if(lblGenre.isChecked()){
                    RadioGroup rg = listGenres.findViewById(R.id.radGroup);
                    g = genreIds.get(rg.getCheckedRadioButtonId());
                }
                Intent i = new Intent(FilterFragment.this.getActivity(), SearchResult.class);
                i.putExtra("name",n);
                i.putExtra("group",g);
                i.putExtra("rating",ratingF);
                startActivity(i);
            }
        });
        new GenresTask().execute();

        return v;
    }
    HashMap<Integer,Integer> genreIds = new HashMap<>();
    class GenresTask extends AsyncTask<Void,Void,Genre[]>{

        @Override
        protected Genre[] doInBackground(Void... voids) {
            return new ApiClient(FilterFragment.this.getActivity()).getGenres();
        }

        @Override
        protected void onPostExecute(Genre[] genres) {
            RadioGroup rg = listGenres.findViewById(R.id.radGroup);
            for(Genre genre : genres){
                RadioButton rb = new RadioButton(FilterFragment.this.getActivity());
                rb.setText(genre.getName());
                rb.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                rg.addView(rb);
                genreIds.put(rb.getId(),genre.getId());
            }
        }
    }

}
