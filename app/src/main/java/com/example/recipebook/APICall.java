//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class APICall extends AsyncTask<Void, Void, String> {
//
//    private HttpURLConnection huc;
//    private InputStream responseStream;
//
//    private static final API_URL = "";
//    private static final API_KEY = "";
//    private static final String TAG = IUTAsyncTask.class.getCanonicalName();
//
//    private Context context;
//    private ProgressBar loading;
//    private String input;
//    private TextView text;
//
//    public APICall(Context context, ProgressBar loading, String input, TextView text) {
//        this.context = context;
//        this.loading = loading;
//        this.input = input;
//        this.text = text;
//    }
//
//    protected void onPreExecute() {
//        super.onPreExecute();
//        this.loading.setVisibility(View.VISIBLE);
//    }
//
//    protected String doInBackground(Void... urls) {
//        String search = input;
//        // Do some validation here
//
//        try {
////            URL url = new URL(API_URL + "email=" + email + "&apiKey=" + API_KEY);
////            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
////            try {
////                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(huc.getInputStream()));
////                StringBuilder stringBuilder = new StringBuilder();
////
////                String line;
////                while ((line = bufferedReader.readLine()) != null) {
////                    stringBuilder.append(line).append("\n");
////                }
////                bufferedReader.close();
////
////                return stringBuilder.toString();
////            }
////            finally{
////                urlConnection.disconnect();
////            }
//
//            doSimpleGetRequest(this.context, urls);
//        }
//        catch(Exception e) {
//            Log.e("ERROR", e.getMessage(), e);
//            return null;
//        }
//    }
//
//    protected void onPostExecute(String response) {
//        if(response == null) {
//            response = "THERE WAS AN ERROR";
//        }
//        loading.setVisibility(View.GONE);
//        text.setText(response);
//    }
//
//    private InputStream doSimpleGetRequest(Context context, String url) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo ni = cm.getActiveNetworkInfo();
//
//        if (ni != null && ni.isConnected()) {
//            Uri uri = Uri.parse(url).buildUpon().build();
//
//            try {
//                java.net.URL requestURL = new URL(uri.toString());
//
//                huc = (HttpURLConnection) requestURL.openConnection();
//                huc.setRequestMethod("GET");
//                huc.connect();
//
//                responseStream = huc.getInputStream();
//
//                return responseStream;
//            } catch(IOException e) {
//                Log.e(TAG, "Error while connecting to " + url, e);
//            }
//        } else {
//            Log.e(TAG, "Error ");
//        }
//
//        return null;
//    }
//}
