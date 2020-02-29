package com.example.recipebook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

public class LoadImage extends AsyncTask<String, Void, Bitmap> {

    private ImageView image;

    public LoadImage(ImageView image) {
        this.image = image;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlImage = strings[0];
        Bitmap bitmap = null;

        try {
            InputStream inputStream = new URL(urlImage).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        this.image.setImageBitmap(bitmap);
    }
}
