package com.example.recipebook;

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

    private static final String API_URL = "https://randomuser.me/api/?results=10";
    private static final String API_KEY = "abc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response : " + response);

                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray array = json.getJSONArray("results");

                    List<Map<String,String>> dataList = new ArrayList<>(0);

                    Map<String, String> dataItem;

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        JSONObject itemName = item.getJSONObject("name");

                        String first = itemName.getString("first");
                        String last = itemName.getString("last");

                        dataItem = new HashMap<>(0);
                        dataItem.put("first", first);
                        dataItem.put("last", last);

                        dataList.add(dataItem);

                        Log.i(TAG, "User : " + itemName.getString("first") + " " + itemName.getString("last"));
                    }

                    SimpleAdapter sa = new SimpleAdapter(SearchResultActivity.this,
                            dataList,
                            R.layout.recipe_item,
                            new String[]{"first","last"},
                            new int[]{R.id.first, R.id.last}
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
