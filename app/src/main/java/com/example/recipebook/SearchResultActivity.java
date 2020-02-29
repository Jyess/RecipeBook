package com.example.recipebook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;


public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/search.php?s=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        Intent intent = getIntent();
        String request = intent.getStringExtra("query");

        String URL = API_URL + request;

        ProgressBar loading = findViewById(R.id.loading);
        ListView listView = findViewById(R.id.listView);
        TextView numberResults = findViewById(R.id.numberResults);
        TextView requestHolder = findViewById(R.id.query);
        ImageView image = findViewById(R.id.imageView);

        new APICall(this, loading, listView, numberResults, request, requestHolder, image).execute(URL, "search");
    }
}