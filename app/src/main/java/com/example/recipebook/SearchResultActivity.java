package com.example.recipebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private ListView listView;
    private ProgressBar loading;
    private TextView category;
    private TextView numberResults;

    private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        Intent intent = getIntent();
        String searchRequest = intent.getStringExtra("query");

        URL = API_URL + searchRequest;

        loading = findViewById(R.id.loading);
        listView = findViewById(R.id.listView);
        category = findViewById(R.id.category);
        numberResults = findViewById(R.id.numberResults);

        new APICall(this, loading, listView, numberResults).execute(URL);
    }
}