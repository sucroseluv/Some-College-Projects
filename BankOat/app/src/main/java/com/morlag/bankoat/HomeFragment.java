package com.morlag.bankoat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String titleStart = "Добро пожаловать";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    User currentUser;
    ConstraintLayout head;
    View[] operationViews;
    LinearLayout leastLayout;
    TextView textShowMore;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserTask ut = new UserTask();
        ut.execute(getActivity());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //LinearLayout linearLayout = v.findViewById(R.id.least_layout);
        //for(int i = 0 ; i < 3; i++)linearLayout.addView(inflater.inflate(R.layout.item_spended,linearLayout,false));
        head = v.findViewById(R.id.head_frame);
        leastLayout = v.findViewById(R.id.least_layout);
        textShowMore = v.findViewById(R.id.text_show_more);
        textShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 3; i < operationViews.length; i++) {
                    if(operationViews[i] == null)
                        break;
                    leastLayout.addView(operationViews[i]);
                }
                textShowMore.setVisibility(View.INVISIBLE);
            }
        });
        Button request = v.findViewById(R.id.btn_request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = MoneyRequestActivity.getIntent(getActivity(),true);
                startActivity(i);
            }
        });
        Button over = v.findViewById(R.id.btn_send_over_qr);
        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = MoneyRequestActivity.getIntent(getActivity(),false);
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_actionbar_menu,menu);
        MenuItem itemView = menu.findItem(R.id.action_search);
        final SearchView searchView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(itemView);
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //searchView.clearFocus();

            }
        });
    }

    class UserTask extends AsyncTask<Context,Void,User> {

        Context c;
        @Override
        protected User doInBackground(Context... context) {
            c = context[0];
            return new BankApiUser().getUserInfo(context[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            currentUser = user;
            ImageView imageAvatar = head.findViewById(R.id.item_image);

            if(currentUser == null){
                AppPreferences.setToken(getActivity(),null);
                getActivity().recreate();
                return;
            }


            ((TextView)head.findViewById(R.id.head_user_title))
                    .setText(titleStart + (currentUser.getFirstname().equals("") ? "" : ", " + currentUser.getFirstname()));
            ((TextView)head.findViewById(R.id.head_user_balance)).setText(currentUser.getBalance().toString()+"₽");
            Glide.with(getActivity())
                    .load(BankApiUser.uriWithoutApiStart+currentUser.getPicture())
                    .placeholder(R.drawable.placeholder)
                    .into(imageAvatar);
        }
    }
    class OperationsTask extends AsyncTask<Context,Void,Operation[]> {

        Context c;
        @Override
        protected Operation[] doInBackground(Context... context) {
            c = context[0];
            return new BankApiUser().getOperations(context[0],10);
        }

        @Override
        protected void onPostExecute(Operation[] operations) {
            operationViews = new View[operations.length];
            Date now = Calendar.getInstance().getTime();
            for(int i = 0; i < operations.length; i++){
                View v = getLayoutInflater().inflate(R.layout.item_spended,leastLayout,false);
                ImageView image = v.findViewById(R.id.item_image);
                TextView name = v.findViewById(R.id.item_name);
                TextView sum = v.findViewById(R.id.item_sum);
                TextView date = v.findViewById(R.id.item_date);

                Glide.with(c)
                        .load(BankApiUser.uriWithoutApiStart + operations[i].picture)
                        .into(image);
                name.setText(operations[i].title);
                sum.setText(String.valueOf(operations[i].amount) + "₽");
                if(operations[i].date.getDate() == now.getDate())
                    date.setText((operations[i].date.getHours()<10 ? "0"+operations[i].date.getHours() : operations[i].date.getHours())+":"+(operations[i].date.getMinutes()<10 ? "0"+operations[i].date.getMinutes():operations[i].date.getMinutes()));
                else
                    date.setText((operations[i].date.getDay()<10?"0"+operations[i].date.getDay():operations[i].date.getDay())+"."
                            + ((operations[i].date.getMonth()+1)<10 ? "0"+(operations[i].date.getMonth()+1) : (operations[i].date.getMonth()+1)));
                operationViews[i] = v;
            }
            leastLayout.removeAllViews();
            for(int i = 0; i < 3; i++){
                leastLayout.addView(operationViews[i]);
            }
            if(operationViews[4] != null)
                textShowMore.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new UserTask().execute(getActivity());
        new OperationsTask().execute(getActivity());
    }
}