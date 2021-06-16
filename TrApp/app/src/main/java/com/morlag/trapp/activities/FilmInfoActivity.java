package com.morlag.trapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.morlag.trapp.R;
import com.morlag.trapp.TemporarySingleton;
import com.morlag.trapp.adapters.SavedAdapter;
import com.morlag.trapp.model.Comment;
import com.morlag.trapp.model.Film;
import com.morlag.trapp.model.Genre;
import com.morlag.trapp.utils.ApiClient;

import org.w3c.dom.Text;

import java.util.Arrays;

public class FilmInfoActivity extends AppCompatActivity {
    TextView title;
    ImageView image;
    TextView genres;
    TextView rating;
    TextView description;
    LinearLayout commentsLayout;
    EditText comment;
    Button sendComment;
    Button btnToSaved;
    ProgressBar progressBar;
    Film film;

    ApiClient api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //setTheme(R.style.AppThemeWithActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_info);
        progressBar = findViewById(R.id.progressBar);
        int id = getIntent().getIntExtra("id",0);
        title = findViewById(R.id.title);
        genres = findViewById(R.id.genres);
        rating = findViewById(R.id.rating);
        image = findViewById(R.id.image);
        description = findViewById(R.id.description);
        commentsLayout = findViewById(R.id.comments);
        sendComment = findViewById(R.id.btnSendComment);
        btnToSaved = findViewById(R.id.btnToSaved);
        comment = findViewById(R.id.etComment);
        api = new ApiClient(FilmInfoActivity.this);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comment.getText().toString().equals(""))
                    new CommentTask().execute();
            }
        });
        btnToSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SavedTask().execute();
            }
        });
        new FilmTask().execute(id);
        new FavoriteTask().execute();
    }
    private void setFilmInfo(Film film){
        if(film==null)
            return;
        this.film = film;
        btnToSaved.setVisibility(View.VISIBLE);
        sendComment.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);
        title.setText(film.getName());
        this.genres.setText(film.getGenresStr());

        LayoutInflater inflater = LayoutInflater.from(this);
        Comment[] comments = film.getComments();
        for(Comment comment : comments){
            View view = inflater.inflate(R.layout.item_comment,commentsLayout,false);
            ((TextView)view.findViewById(R.id.user)).setText(comment.getUser().getName());
            ((TextView)view.findViewById(R.id.text)).setText(comment.getBody());
            commentsLayout.addView(view);
        }
        if(comments.length>0)
            ((TextView)findViewById(R.id.commentsLabel)).setVisibility(View.VISIBLE);

        rating.setText(String.format("Рейтинг: %.1f",film.getRating()));
        description.setText(film.getDescription());
        image.setImageResource(api.PseudoApiPokaDanilaNeHotchetZapuskatServer_getDrawableId(film.getId()));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.setTitle(film.getName());
    }
    class FilmTask extends AsyncTask<Integer,Void,Film>{

        @Override
        protected Film doInBackground(Integer... ids) {
            return api.getFilm(ids[0]);
        }

        @Override
        protected void onPostExecute(Film film) {
            setFilmInfo(film);
        }
    }
    class CommentTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            api.sendComment(film.getId(),comment.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(FilmInfoActivity.this,"Отзыв отправлен.",Toast.LENGTH_SHORT).show();
            comment.setText("");
            FilmInfoActivity.this.commentsLayout.removeAllViews();
            new FilmTask().execute(getIntent().getIntExtra("id",0));
        }
    }
    class SavedTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if(btnToSaved.getText().toString().equals("В избранное"))
                api.addFavoriteFilm(film.getId());
            else
                api.deleteFavoriteFilm(film.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(FilmInfoActivity.this,"Сохранено.",Toast.LENGTH_SHORT).show();
            if(btnToSaved.getText().toString().equals("В избранное"))
                btnToSaved.setText("Не в избранное");
            else
                btnToSaved.setText("В избранное");
        }
    }
    public class FavoriteTask extends AsyncTask<Void,Void,Film[]>{

        @Override
        protected Film[] doInBackground(Void... voids) {
            return new ApiClient(FilmInfoActivity.this).getFavoriteFilms();
        }

        @Override
        protected void onPostExecute(Film[] films) {
            int id = getIntent().getIntExtra("id",0);
            for(Film f : films){
                if(f.getId() == id){
                    btnToSaved.setText("Не в избранное");
                    break;
                }
            }
        }
    }
}
