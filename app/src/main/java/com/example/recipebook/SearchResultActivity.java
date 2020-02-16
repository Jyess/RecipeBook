package com.example.recipebook;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        Intent intent = getIntent();
        String searchRequest = intent.getStringExtra("query");

        URL = API_URL + searchRequest;

        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray results = json.getJSONArray("meals");

                    List<Map<String, String>> dataList = new ArrayList<>(0);

                    Map<String, String> dataItem;

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject item = results.getJSONObject(i);

                        String title = item.getString("strMeal");

                        dataItem = new HashMap<>(0);
                        dataItem.put("title", title);

                        dataList.add(dataItem);
                    }

                    SimpleAdapter sa = new SimpleAdapter(SearchResultActivity.this,
                            dataList,
                            R.layout.recipe_item,
                            new String[]{"title"},
                            new int[]{R.id.title}
                    );

                    listView = findViewById(R.id.listView);
                    listView.setAdapter(sa);

                    findViewById(R.id.loading).setVisibility(View.GONE);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(SearchResultActivity.this, "test", Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "Error while parsing data : " + e);
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