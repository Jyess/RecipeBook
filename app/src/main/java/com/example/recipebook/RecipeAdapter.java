package com.example.recipebook;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> title;
    private List<String> category;
    private List<String> origin;

    public RecipeAdapter(@NonNull Context context, List<String> title, List<String> category, List<String> origin) {
        super(context, R.layout.recipe_item, R.id.title, title);
        this.context = context;
        this.title = title;
        this.category = category;
        this.origin = origin;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = layoutInflater.inflate(R.layout.recipe_item, parent, false);

        TextView title = item.findViewById(R.id.title);
        TextView category = item.findViewById(R.id.category);
        TextView origin = item.findViewById(R.id.origin);

        title.setText(this.title.get(position));
        category.setText(this.category.get(position));
        origin.setText(this.origin.get(position));

        setBackgroundColor(category);

        return item;
    }

    /**
     * Modifie la couleur de la catégorie en fonction de son nom.
     * @param category      le textview category
     */
    private void setBackgroundColor(TextView category) {
        GradientDrawable d = (GradientDrawable) category.getBackground().mutate();
        String categoryName = (String) category.getText();
        Resources resources = this.context.getResources();

        if (categoryName.equals("Beef") || categoryName.equals("Chicken") || categoryName.equals("Goat") || categoryName.equals("Lamb")  || categoryName.equals("Pork")) {
            d.setColor(resources.getColor(R.color.meat));
        } else if (categoryName.equals("Breakfast") || categoryName.equals("Dessert") || categoryName.equals("Side") || categoryName.equals("Starter")) {
            d.setColor(resources.getColor(R.color.meal));
        } else if (categoryName.equals("Miscellaneous")) {
            d.setColor(resources.getColor(R.color.misc));
        } else if (categoryName.equals("Seafood")) {
            d.setColor(resources.getColor(R.color.fish));
        } else if (categoryName.equals("Vegan") || categoryName.equals("Vegetarian")) {
            d.setColor(resources.getColor(R.color.green));
        } else if (categoryName.equals("Pasta")) {
            d.setColor(resources.getColor(R.color.pasta));
        }
    }
}
