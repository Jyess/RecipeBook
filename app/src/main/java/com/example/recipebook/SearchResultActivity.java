package com.example.recipebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private RequestQueue requestQueue;
    private ListView listView;

    private static final String API_URL = "https://api.spoonacular.com/recipes/search?query=";
    private static final String API_KEY = "0d1c9f44adfb48bb9941078b17913c00";
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        Intent intent = getIntent();
        String searchRequest = intent.getStringExtra("query");

        URL = API_URL + searchRequest + "&apiKey=" + API_KEY;
        Log.i(TAG, "URL : " + URL);

        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response : " + response);

                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray results = json.getJSONArray("results");

                    List<Map<String,String>> dataList = new ArrayList<>(0);

                    Map<String, String> dataItem;

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject item = results.getJSONObject(i);

                        String title = item.getString("title");
                        String time = item.getString("readyInMinutes");
                        String servings = item.getString("servings");

                        dataItem = new HashMap<>(0);
                        dataItem.put("title", title);
                        dataItem.put("time", time);
                        dataItem.put("servings", servings);

                        dataList.add(dataItem);

                        Log.i(TAG, "OK");
                    }

                    SimpleAdapter sa = new SimpleAdapter(SearchResultActivity.this,
                            dataList,
                            R.layout.recipe_item,
                            new String[] {
                                    "title",
                                    "time",
                                    "servings"
                                },
                            new int[] {
                                    R.id.title,
                                    R.id.time,
                                    R.id.servings
                                }
                    );

                    listView = findViewById(R.id.listView);
                    listView.setAdapter(sa);

                } catch(JSONException e) {
                    Log.e(TAG, "Error : " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error : " + error);
            }
        });

        this.requestQueue.add(stringRequest);

    }
}
