package com.morlag.trapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.morlag.trapp.R;
import com.morlag.trapp.fragments.ShowInfoDialogFragment;
import com.morlag.trapp.model.Login;
import com.morlag.trapp.utils.ApiClient;
import com.morlag.trapp.utils.AppPreferences;

public class RegisterActivity extends AppCompatActivity {
    EditText login;
    EditText nickname;
    EditText password;
    EditText password1;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        login = findViewById(R.id.editLogin);
        nickname = findViewById(R.id.editName);
        password = findViewById(R.id.editPassword);
        password1 = findViewById(R.id.editPassword1);
        register = findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l = login.getText().toString();
                if(l.equals("")){
                    Toast.makeText(RegisterActivity.this,"Введите логин",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(l.length()<5){
                    Toast.makeText(RegisterActivity.this,"Логин должен быть не менее 5 символов",Toast.LENGTH_SHORT).show();
                    return;
                }
                String n = nickname.getText().toString();
                if(n.equals("")){
                    Toast.makeText(RegisterActivity.this,"Введите никнейм",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(n.length()<3){
                    Toast.makeText(RegisterActivity.this,"Никнейм должен быть не менее 3 символов",Toast.LENGTH_SHORT).show();
                    return;
                }
                String p1 = password.getText().toString();
                if(p1.equals("")){
                    Toast.makeText(RegisterActivity.this,"Введите пароль",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.getText().toString().equals(password1.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"Пароли не совпадают",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length() < 8){
                    Toast.makeText(RegisterActivity.this,"Пароль должен быть не менее 8 символов",Toast.LENGTH_SHORT).show();
                    return;
                }

                new RegisterTask().execute(l,p1,n);
            }
        });
    }
    class RegisterTask extends AsyncTask<String,Void,Login>{
        @Override
        protected Login doInBackground(String... data) {
            return new ApiClient(RegisterActivity.this).register(data[0],data[1],data[2]);
        }

        @Override
        protected void onPostExecute(Login login) {
            if(login != null && login.success) {
                Toast.makeText(RegisterActivity.this,"Success " + login.token, Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                new ShowInfoDialogFragment("Регистарция не завершена","Ошибка соединения с сервером.").show(getSupportFragmentManager(),"tag");
        }
    }
}