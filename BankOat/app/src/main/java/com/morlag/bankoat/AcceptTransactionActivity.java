package com.morlag.bankoat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AcceptTransactionActivity extends FormedFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return AcceptTransactionFragment.newInstance();
    }

    public static Intent getIntent(Context context,String transactionCode){
        Intent i = new Intent(context,AcceptTransactionActivity.class);
        i.putExtra("code",transactionCode);

        return i;
    }
}