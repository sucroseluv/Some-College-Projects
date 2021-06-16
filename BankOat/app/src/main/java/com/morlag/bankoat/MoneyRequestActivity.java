package com.morlag.bankoat;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MoneyRequestActivity extends FormedFragmentActivity {
    public static final String TYPE = "type";
    public static final int TYPE_ZAPROS = 0;
    public static final int TYPE_SEND = 1;

    @Override
    protected Fragment getFragment() {
        return MoneyRequestFragment.newInstance();
    }

    public static Intent getIntent(Context context, boolean isZapros) {
        Intent i = new Intent(context,MoneyRequestActivity.class);
        if(isZapros)
            i.putExtra("type",TYPE_ZAPROS);
        else
            i.putExtra("type",TYPE_SEND);


        return i;
    }
}
