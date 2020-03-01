package com.example.recipebook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        //un appel à l'API avec un paramètre 's' vide retourne une liste de recettes par défaut
        final String URL = "https://www.themealdb.com/api/json/v1/1/search.php?s=";

        //pour avoir la view search_result
        View viewHome = inflater.inflate(R.layout.search_result, container, false);

        ProgressBar loading = viewHome.findViewById(R.id.loading);
        ListView listView = viewHome.findViewById(R.id.listView);
        TextView numberResults = viewHome.findViewById(R.id.numberResults);
        TextView requestHolder = viewHome.findViewById(R.id.query);
        ImageView image = viewHome.findViewById(R.id.imageView);

        new DisplayRecipes(getContext(), loading, listView, numberResults, "", requestHolder, image).execute(URL);

        return viewHome;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options, menu);
    }
}
