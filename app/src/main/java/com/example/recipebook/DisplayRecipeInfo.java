package com.example.recipebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayRecipeInfo extends AsyncTask<String, Void, String> {

    private static final String TAG = DisplayRecipeInfo.class.getCanonicalName();

    private Context context;
    private ImageView image;
    private TextView title;
    private TextView origin;
    private TextView category;
    private LinearLayout ingredientsList;
    private TextView instructions;
    private ImageView videoInstructions;

    private String imageText;
    private String titleText;
    private String categoryText;
    private String originText;
    private String instructionsText;
    private String youtubeLink;

    private List<String> ingredients = new ArrayList<>();
    private List<String> measures = new ArrayList<>();

    public DisplayRecipeInfo(Context context, ImageView image, TextView title, TextView origin, TextView category, LinearLayout ingredientsList, TextView instructions, ImageView videoInstructions) {
        this.context = context;
        this.image = image;
        this.title = title;
        this.origin = origin;
        this.category = category;
        this.ingredientsList = ingredientsList;
        this.instructions = instructions;
        this.videoInstructions = videoInstructions;
    }

    protected void onPreExecute() {
        super.onPreExecute();
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

                imageText = item.getString("strMealThumb"); //image  de la recette
                titleText = item.getString("strMeal"); //titre de la recette
                categoryText = item.getString("strCategory"); //catégorie de la recette
                originText = item.getString("strArea"); //pays de la recette
                instructionsText = item.getString("strInstructions"); //instructions de la recette
                youtubeLink = item.getString("strYoutube"); //instructions vidéo sur Youtube

                for (int j = 1; j < 21; j++) {
                    String ingredient = item.getString("strIngredient" + j);
                    String measure = item.getString("strMeasure" + j);

                    //quand il n'y a pas d'autres ingrédients, retourne parfois un string "null" ou du vide
                    //certains ingrédients n'ont pas de mesures donc on ajoute quand meme le vide retourné
                    if (!ingredient.equals("null") && ingredient.length() > 0) {
                        ingredients.add(ingredient);
                        measures.add(measure);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected void onPostExecute(String response) {

        new LoadImage(this.image).execute(imageText);
        this.title.setText(titleText);
        this.origin.setText(originText);
        this.category.setText(categoryText);
        Util.setBackgroundColor(this.category, this.context);
        this.instructions.setText(instructionsText);
//        this.videoInstructions.setMovementMethod(LinkMovementMethod.getInstance());
//        this.videoInstructions.setText(youtubeLink);

        this.videoInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (youtubeLink.length() > 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
                    v.getContext().startActivity(intent);
                } else {
                    Toast.makeText(v.getContext(), "No video instructions available", Toast.LENGTH_LONG).show();
                }
            }
        });

        for (int i = 0; i < ingredients.size(); i++) {
            //une ligne qui contiendra l'ingrédient et sa mesure
            TableRow row = new TableRow(this.context);

            //les textes ingrédient et mesure
            TextView ingredientText = new TextView(this.context);
            TextView measureText = new TextView(this.context);

            ingredientText.setText(ingredients.get(i));
            measureText.setText(measures.get(i));

            //change l'affichage en 50/50
            TableRow.LayoutParams param = new TableRow.LayoutParams();
            param.weight = 1f;
            param.width = 0;
            param.setMargins(0, 10, 0, 10);
            ingredientText.setLayoutParams(param);
            measureText.setLayoutParams(param);

            //ajoute les textes à la ligne
            row.addView(ingredientText);
            row.addView(measureText);

            //ajoute à la ligne à la table
            this.ingredientsList.addView(row);
        }
    }
}