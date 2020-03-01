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
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DisplayCategories extends AsyncTask<String, Void, String> {

    private static final String TAG = DisplayRecipes.class.getCanonicalName();

    private Context context;
    private ProgressBar loading;
    private ListView categList;

    private List<Map<String,String>> dataList = new ArrayList<>(0);

    public DisplayCategories(Context context, ProgressBar loading, ListView categList) {
        this.context = context;
        this.loading = loading;
        this.categList = categList;
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

            Map<String, String> dataItem;

            for (int i = 0; i < results.length(); i++) {
                JSONObject item = results.getJSONObject(i);

                String category = item.getString("strCategory"); //l'id  de la recette

                dataItem = new HashMap<>(0);
                dataItem.put("categ", category);

                dataList.add(dataItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected void onPostExecute(String response) {
        //enlève l'icone de chargement
        this.loading.setVisibility(View.GONE);

        SimpleAdapter adapter = new SimpleAdapter(
                this.context,
                dataList,
                R.layout.activity_basic_list_item,
                new String[]{"categ"},
                new int[]{R.id.item}
        );
        this.categList.setAdapter(adapter);

        //définit les listeners pour chaque élément de la liste
        this.categList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < categList.getCount(); i++) {
                    if (position == i) {
                        String category = dataList.get(i).get("categ");

                        Intent intent = new Intent(context, SortedRecipesActivity.class);
                        intent.putExtra("categ", category);
                        context.startActivity(intent);
                    }
                }
            }
        });
    }
}