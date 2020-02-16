package com.example.foodex.utils;

import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
    public static void strictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static void displayImage(ImageView imageView, String imageUrl) {
        strictMode();
        URL url = null;
        try {
            url = new URL(imageUrl);
            imageView.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
