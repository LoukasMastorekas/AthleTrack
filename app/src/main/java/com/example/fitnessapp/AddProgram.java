package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class AddProgram extends AppCompatActivity {

    RecyclerView exerciseRecyclerView;
    ArrayList<Exercise> exerciseList=new ArrayList<>();
    ExerciseAdapter exerciseAdapter;
    DatabaseHelper dbExercise;
    SearchView exerciseSearchView;
    ArrayList<FullExercise> fullExercises;
    EditText nameOfProgram;

    private ActivityResultLauncher<Intent> ExerciseDetailLauncher;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_program);
        exerciseRecyclerView=findViewById(R.id.exercise_recycler_view);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exerciseSearchView=findViewById(R.id.exercise_search_view);
        dbExercise=new DatabaseHelper(this);
        exerciseList=dbExercise.getAllExercises();
        fullExercises=new ArrayList<>();
        nameOfProgram=findViewById(R.id.program_name_edit_text);



        ExerciseDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data=result.getData();
                        if(data!=null){
                            String name=data.getStringExtra("name");
                            String description=data.getStringExtra("description");
                            String muscle=data.getStringExtra("muscle");
                            int sets=Integer.parseInt(Objects.requireNonNull(data.getStringExtra("sets")));
                            int reps=Integer.parseInt(Objects.requireNonNull(data.getStringExtra("reps")));

                            FullExercise exercise=new FullExercise(name,0,description,muscle,sets,reps);
                            fullExercises.add(exercise);
                        }
                    }
                });
        exerciseAdapter=new ExerciseAdapter(this,exerciseList,ExerciseDetailLauncher);
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        // Search λειτουργία
        exerciseSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                exerciseAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                exerciseAdapter.getFilter().filter(newText);
                return false;
            }
        });

        ImageButton backButton=findViewById(R.id.backButton_AddProgram);
        backButton.setOnClickListener(v -> {
            String name = nameOfProgram.getText().toString().trim();
            Intent resultIntent=new Intent();
            if(!fullExercises.isEmpty() && !name.isEmpty()){
            resultIntent.putExtra("list",fullExercises);
            resultIntent.putExtra("name",name);
            setResult(RESULT_OK,resultIntent);
            Toast.makeText(this, "Program added", Toast.LENGTH_SHORT).show();
            finish();
            }
            else{
                Toast.makeText(this, "Add exercise or Name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}