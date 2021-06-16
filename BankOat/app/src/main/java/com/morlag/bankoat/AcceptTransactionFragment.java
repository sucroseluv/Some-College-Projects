package com.morlag.bankoat;

import android.content.Context;
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

public class AcceptTransactionFragment extends Fragment {
    EditText nameInfo;
    EditText phoneInfo;
    EditText sumInfo;
    TextView complete;
    TextView title;
    TextView labelName;
    Button btnTransaction;

    public static AcceptTransactionFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AcceptTransactionFragment fragment = new AcceptTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_accept_transaction,container,false);

        String code = getActivity().getIntent().getStringExtra("code");

        title = v.findViewById(R.id.title);
        labelName = v.findViewById(R.id.label_name);

        nameInfo = v.findViewById(R.id.name_info);
        phoneInfo = v.findViewById(R.id.phone_info);
        sumInfo = v.findViewById(R.id.sum_info);
        complete = v.findViewById(R.id.transaction_complete);

        btnTransaction = v.findViewById(R.id.btn_accept_transaction);
        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AcceptTransactionTask(getActivity()).execute(code);
            }
        });

        new ResultTask(getActivity()).execute(code);

        return v;
    }

    class ResultTask extends AsyncTask<String,Void,Transaction> {
        Context mContext;
        public ResultTask(Context context){
            mContext = context;
        }

        @Override
        protected Transaction doInBackground(String... code) {
            return new BankApiUser().getTransaction(mContext,code[0]);
        }

        @Override
        protected void onPostExecute(Transaction transaction) {
            String name = new StringBuilder()
                    .append(transaction.getFirstname())
                    .append(" ")
                    .append(transaction.getMiddlename())
                    .append(" ")
                    .append(transaction.getLastname() == null ? "" : transaction.getLastname().substring(0,0))
                    .toString();

            if(transaction.getType().equals("receive")){
                title.setText("Получить деньги");
                labelName.setText("От кого:");
                btnTransaction.setText("Получить");
            }
            else{
                title.setText("Отправить деньги");
                labelName.setText("Кому:");
                btnTransaction.setText("Отправить");
            }

            nameInfo.setText(name);
            phoneInfo.setText(transaction.getPhone());
            sumInfo.setText(transaction.getAmount());
        }
    }
    class AcceptTransactionTask extends AsyncTask<String,Void,Boolean> {
        Context mContext;
        public AcceptTransactionTask(Context context){
            mContext = context;
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            return new BankApiUser().executeTransaction(mContext,strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                Toast.makeText(getActivity(),"Транзакция совершена",Toast.LENGTH_SHORT);
                complete.setVisibility(View.VISIBLE);
            }
        }
    }
}
