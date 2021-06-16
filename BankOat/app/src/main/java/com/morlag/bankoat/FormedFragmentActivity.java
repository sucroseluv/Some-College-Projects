package com.morlag.bankoat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public abstract class FormedFragmentActivity extends AppCompatActivity {
    protected abstract Fragment getFragment();

    protected int getLayoutId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_frame);
        if(fragment == null){
            fragment = getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_frame,fragment)
                    .commit();
        }

    }
}
