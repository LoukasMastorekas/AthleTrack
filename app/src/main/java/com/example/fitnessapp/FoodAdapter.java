package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> implements Filterable {

    private Context context;
    private List<Food> foodList;
    private List<Food> foodListFull;
    private ActivityResultLauncher<Intent> foodDetailLauncher;

    public FoodAdapter(Context context, List<Food> foodList, ActivityResultLauncher<Intent> launcher) {
        this.context = context;
        this.foodList = foodList;
        this.foodListFull = new ArrayList<>(foodList);
        this.foodDetailLauncher = launcher;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.foodNameTextView.setText(food.getName());
        holder.caloriesTextView.setText(String.valueOf((int)food.getCaloriesper100()) + " calories");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodDetailActivity.class);
            intent.putExtra("food_name", food.getName());
            intent.putExtra("calories", food.getCaloriesper100());
            intent.putExtra("protein", food.getProteinper100());
            intent.putExtra("fat", food.getFatper100());
            intent.putExtra("carbs", food.getCarbsper100());
            foodDetailLauncher.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView caloriesTextView;  // Προσθήκη του TextView για τις θερμίδες

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.food_name_text);
            caloriesTextView = itemView.findViewById(R.id.Calories_text);  // Αντιστοιχούμε το νέο TextView για θερμίδες
        }
    }

    // Search φίλτρο
    @Override
    public Filter getFilter() {
        return foodFilter;
    }

    private Filter foodFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Food> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(foodListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Food item : foodListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            foodList.clear();
            foodList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
