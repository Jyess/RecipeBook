package com.example.recipebook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final String URL = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
        View viewHome = inflater.inflate(R.layout.search_result, container, false);

        ProgressBar loading = viewHome.findViewById(R.id.loading);
        ListView listView = viewHome.findViewById(R.id.listView);
        TextView numberResults = viewHome.findViewById(R.id.numberResults);

        new APICall(viewHome.getContext(), loading, listView, numberResults).execute(URL);

        return viewHome;
    }
}
