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

public class RecipeAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> id;
    private List<String> image;
    private List<String> title;
    private List<String> category;
    private List<String> origin;

    public RecipeAdapter(@NonNull Context context, List<String> id, List<String> image, List<String> title, List<String> category, List<String> origin) {
        super(context, R.layout.recipe_item, R.id.title_list, title);
        this.context = context;
        this.id = id;
        this.image = image;
        this.title = title;
        this.category = category;
        this.origin = origin;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = layoutInflater.inflate(R.layout.recipe_item, parent, false);

        TextView id = item.findViewById(R.id.recipe_id);
        ImageView image = item.findViewById(R.id.imageView);
        TextView title = item.findViewById(R.id.title_list);
        TextView category = item.findViewById(R.id.category);
        TextView origin = item.findViewById(R.id.origin);

        id.setText(this.id.get(position));
        new LoadImage(image).execute(this.image.get(position));
        title.setText(this.title.get(position));
        category.setText(this.category.get(position));
        origin.setText(this.origin.get(position));

        Util.setBackgroundColor(category, this.context);

        return item;
    }
}
