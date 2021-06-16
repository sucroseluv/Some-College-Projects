package com.morlag.trapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.morlag.trapp.R;
import com.morlag.trapp.activities.FilmInfoActivity;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.utils.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    List<Film> mFilms;
    public HistoryAdapter(List<Film> films){
        mFilms = films;
    }
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new HistoryHolder(v,parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.bind(mFilms.get(position));
    }

    @Override
    public int getItemCount() {
        return mFilms.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;
        TextView rating;
        TextView genres;
        ApiClient mApiClient;
        View iv;
        Context context;

        public HistoryHolder(@NonNull View itemView, Context context) {
            super(itemView);
            iv = itemView;
            this.context = context;
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
            genres = itemView.findViewById(R.id.genres);
            mApiClient = new ApiClient(context);
        }
        public void bind(final Film film){
            if(film==null)
                return;

            image.setImageBitmap(mApiClient.getImage(film.getId()));
            name.setText(film.getName());
            rating.setText(String.valueOf(film.getRating()));
            genres.setText(film.getGenresStr());
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, FilmInfoActivity.class);
                    i.putExtra("id",film.getId());
                    context.startActivity(i);
                }
            });
        }
    }
}
