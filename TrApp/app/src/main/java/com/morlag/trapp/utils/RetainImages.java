package com.morlag.trapp.utils;

import android.graphics.Bitmap;

import java.util.Dictionary;
import java.util.HashMap;

public class RetainImages {
    private static RetainImages inst;
    public static RetainImages getInstance(){
        if(inst==null)
            inst = new RetainImages();
        return inst;
    }
    private RetainImages(){
        savedImages = new HashMap<>();
    }
    HashMap<Integer, Bitmap> savedImages;
    public Bitmap getImage(int id){
        return savedImages.get(id);
    }
    public void setImage(int id, Bitmap bitmap){
        savedImages.put(id,bitmap);
    }
}
