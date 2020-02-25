package com.example.recipebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/search.php?s=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        Intent intent = getIntent();
        String request = intent.getStringExtra("query");

        Log.e(TAG, "onCreate: " + request);

        String URL = API_URL + request;

        ProgressBar loading = findViewById(R.id.loading);
        ListView listView = findViewById(R.id.listView);
        TextView numberResults = findViewById(R.id.numberResults);

        new APICall(this, loading, listView, numberResults).execute(URL);
    }
}