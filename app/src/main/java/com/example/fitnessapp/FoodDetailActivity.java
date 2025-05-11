package com.example.fitnessapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDetailActivity extends AppCompatActivity {

    TextView foodNameText, caloriesText, proteinText, fatText, carbsText;
    EditText gramsInput;
    Button addButton;

    double caloriesPerGram, proteinPerGram, fatPerGram, carbsPerGram;
    String foodName;

    int currentGrams = 100;  // default τιμή

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        foodNameText = findViewById(R.id.food_name_text);
        caloriesText = findViewById(R.id.calories_text);
        proteinText = findViewById(R.id.protein_text);
        fatText = findViewById(R.id.fat_text);
        carbsText = findViewById(R.id.carbs_text);
        gramsInput = findViewById(R.id.grams_input);
        addButton = findViewById(R.id.add_button);

        // Πάρε τιμές από intent
        foodName = getIntent().getStringExtra("food_name");
        double calories = getIntent().getDoubleExtra("calories", 0);
        double protein = getIntent().getDoubleExtra("protein", 0);
        double fat = getIntent().getDoubleExtra("fat", 0);
        double carbs = getIntent().getDoubleExtra("carbs", 0);

        // Υπολόγισε ανά γραμμάριο
        caloriesPerGram = calories / 100;
        proteinPerGram = protein / 100;
        fatPerGram = fat / 100;
        carbsPerGram = carbs / 100;

        // Εμφάνισε αρχικές τιμές για 100g
        foodNameText.setText(foodName);
        displayValues(100);

        // Όταν αλλάζει το input
        gramsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String gramsText = s.toString();
                if (!gramsText.isEmpty()) {
                    currentGrams = Integer.parseInt(gramsText);
                    displayValues(currentGrams);
                } else {
                    currentGrams = 100;
                    displayValues(100);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Όταν πατηθεί το κουμπί
        addButton.setOnClickListener(v -> {
            String grams= String.valueOf(gramsInput.getText());
            if(!grams.isEmpty())
            { //Toast.makeText(this, foodName + " (" + currentGrams + "g) added!", Toast.LENGTH_SHORT).show();
            submitMacros(v);}
            else{
                Toast.makeText(this, "put data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Ενημέρωση των TextView με βάση τα γραμμάρια
    private void displayValues(int grams) {
        caloriesText.setText("Calories: " + (int) (caloriesPerGram * grams) + " kcal");
        proteinText.setText("Protein: " + (int) (proteinPerGram * grams) + " g");
        fatText.setText("Fat: " + (int) (fatPerGram * grams) + " g");
        carbsText.setText("Carbs: " + (int) (carbsPerGram * grams) + " g");
    }

    public void submitMacros(View view) {
        try {
            // Διαβάζουμε τον αριθμό γραμμαρίων που έβαλε ο χρήστης
            currentGrams = (int) Double.parseDouble(gramsInput.getText().toString());

            // Υπολογισμός των μακροθρεπτικών συστατικών
            int calories = (int) (caloriesPerGram * currentGrams);
            int protein = (int) (proteinPerGram * currentGrams);
            int fat = (int) (fatPerGram * currentGrams);
            int carbs = (int) (carbsPerGram * currentGrams);


            Intent resultIntent = new Intent();
            resultIntent.putExtra("calories", calories);
            resultIntent.putExtra("protein", protein);
            resultIntent.putExtra("fat", fat);
            resultIntent.putExtra("carbs", carbs);
            resultIntent.putExtra("name",foodName);
            setResult(RESULT_OK, resultIntent);



            // Κλείσιμο της δραστηριότητας
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Παρακαλώ εισάγετε έγκυρο αριθμό γραμμαρίων.", Toast.LENGTH_SHORT).show();
        }
    }
}
