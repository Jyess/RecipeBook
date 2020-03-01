package com.example.recipebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CategoryFragment extends Fragment {

    private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/list.php?c=list";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View categView = inflater.inflate(R.layout.fragment_category, container, false);

        ProgressBar loading = categView.findViewById(R.id.loading_categ);
        ListView list = categView.findViewById(R.id.categ_list);

        new DisplayCategories(getContext(), loading, list).execute(API_URL);

        return categView;
    }
}
