package com.mystudycanada.shreehari.Model;

import android.graphics.Bitmap;

public interface IOnResourceReady {
    void OnResourceReady(Bitmap bitmap);

    void onLoadFailed();
}