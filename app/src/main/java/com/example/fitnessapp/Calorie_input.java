package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Calorie_input extends AppCompatActivity {

    EditText calorieInput;
    Button saveCaloriesButton;
    EditText ageInput, weightInput, heightInput;
    RadioGroup genderGroup;
    RadioGroup activityGroup;
    Button calculateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_input);

        // Σύνδεση με το EditText και το Button
        calorieInput = findViewById(R.id.caloriesInput);
        saveCaloriesButton = findViewById(R.id.saveCaloriesButton);
        ageInput = findViewById(R.id.ageInput);
        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);
        genderGroup = findViewById(R.id.genderGroup);
        activityGroup = findViewById(R.id.activityGroup);
        calculateButton = findViewById(R.id.calculateButton);

        // Ρύθμιση του OnClickListener για το κουμπί "Αποθήκευση"
        saveCaloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCalories(v);
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
                hideKeyboard(v);
            }
        });
    }

    // Μέθοδος για την αποστολή των θερμίδων
    public void submitCalories(View view) {
        // Λήψη της εισαγωγής των θερμίδων από το EditText
        int calories = Integer.parseInt(calorieInput.getText().toString());

        // Δημιουργία Intent για επιστροφή των δεδομένων
        Intent resultIntent = new Intent();
        resultIntent.putExtra("calories", calories);

        // Αποστολή αποτελέσματος πίσω στην MainActivity
        setResult(RESULT_OK, resultIntent);
        finish(); // Κλείσιμο της CalorieInputActivity
    }
    private void calculateCalories() {
        try {
            int age = Integer.parseInt(ageInput.getText().toString());
            double weight = Double.parseDouble(weightInput.getText().toString());
            double height = Double.parseDouble(heightInput.getText().toString());

            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            RadioButton selectedGender = findViewById(selectedGenderId);
            String gender = selectedGender.getText().toString();

            int selectedActivityId = activityGroup.getCheckedRadioButtonId();
            RadioButton selectedActivity = findViewById(selectedActivityId);
            String activityLevel = selectedActivity.getText().toString();

            double bmr;
            if (gender.equalsIgnoreCase("Άνδρας")) {
                bmr = 10 * weight + 6.25 * height - 5 * age + 5;
            } else {
                bmr = 10 * weight + 6.25 * height - 5 * age - 161;
            }

            double multiplier = getActivityMultiplier(activityLevel);
            double maintenanceCalories = bmr * multiplier;

            // Προσθήκη επιπλέον καταστάσεων
            double loseHalfKg = maintenanceCalories - 500;
            double loseQuarterKg = maintenanceCalories - 250;
            double gainQuarterKg = maintenanceCalories + 250;
            double gainHalfKg = maintenanceCalories + 500;

            // Στείλε αποτελέσματα πίσω ή εμφάνισέ τα σε νέα Activity (εδώ τα στέλνουμε πίσω)
            TextView resultTitle = findViewById(R.id.resultTitle);
            TextView maintenanceText = findViewById(R.id.maintenanceText);
            TextView loseQuarterText = findViewById(R.id.loseQuarterText);
            TextView loseHalfText = findViewById(R.id.loseHalfText);
            TextView gainQuarterText = findViewById(R.id.gainQuarterText);
            TextView gainHalfText = findViewById(R.id.gainHalfText);

            resultTitle.setVisibility(View.VISIBLE);
            maintenanceText.setVisibility(View.VISIBLE);
            loseQuarterText.setVisibility(View.VISIBLE);
            loseHalfText.setVisibility(View.VISIBLE);
            gainQuarterText.setVisibility(View.VISIBLE);
            gainHalfText.setVisibility(View.VISIBLE);

            maintenanceText.setText("Συντήρηση: " + (int) maintenanceCalories + " kcal");
            loseQuarterText.setText("Χάσιμο 250γρ/εβδ: " + (int) loseQuarterKg + " kcal");
            loseHalfText.setText("Χάσιμο 500γρ/εβδ: " + (int) loseHalfKg + " kcal");
            gainQuarterText.setText("Αύξηση 250γρ/εβδ: " + (int) gainQuarterKg + " kcal");
            gainHalfText.setText("Αύξηση 500γρ/εβδ: " + (int) gainHalfKg + " kcal");


        } catch (Exception e) {
            Toast.makeText(this, "Παρακαλώ συμπλήρωσε σωστά όλα τα πεδία", Toast.LENGTH_SHORT).show();
        }
    }
    private double getActivityMultiplier(String level) {
        switch (level) {
            case "Καθιστική ζωή":
                return 1.2;
            case "Ελαφρώς ενεργός":
                return 1.375;
            case "Μέτρια δραστήριος":
                return 1.55;
            case "Πολύ δραστήριος":
                return 1.725;
            case "Εξαιρετικά δραστήριος":
                return 1.9;
            default:
                return 1.2;
        }
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
