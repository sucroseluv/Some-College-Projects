package com.morlag.trapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmDialogFragment extends DialogFragment {
    String title;
    String text;
    DialogInterface.OnClickListener mListener;
    public ConfirmDialogFragment(String title, String text, DialogInterface.OnClickListener buttonListener){
        this.title = title;
        this.text = text;
        mListener = buttonListener;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("Да", mListener)
                .setNegativeButton("Отмена",null)
                .create();
    }
}
