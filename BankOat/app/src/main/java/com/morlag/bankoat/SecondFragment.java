package com.morlag.bankoat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;

import java.security.Permissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    BarcodeView barcodeView;


    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
    }

    View myview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_second, container, false);
        barcodeView = myview.findViewById(R.id.barcodeView);




        return myview;
    }

    @Override
    public void onResume() {
        super.onResume();
        //barcodeView = (BarcodeView)myview.findViewById(R.id.barcodeView);
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED)
        {
            barcodeView.resume();
            barcodeView.decodeSingle(new BarcodeCallback() {
                @Override
                public void barcodeResult(BarcodeResult result) {
                    Intent i = AcceptTransactionActivity.getIntent(getActivity(),result.getText());
                    startActivity(i);
                    //Toast.makeText(getActivity(),result.getText(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            requestPermissions(new String[]{Manifest.permission.CAMERA},0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults != null){
            barcodeView.resume();
            barcodeView.decodeSingle(new BarcodeCallback() {
                @Override
                public void barcodeResult(BarcodeResult result) {
                    Toast.makeText(getActivity(),result.getText(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




}