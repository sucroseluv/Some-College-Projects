package com.morlag.bankoat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
            AppPreferences.setToken(this,"zfdHNDtgA4fQNsnUBeHMavC2X2YsLQaMSYY1ErHsVQj8ioRBm9oNRySaLZburogxpX51tYVgs5cCMuGksoB25MpwJI7T5eC6Ouk8OxQPY09G6bM64ltSZRnymdS0XQ5NTRYwb1yHH4O4NvIrtRoXxE");
        else
            AppPreferences.setToken(this,"osdgh93w4jg9834hgwheg9n2439gb948gh93wjfg0s44eg343");
        */
        AppPreferences.setServer(this, getResources().getString(R.string.server_ip));

        if(AppPreferences.getToken(this) != null && !AppPreferences.getToken(this).equals("")){
            instanceApplication();
        }
        else{
            instanceAuthentication();
        }

    }
    public void instanceApplication(){
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,R.id.fragment);
        //AppBarConfiguration configuration = new AppBarConfiguration.Builder(new HashSet<Integer>(R.layout.fragment_home,R.layout.fragment_second)).build();
        //NavigationUI.setupActionBarWithNavController(this,navController);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
    public void instanceAuthentication(){
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_frame);
        if(f==null)
            fm.beginTransaction()
                .add(R.id.fragment_frame,AuthenticationFragment.newInstance())
                .commit();

    }
}