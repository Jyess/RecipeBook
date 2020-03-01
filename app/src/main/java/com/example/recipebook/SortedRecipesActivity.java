package com.example.recipebook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;


public class SortedRecipesActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/filter.php?c=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_result);

        Intent intent = getIntent();
        String request = intent.getStringExtra("categ");

        String URL = API_URL + request;

        ProgressBar loading = findViewById(R.id.loading_sorted);
        ListView listView = findViewById(R.id.sorted_list);
        TextView title = findViewById(R.id.title_sorted);
        TextView categHolder = findViewById(R.id.categ_name);

        new DisplaySortedRecipes(this, loading, listView, categHolder, request).execute(URL);
    }
}