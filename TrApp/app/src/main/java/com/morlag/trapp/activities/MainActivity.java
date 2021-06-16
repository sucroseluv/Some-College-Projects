package com.morlag.trapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.morlag.trapp.R;
import com.morlag.trapp.fragments.ShowInfoDialogFragment;
import com.morlag.trapp.model.Login;
import com.morlag.trapp.utils.ApiClient;
import com.morlag.trapp.utils.AppPreferences;

public class MainActivity extends AppCompatActivity {
    Button bLogin;
    EditText login;
    EditText password;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppPreferences.getInstance(MainActivity.this).setCurrentServer("http://5bbfab05979f.ngrok.io");
        String token = AppPreferences.getInstance(this).getToken();
        if(token==null || token.equals("") || token.equals("Server is empty")){
            setContentView(R.layout.activity_login);
            bLogin = findViewById(R.id.btnRegister);
            login = findViewById(R.id.editLogin);
            password = findViewById(R.id.editPassword);

            bLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new LoginTask().execute(login.getText().toString(),password.getText().toString());
                }
            });
            register = findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(i);
                }
            });

            initLogged();
        } else {
            new IsValidTokenTask().execute();
        }
    }
    void initLogged(){
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        NavController controller = Navigation.findNavController(MainActivity.this, R.id.navigationFragment);
        NavigationUI.setupWithNavController(navView, controller);
    }

    class LoginTask extends AsyncTask<String,Void,Login> {

        @Override
        protected Login doInBackground(String... data) {
            return new ApiClient(MainActivity.this).login(data[0],data[1]);
        }

        @Override
        protected void onPostExecute(Login login) {
            if(login != null && login.success) {
                Toast.makeText(MainActivity.this,"Success " + login.token, Toast.LENGTH_SHORT).show();
                AppPreferences.getInstance(MainActivity.this).setToken(login.token);
                initLogged();
            }
            else
                new ShowInfoDialogFragment("Ошибка авторизации","Проверьте правильность логина или пароля.").show(getSupportFragmentManager(),"tag");

        }
    }
    class IsValidTokenTask extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... data) {
            return new ApiClient(MainActivity.this).tokenIsValid();
        }

        @Override
        protected void onPostExecute(Boolean isLogin) {
            if(isLogin) {
                initLogged();
            }
            else{
                AppPreferences.getInstance(MainActivity.this).setToken(null);
                recreate();
            }
        }
    }
}