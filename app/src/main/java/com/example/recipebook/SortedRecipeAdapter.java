package com.example.recipebook;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SortedRecipeAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> id;
    private List<String> image;
    private List<String> title;

    public SortedRecipeAdapter(@NonNull Context context, List<String> id, List<String> image, List<String> title) {
        super(context, R.layout.recipe_item, R.id.title_list, title);
        this.context = context;
        this.id = id;
        this.image = image;
        this.title = title;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = layoutInflater.inflate(R.layout.sorted_item, parent, false);

        TextView id = item.findViewById(R.id.recipe_id_sorted);
        ImageView image = item.findViewById(R.id.image_sorted);
        TextView title = item.findViewById(R.id.title_sorted);

        id.setText(this.id.get(position));
        new LoadImage(image).execute(this.image.get(position));
        title.setText(this.title.get(position));

        return item;
    }
}
