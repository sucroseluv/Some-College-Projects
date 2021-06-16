package com.morlag.trapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.morlag.trapp.activities.FilmInfoActivity;
import com.morlag.trapp.R;
import com.morlag.trapp.interfaces.Filterable;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.utils.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class NoveltiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements Filterable {
    public static final String TAG = "SearchAdapter";
    Context mContext;
    List<Film> mFilms;
    List<Film> filtered;
    boolean isFilter;

    public NoveltiesAdapter(Context context, List<Film> films){
        mContext = context;
        mFilms = films;
        filtered = new ArrayList<>();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v;
        if(viewType == 0){
            v = inflater.inflate(R.layout.item_film_header,parent,false);
            return new ResultHeader(v,this);
        }
        else{
            v = inflater.inflate(R.layout.item_film,parent,false);
            return new NoveltiesResultHolder(v, mContext);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return 0;
        else return 1;
    }

    @Override
    public int getItemCount() {
        if(isFilter)
            return filtered.size()+1;
        return mFilms.size()+1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        List<Film> current = mFilms;
        if(isFilter)
            current = filtered;

        if(holder instanceof NoveltiesResultHolder)
            ((NoveltiesResultHolder) holder).bind(current.get(position-1));
        else if(holder instanceof ResultHeader)
            ((ResultHeader) holder).bind();
        else
            Log.e(TAG, "onBindViewHolder: instanceof Error");
    }

    @Override
    public void filter(String text) {
        if(text.equals("")){
            clearFilter();
            return;
        }

        filtered.clear();
        for (Film film : mFilms) {
            if(film.getName().toLowerCase().contains(text.toLowerCase()))
                filtered.add(film);
        }
        isFilter = true;
        notifyDataSetChanged();
    }

    @Override
    public void clearFilter() {
        isFilter = false;
        notifyDataSetChanged();
    }
}
class NoveltiesResultHolder extends RecyclerView.ViewHolder {
    int id = 0;
    ImageView image;
    TextView title;
    TextView genre;
    TextView rating;
    ApiClient mApiClient;
    public NoveltiesResultHolder(@NonNull final View itemView, final Context context) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        title = itemView.findViewById(R.id.title);
        genre = itemView.findViewById(R.id.genre);
        rating = itemView.findViewById(R.id.rating);
        mApiClient = new ApiClient(context);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FilmInfoActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
    }
    public void bind(Film film){
        image.setImageBitmap(mApiClient.getImage(film.getId()));
        id = film.getId();
        title.setText(film.getName());
        genre.setText(film.getGenresStr());
        rating.setText(String.format("Рейтинг: %.1f",film.getRating()));
    }
}
class ResultHeader extends RecyclerView.ViewHolder {
    Filterable mAdapter;
    TextView greeting;
    SearchView search;
    public ResultHeader(@NonNull View itemView, Filterable adapter){
        super(itemView);
        greeting = itemView.findViewById(R.id.greeting);
        search = itemView.findViewById(R.id.search);
        mAdapter = adapter;
    }
    public void bind(){
        greeting.setText("Привет, какой фильм ты хочешь найти?");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search.clearFocus();
                mAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });
        search.setQueryHint("Фильмы и сериалы");
    }
}