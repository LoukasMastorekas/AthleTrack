package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {
    RecyclerView foodRecyclerView;
    ArrayList<Food> foodList;
    FoodAdapter foodAdapter;
    DatabaseHelper dbHelper;
    SearchView foodSearchView;
    String foodName;
    ArrayList<Food> ListOfFoods=new ArrayList<>();

    // Σωρευτικά macros
    int totalCalories = 0;
    int totalProtein = 0;
    int totalFat = 0;
    int totalCarbs = 0;

    // Launcher για να παίρνει αποτελέσματα από FoodDetailActivity
    private ActivityResultLauncher<Intent> foodDetailLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        // Αρχικοποίηση των views
        foodRecyclerView = findViewById(R.id.food_list_view);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodSearchView = findViewById(R.id.searchBar);
        dbHelper = new DatabaseHelper(this);
        foodList = dbHelper.getAllFoods();

        // Ορίζουμε τον launcher
        foodDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int calories = data.getIntExtra("calories", 0);
                            int protein = data.getIntExtra("protein", 0);
                            int fat = data.getIntExtra("fat", 0);
                            int carbs = data.getIntExtra("carbs", 0);
                            foodName=data.getStringExtra("name");
                            ListOfFoods.add(new Food(foodName,calories,fat,protein,carbs));

                            // Σωρευτικά
                            totalCalories += calories;
                            totalProtein += protein;
                            totalFat += fat;
                            totalCarbs += carbs;

                           /* Toast.makeText(this,
                                    "Σύνολο: " + totalCalories + " kcal, "
                                            + totalProtein + "g πρωτεΐνη",
                                    Toast.LENGTH_SHORT).show();*/
                        }
                    }
                }
        );

        // Σύνδεση adapter
        foodAdapter = new FoodAdapter(this, foodList, foodDetailLauncher);
        foodRecyclerView.setAdapter(foodAdapter);

        // Search λειτουργία
        foodSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                foodAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                foodAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // Back button functionality
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("calories_total", totalCalories);
            resultIntent.putExtra("protein_total", totalProtein);
            resultIntent.putExtra("fat_total", totalFat);
            resultIntent.putExtra("carbs_total", totalCarbs);
            resultIntent.putExtra("list",ListOfFoods);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
