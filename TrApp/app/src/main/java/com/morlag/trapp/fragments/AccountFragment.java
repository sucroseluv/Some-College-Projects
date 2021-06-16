package com.morlag.trapp.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.morlag.trapp.R;
import com.morlag.trapp.activities.HistoryActivity;
import com.morlag.trapp.activities.MainActivity;
import com.morlag.trapp.activities.SavedActivity;
import com.morlag.trapp.activities.SettingsActivity;
import com.morlag.trapp.model.Login;
import com.morlag.trapp.model.User;
import com.morlag.trapp.utils.ApiClient;
import com.morlag.trapp.utils.AppPreferences;

public class AccountFragment extends Fragment {
    Button mButton;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account,container,false);
        textView = v.findViewById(R.id.textView);
        v.findViewById(R.id.buttonSaved).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SavedActivity.class));
            }
        });
        v.findViewById(R.id.buttonSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
        v.findViewById(R.id.buttonHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });
        v.findViewById(R.id.buttonExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Login exit = new Login(false,null);
                ConfirmDialogFragment fragment = new ConfirmDialogFragment("Выход", "Вы действительно хотите выйти?",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new CloserTask().execute();
                            }
                        });
                fragment.show(getActivity().getSupportFragmentManager(),"exit");

            }
        });
        new IsValidTokenTask().execute();

        // убрать
        textView.setText("User 1");
        return v;
    }

    class CloserTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AppPreferences.getInstance(getActivity()).setToken("");
            getActivity().recreate();
        }
    }
    class IsValidTokenTask extends AsyncTask<Void,Void,User> {

        @Override
        protected User doInBackground(Void... data) {
            return new ApiClient(AccountFragment.this.getActivity()).getCurrentUser();
        }

        @Override
        protected void onPostExecute(User user) {
            if(user!=null){
                textView.setText(user.getName());
            }
        }
    }
}
