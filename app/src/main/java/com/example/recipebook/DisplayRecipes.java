package com.example.recipebook;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisplayRecipes extends AsyncTask<String, Void, String> {

    private static final String TAG = DisplayRecipes.class.getCanonicalName();

    private Context context;
    private ProgressBar loading;
    private ListView listView;
    private TextView numberResults;
    private String request;
    private TextView requestHolder;
    private ImageView image;
    private String instructions;

    private List<String> titles = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<String> origins = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private List<String> ids = new ArrayList<>();

    public DisplayRecipes(Context context, ProgressBar loading, ListView listView, TextView numberResults, String request, TextView requestHolder, ImageView image) {
        this.context = context;
        this.loading = loading;
        this.listView = listView;
        this.numberResults = numberResults;
        this.request = request;
        this.requestHolder = requestHolder;
        this.image = image;
    }

    protected void onPreExecute() {
        super.onPreExecute();

        //affiche l'icone de chargement
        this.loading.setVisibility(View.VISIBLE);
    }

    protected String doInBackground(String... urls) {
        String apiUrl = urls[0];

        //fait la requete vers l'api
        String response = Util.getRequest(this.context, apiUrl);

        try {
            JSONObject json = new JSONObject(response);
            JSONArray results = json.getJSONArray("meals");

            for (int i = 0; i < results.length(); i++) {
                JSONObject item = results.getJSONObject(i);

                //éléments basiques
                String id = item.getString("idMeal"); //l'id  de la recette
                String image = item.getString("strMealThumb"); //image  de la recette
                String title = item.getString("strMeal"); //titre de la recette
                String category = item.getString("strCategory"); //catégorie de la recette
                String origin = item.getString("strArea"); //pays de la recette

                ids.add(id);
                images.add(image);
                titles.add(title);
                categories.add(category);
                origins.add(origin);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected void onPostExecute(String response) {
        //la liste des recettes avec ses composants
        RecipeAdapter recipeAdapter = new RecipeAdapter(context, this.ids, this.images, this.titles, this.categories, this.origins);
        this.listView.setAdapter(recipeAdapter);

        //enlève l'icone de chargement
        this.loading.setVisibility(View.GONE);

        //affiche le nombre de résultats et la recherche
        this.numberResults.setVisibility(View.VISIBLE);
        this.numberResults.setText(String.valueOf(this.listView.getCount()));

        //affiche la requete faite par l'utilisateur (sa recherche)
        this.requestHolder.setText(this.request);

        //définit les listeners pour chaque élément de la liste
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < listView.getCount(); i++) {
                    if (position == i) {
                        Intent intent = new Intent(context, RecipeInfo.class);
                        intent.putExtra("id", ids.get(i));
                        context.startActivity(intent);
                    }
                }
            }
        });
    }
}