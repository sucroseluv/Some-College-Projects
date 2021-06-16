package com.morlag.bankoat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.pinball83.maskededittext.MaskedEditText;

public class AuthenticationFragment extends Fragment {
    int step = 1;

    MaskedEditText editPhone;
    TextView labelCode;
    EditText editCode;
    Button btn;

    String auth_token;
    String android_token;

    public static AuthenticationFragment newInstance() {

        Bundle args = new Bundle();

        AuthenticationFragment fragment = new AuthenticationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_auth,container,false);

        editPhone = v.findViewById(R.id.phone_info);
        labelCode = v.findViewById(R.id.label_code);
        editCode = v.findViewById(R.id.code_info);

        btn = v.findViewById(R.id.btn_auth);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(step == 1)
                    new StepOneTask().execute("7"+editPhone.getUnmaskedText());
                else if(step==2)
                    new StepTwoTask().execute("7"+editPhone.getUnmaskedText(),auth_token,editCode.getText().toString());

                //getActivity().recreate();
            }
        });

        return v;
    }

    class StepOneTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return new BankApiUser().loginStepOne(getActivity(),strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null) {
                auth_token = s;
                step = 2;
                labelCode.setVisibility(View.VISIBLE);
                editPhone.setFocusable(false);
                editCode.requestFocus();
                editCode.setVisibility(View.VISIBLE);
                btn.setText("Подтвердить");
            }
            else {
                Toast.makeText(getActivity(),"Неверный номер", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class StepTwoTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return new BankApiUser().loginStepTwo(getActivity(),strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null) {
                android_token = s;
                AppPreferences.setToken(getActivity(),android_token);
                getActivity().recreate();
            }
            else {
                Toast.makeText(getActivity(),"Неверный код", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
