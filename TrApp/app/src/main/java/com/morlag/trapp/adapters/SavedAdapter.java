package com.morlag.trapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.morlag.trapp.R;
import com.morlag.trapp.activities.FilmInfoActivity;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.utils.ApiClient;

import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<SavedHolder> {
    List<Film> films; // Список фильмов
    Context mContext;
    public SavedAdapter(Context context,List<Film> mfilms){
        films = mfilms;
        mContext = context;
    }
    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Метод, который должен вернуть готовый ViewHolder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_saved_film,parent,false);
        return new SavedHolder(v,mContext); // Конструктор SavedHolder
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, int position) {
        holder.bind(films.get(position)); // Установка актуальных значений во ViewHolder
    }

    @Override
    public int getItemCount() {
        return films.size();
    } // Нужно вернуть количество элементов в списке
}
class SavedHolder extends RecyclerView.ViewHolder{ // Класс, который будет отвечать за отображение Film
    TextView text;
    ImageView image;
    Context mContext;
    View itemView;
    public SavedHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.itemView = itemView;
        text = itemView.findViewById(R.id.title);
        image = itemView.findViewById(R.id.image);
        mContext = context;
    }
    public void bind(final Film f){ // Метод для установки значений из Film в виджеты на экране
        text.setText(f.getName());
        image.setImageResource(new ApiClient(mContext).PseudoApiPokaDanilaNeHotchetZapuskatServer_getDrawableId(f.getId()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, FilmInfoActivity.class);
                i.putExtra("id", f.getId());
                ((AppCompatActivity)mContext).startActivityForResult(i,1);
            }
        });
    }
}
