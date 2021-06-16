package com.morlag.bankoat;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MoneyRequestFragment extends Fragment {

    private int type = 0;
    private EditText editSum = null;
    private ImageView image = null;
    private TextView labelUnderCode = null;
    private TextView label = null;

    public static MoneyRequestFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MoneyRequestFragment fragment = new MoneyRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getActivity().getIntent().getIntExtra(MoneyRequestActivity.TYPE,0);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.money_request_fragment,container,false);

        image = v.findViewById(R.id.image_qr_code);
        labelUnderCode = v.findViewById(R.id.label_undercode);
        label = v.findViewById(R.id.label_zapros_type);
        if(type == MoneyRequestActivity.TYPE_SEND){
            label.setText("Запрос на перевод");
            labelUnderCode.setText("Покажите тому, на чей счет хотите отправить деньги");
        }

        editSum = ((EditText)v.findViewById(R.id.edit_sum));
        Button b = v.findViewById(R.id.generate_request);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sum = Integer.parseInt(editSum.getText().toString());
                QrGeneratorTask qr = new QrGeneratorTask(sum);
                qr.execute(getActivity());
            }
        });

        return v;
    }
    class QrGeneratorTask extends AsyncTask<Context,Void, Bitmap>{
        private int mSum;
        private Context mContext;
        public QrGeneratorTask(int sum){
            mSum = sum;
        }

        @Override
        protected Bitmap doInBackground(Context... contexts) {
            mContext = contexts[0];
            String requestStr = null;
            if(type == MoneyRequestActivity.TYPE_ZAPROS)
                requestStr = new BankApiUser().requestReceiver(mContext,mSum);
            else
                requestStr = new BankApiUser().requestSender(mContext,mSum);
            if(requestStr==null)
                return null;

            Bitmap bitmap = null;
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.encodeBitmap(requestStr, BarcodeFormat.QR_CODE, 1024, 1024);
            } catch(Exception e) {

            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //image.setImageBitmap(bitmap);
            Glide.with(mContext)
                    .load(bitmap)
                    .into(image);
            editSum.setFocusable(false);
            labelUnderCode.setVisibility(View.VISIBLE);
        }
    }
}
