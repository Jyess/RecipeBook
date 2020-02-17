package com.example.recipebook;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class APICall extends AsyncTask<String, String, List<Map<String, String>>> {

    private static final String TAG = APICall.class.getCanonicalName();

    private HttpURLConnection huc;
    private InputStream responseStream;

    private Context context;
    private ProgressBar loading;
    private ListView listView;
    private TextView category;

    public APICall(Context context, ProgressBar loading, ListView listView, TextView category) {
        this.context = context;
        this.loading = loading;
        this.listView = listView;
        this.category = category;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.loading.setVisibility(View.VISIBLE);
    }

    protected List<Map<String, String>> doInBackground(String... urls) {
        List<Map<String, String>> dataList = new ArrayList<>();
        String response = doSimpleGetRequest(this.context, urls[0]);

        try {
            JSONObject json = new JSONObject(response);
            JSONArray results = json.getJSONArray("meals");

            dataList = new ArrayList<>(0);

            Map<String, String> dataItem;

            for (int i = 0; i < results.length(); i++) {
                JSONObject item = results.getJSONObject(i);

                String title = item.getString("strMeal");
                String category = item.getString("strCategory");
                String origin = item.getString("strArea");

                dataItem = new HashMap<>(0);
                dataItem.put("title", title);
                dataItem.put("category", category);
                dataItem.put("origin", origin);

                dataList.add(dataItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    protected void onPostExecute(List<Map<String, String>> dataList) {
        SimpleAdapter sa = new SimpleAdapter(this.context,
                dataList,
                R.layout.recipe_item,
                new String[]{"title", "category", "origin"},
                new int[]{R.id.title, R.id.category, R.id.origin}
        );

        this.listView.setAdapter(sa);

        this.loading.setVisibility(View.GONE);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "test", Toast.LENGTH_LONG).show();
            }
        });

//        GradientDrawable d = (GradientDrawable) this.category.getBackground().mutate();
//        d.setColor(this.context.getResources().getColor(R.color.pasta));
    }

    private String doSimpleGetRequest(Context context, String url) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected()) {
            Uri uri = Uri.parse(url).buildUpon().build();

            try {
                java.net.URL requestURL = new URL(uri.toString());

                huc = (HttpURLConnection) requestURL.openConnection();
                huc.setRequestMethod("GET");
                huc.connect();

                responseStream = huc.getInputStream();

                return convertStreamToString(responseStream);
            } catch(IOException e) {
                Log.e(TAG, "Error while connecting to " + url, e);
            }
        } else {
            Log.e(TAG, "Error ");
        }

        return null;
    }

    public static String convertStreamToString(InputStream input) {
        Scanner s = new Scanner(input).useDelimiter("\n");
        return s.hasNext() ? s.next() : "";
    }
}