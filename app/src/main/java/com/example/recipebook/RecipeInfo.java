package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeInfo extends AppCompatActivity {

    public static final String API_URL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_info);

        //récupère l'id de la recette
        Intent intent = getIntent();
        String recipeId = intent.getStringExtra("id");

        //crée la requete vers l'api
        String request = API_URL + recipeId;

        ImageView image = findViewById(R.id.image_info);
        TextView title = findViewById(R.id.title_info);
        TextView country = findViewById(R.id.origin_info);
        TextView category = findViewById(R.id.category_info);
        LinearLayout list = findViewById(R.id.ingredients_list);
        TextView instructions = findViewById(R.id.instructions);

        new DisplayRecipeInfo(this, image, title, country, category, list, instructions).execute(request);
    }
}
