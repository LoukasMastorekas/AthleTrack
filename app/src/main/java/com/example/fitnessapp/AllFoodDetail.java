package com.example.fitnessapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AllFoodDetail extends AppCompatActivity {
    private ListView foodListView;
    private ArrayList<String> foodNames;
    private ArrayAdapter<String> adapter;
    private int calories = 0;
    private int proteins = 0;
    private int fats = 0;
    private int carbs = 0;
    private ArrayList<Food> foodList;  // Κρατάμε και τη λίστα αντικειμένων

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_food_detail);

        foodListView = findViewById(R.id.food_list_view);

        foodList = (ArrayList<Food>) getIntent().getSerializableExtra("foodList");
        if (foodList != null) {
            foodNames = new ArrayList<>();
            for (Food food : foodList) {
                foodNames.add(food.getName() + " \n" + food.getCalories() + " Θερμίδες");
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodNames);
            foodListView.setAdapter(adapter);

            foodListView.setOnItemClickListener((parent, view, position, id) -> {
                new AlertDialog.Builder(AllFoodDetail.this)
                        .setTitle("Διαγραφή")
                        .setMessage("Θέλεις να διαγράψεις αυτό το φαγητό;")
                        .setPositiveButton("Ναι", (dialog, which) -> {
                            Food deletedFood = foodList.get(position);

                            // Προσθέτουμε τα macros στη συνολική διαγραφή
                            calories += deletedFood.getCalories();
                            proteins += deletedFood.getProteins();
                            fats += deletedFood.getFats();
                            carbs += deletedFood.getCarbs();

                            // Διαγραφή από λίστα και ανανέωση
                            foodList.remove(position);
                            foodNames.remove(position);
                            adapter.notifyDataSetChanged();

                            // Επιστροφή αποτελεσμάτων στην MainActivity
                            submitMacros();
                        })
                        .setNegativeButton("Όχι", null)
                        .show();
            });
        }

    }

    public void submitMacros() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("calories_removed", calories);
        resultIntent.putExtra("proteins_removed", proteins);
        resultIntent.putExtra("fats_removed", fats);
        resultIntent.putExtra("carbs_removed", carbs);
        resultIntent.putExtra("list",foodList);
        setResult(RESULT_OK, resultIntent);
        // Δεν καλούμε finish εδώ ώστε ο χρήστης να μπορεί να διαγράψει πολλά αν θέλει
    }

    @Override
    public void onBackPressed() {
        // Επιστρέφουμε τις συνολικές διαγραφές όταν ο χρήστης πατήσει back
        submitMacros();
        super.onBackPressed();
    }
}
