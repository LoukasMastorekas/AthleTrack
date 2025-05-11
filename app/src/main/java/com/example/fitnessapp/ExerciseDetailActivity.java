package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExerciseDetailActivity extends AppCompatActivity {

    private TextView exerciseNameTextView;
    private TextView exerciseDescriptionTextView;
    private TextView exerciseMuscleTextView;
    private EditText setInput;
    private EditText repsInput;
    private Button addButton;
    String sets ;
    String reps ;
    String name;
    String description;
    String muscle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercise_detail);

        exerciseNameTextView = findViewById(R.id.exercise_name_detail);
        exerciseDescriptionTextView = findViewById(R.id.exercise_description_detail);
        exerciseMuscleTextView = findViewById(R.id.exercise_muscle_detail);
        setInput = findViewById(R.id.set_input);
        repsInput = findViewById(R.id.reps_input);
        addButton = findViewById(R.id.add_button);

         name = getIntent().getStringExtra("exercise_name");
         description = getIntent().getStringExtra("exercise_description");
         muscle = getIntent().getStringExtra("exercise_muscle");

        exerciseNameTextView.setText(name);
        exerciseDescriptionTextView.setText(description);
        exerciseMuscleTextView.setText(muscle);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sets = setInput.getText().toString().trim();
                 reps = repsInput.getText().toString().trim();

                if (!sets.isEmpty() && !reps.isEmpty()) {
                    Toast.makeText(ExerciseDetailActivity.this, "Exercise added", Toast.LENGTH_SHORT).show();
                    submitExercise(v);
                } else {
                    Toast.makeText(ExerciseDetailActivity.this, "Βάλτε έγκυρες τιμές", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void submitExercise(View view){
         Intent resultIntent= new Intent();
         resultIntent.putExtra("sets",sets);
         resultIntent.putExtra("reps",reps);
         resultIntent.putExtra("name",name);
         resultIntent.putExtra("description",description);
         resultIntent.putExtra("muscle",muscle);
         setResult(RESULT_OK,resultIntent);
         finish();
    }
}
