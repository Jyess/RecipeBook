package com.example.recipebook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplaySortedRecipes extends AsyncTask<String, Void, String> {

    private static final String TAG = DisplayRecipeInfo.class.getCanonicalName();

    private Context context;
    private ProgressBar loading;
    private ListView listView;
    private TextView categHolder;
    private String categName;

    private List<String> titles = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private List<String> ids = new ArrayList<>();

    public DisplaySortedRecipes(Context context, ProgressBar loading, ListView listView, TextView categHolder, String categName) {
        this.context = context;
        this.loading = loading;
        this.listView = listView;
        this.categHolder = categHolder;
        this.categName = categName;
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

                ids.add(id);
                images.add(image);
                titles.add(title);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected void onPostExecute(String response) {
        //la liste des recettes avec ses composants
        SortedRecipeAdapter sortedRecipeAdapter = new SortedRecipeAdapter(context, this.ids, this.images, this.titles);
        this.listView.setAdapter(sortedRecipeAdapter);

        //enlève l'icone de chargement
        this.loading.setVisibility(View.GONE);

        this.categHolder.setText(this.categName);

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