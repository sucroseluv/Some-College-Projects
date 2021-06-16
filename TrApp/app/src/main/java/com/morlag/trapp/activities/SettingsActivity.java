package com.morlag.trapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.morlag.trapp.R;
import com.morlag.trapp.utils.ApiClient;

public class SettingsActivity extends AppCompatActivity {
    Button button;
    EditText login;
    EditText nickname;
    EditText pass1;
    EditText pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeWithActionBar);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Настройки");
        setContentView(R.layout.activity_settings);
        login = findViewById(R.id.editLogin);
        nickname = findViewById(R.id.editNickname);
        pass1 = findViewById(R.id.editPassword);
        pass2 = findViewById(R.id.editPasswordAgain);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l = login.getText().toString();
                if(l.equals(""))
                    l = null;
                String n = nickname.getText().toString();
                if(n.equals(""))
                    n = null;
                if(!pass1.getText().toString().equals(pass2.getText().toString())) {
                    Toast.makeText(SettingsActivity.this,"Пароли не совпадают",Toast.LENGTH_SHORT).show();
                    return;
                }
                String p = pass1.getText().toString();
                if(p.equals(""))
                    p = null;
                new ChangeInfo(l,p,n).execute();
            }
        });
    }

    class ChangeInfo extends AsyncTask<Void,Void,Void>{
        String l = null;
        String p = null;
        String n = null;

        public ChangeInfo(String login, String password, String name){
            l = login;
            p = password;
            n = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            new ApiClient(SettingsActivity.this).refreshUserInfo(l,p,n);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(SettingsActivity.this,"Данные изменены",Toast.LENGTH_SHORT).show();
        }
    }
}