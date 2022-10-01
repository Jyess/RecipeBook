package com.example.recipebook;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Util {

    private static final String TAG = Util.class.getCanonicalName();

    /**
     * Fait une requête GET vers un serveur.
     * @param context   le contexte de l'appelant
     * @param url       l'url vers lequel on effectue la requête get
     * @return          la réponse du serveur
     */
    public static String getRequest(Context context, String url) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Uri uri = Uri.parse(url).buildUpon().build();

            try {
                java.net.URL requestURL = new URL(uri.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) requestURL.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream responseStream = httpURLConnection.getInputStream();

                return convertStreamToString(responseStream);
            } catch(IOException e) {
                Log.e(TAG, "Error while connecting to " + url, e);
            }
        } else {
            Log.e(TAG, "Error ");
        }

        return "";
    }

    /**
     * Convertit un stream en une chaîne de caractères.
     * @param input     un stream
     * @return          une chaîne de caractères
     */
    private static String convertStreamToString(InputStream input) {
        Scanner s = new Scanner(input).useDelimiter("\n");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Modifie la couleur de la catégorie en fonction de son nom.
     * @param category      le textview category
     */
    public static void setBackgroundColor(TextView category, Context context) {
        GradientDrawable d = (GradientDrawable) category.getBackground().mutate();
        String categoryName = (String) category.getText();
        Resources resources = context.getResources();

        switch (categoryName) {
            case "Beef":
            case "Chicken":
            case "Goat":
            case "Lamb":
            case "Pork":
                d.setColor(resources.getColor(R.color.meat));
                break;
            case "Breakfast":
            case "Dessert":
            case "Side":
            case "Starter":
                d.setColor(resources.getColor(R.color.meal));
                break;
            case "Miscellaneous":
                d.setColor(resources.getColor(R.color.misc));
                break;
            case "Seafood":
                d.setColor(resources.getColor(R.color.fish));
                break;
            case "Vegan":
            case "Vegetarian":
                d.setColor(resources.getColor(R.color.green));
                break;
            case "Pasta":
                d.setColor(resources.getColor(R.color.pasta));
                break;
        }
    }
}
