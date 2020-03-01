package com.example.recipebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    private String inputUser = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View searchView = inflater.inflate(R.layout.search, container, false);

        Button searchBtn = searchView.findViewById(R.id.searchBtn);
        Button random = searchView.findViewById(R.id.random);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchView inputField = searchView.findViewById(R.id.inputRecipe);
                inputUser = inputField.getQuery().toString();

                if (inputUser.length() > 0) {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("query", inputUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), R.string.empty_query, Toast.LENGTH_LONG).show();
                }
            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RandomRecipe.class);
                startActivity(intent);
            }
        });

        return searchView;
    }
}
