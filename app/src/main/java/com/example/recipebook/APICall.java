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

public class APICall extends AsyncTask<String, Void, String> {

    private static final String TAG = APICall.class.getCanonicalName();

    private HttpURLConnection httpURLConnection;
    private InputStream responseStream;

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
    private List<String> ingredients = new ArrayList<>();
    private List<String> measures = new ArrayList<>();

    public APICall(Context context, ProgressBar loading, ListView listView, TextView numberResults, String request, TextView requestHolder, ImageView image) {
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
        String source = urls[1];

        //fait la requete vers l'api
        String response = doSimpleGetRequest(this.context, apiUrl);

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

                //informations sur une recette
                if (source.equals("info")) {
                    this.instructions = item.getString("strInstructions"); //instructions de la recette

                    for (int j = 1; j < 21; j++) {
                        String ingredient = item.getString("strIngredient" + j);
                        String measure = item.getString("strMeasure" + j);

                        if (ingredient.length() > 0) {
                            ingredients.add(ingredient);
                        }

                        if (measure.length() > 0) {
                            measures.add(measure);
                        }
                    }
                }

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
        RecipeAdapter sa = new RecipeAdapter(context, this.ids, this.images, this.titles, this.categories, this.origins);
        this.listView.setAdapter(sa);

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

    /**
     * Fait une requête GET vers un serveur.
     * @param context   le contexte de l'appelant
     * @param url       l'url vers lequel on effectue la requête get
     * @return          la réponse du serveur
     */
    private String doSimpleGetRequest(Context context, String url) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Uri uri = Uri.parse(url).buildUpon().build();

            try {
                java.net.URL requestURL = new URL(uri.toString());

                httpURLConnection = (HttpURLConnection) requestURL.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                responseStream = httpURLConnection.getInputStream();

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
}