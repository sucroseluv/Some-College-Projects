package com.morlag.trapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ShowInfoDialogFragment extends DialogFragment {
    String title;
    String text;
    public ShowInfoDialogFragment(String title, String text){
        this.title = title;
        this.text = text;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("Хорошо",null)
                .create();
    }
}
