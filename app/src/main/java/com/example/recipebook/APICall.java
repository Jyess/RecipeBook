package com.example.recipebook;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    private HttpURLConnection huc;
    private InputStream responseStream;

    private Context context;
    private ProgressBar loading;
    private ListView listView;
    private TextView numberResults;
    private String request;
    private TextView requestHolder;

    private List<String> titles = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<String> origins = new ArrayList<>();

    public APICall(Context context, ProgressBar loading, ListView listView, TextView numberResults, TextView requestHolder, String request) {
        this.context = context;
        this.loading = loading;
        this.listView = listView;
        this.numberResults = numberResults;
        this.requestHolder = requestHolder;
        this.request = request;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.numberResults.setVisibility(View.GONE);
        this.loading.setVisibility(View.VISIBLE);
    }

    protected String doInBackground(String... urls) {
        String response = doSimpleGetRequest(this.context, urls[0]);

        try {
            JSONObject json = new JSONObject(response);
            JSONArray results = json.getJSONArray("meals");

            for (int i = 0; i < results.length(); i++) {
                JSONObject item = results.getJSONObject(i);

                String title = item.getString("strMeal");
                String category = item.getString("strCategory");
                String origin = item.getString("strArea");

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
        //la liste
        RecipeAdapter sa = new RecipeAdapter(context, this.titles, this.categories, this.origins);
        this.listView.setAdapter(sa);

        //enlève l'icone de chargement
        this.loading.setVisibility(View.GONE);

        //affiche le nombre de résultats et la recherche
        this.numberResults.setVisibility(View.VISIBLE);
        this.numberResults.setText(String.valueOf(this.listView.getCount()));

        this.requestHolder.setText(this.request);

        //définit les listeners pour chaque élément de la liste
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < listView.getCount(); i++) {
                    if (position == i) {
                        Toast.makeText(context, "" + listView.getSelectedItem(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Fait une requête get vers un serveur.
     * @param context   le contexte de l'appelant
     * @param url       l'url vers lequel on effectue la requête get
     * @return          la réponse du serveur
     */
    private String doSimpleGetRequest(Context context, String url) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected()) {
            Uri uri = Uri.parse(url).buildUpon().build();

            try {
                java.net.URL requestURL = new URL(uri.toString());

                huc = (HttpURLConnection) requestURL.openConnection();
                huc.setRequestMethod("GET");
                huc.connect();

                responseStream = huc.getInputStream();

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