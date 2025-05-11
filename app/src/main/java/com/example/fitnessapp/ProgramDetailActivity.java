package com.example.fitnessapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class ProgramDetailActivity extends AppCompatActivity {

    private TextView programNameTextView;
    private ListView exerciseListView;
    private ArrayAdapter<SpannableString> adapter;
    private ArrayList<SpannableString> exerciseDescriptions;
    private Program program;
    private ActivityResultLauncher<Intent> addExerciseLauncher;

    private int programIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_detail);

        programNameTextView = findViewById(R.id.programName);
        exerciseListView = findViewById(R.id.exerciseList);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AthleTrack");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);

        // Προσθήκη κουμπιού "Διαγραφή"
        TextView deleteBtn = new TextView(this);
        deleteBtn.setText("Διαγραφή");
        deleteBtn.setTextColor(getResources().getColor(android.R.color.white));
        deleteBtn.setPadding(32, 0, 32, 0);
        deleteBtn.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        deleteBtn.setOnClickListener(v -> confirmDelete());
        toolbar.addView(deleteBtn, new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT, Gravity.END));

        programNameTextView.setOnClickListener(v -> showRenameDialog());

        FloatingActionButton addExerciseBtn = findViewById(R.id.addExerciseButton);
        addExerciseBtn.setOnClickListener(v -> openAddExerciseActivity());

        // Λήψη προγράμματος
        program = (Program) getIntent().getSerializableExtra("program");

        if (program != null) {
            programNameTextView.setText(program.getName());

            exerciseDescriptions = new ArrayList<>();
            for (FullExercise ex : program.getFullExercises()) {
                exerciseDescriptions.add(formatExercise(ex));
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseDescriptions);
            exerciseListView.setAdapter(adapter);

            exerciseListView.setOnItemClickListener((parent, view, position, id) -> {
                FullExercise selectedExercise = program.getFullExercises().get(position);
                showExerciseOptions(selectedExercise, position);
            });
        }

        // Launcher για προσθήκη άσκησης
        addExerciseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<FullExercise> newExercises = (ArrayList<FullExercise>) result.getData().getSerializableExtra("list");

                        if (newExercises != null) {
                            for (FullExercise ex : newExercises) {
                                program.getFullExercises().add(ex);
                                exerciseDescriptions.add(formatExercise(ex));
                            }
                            adapter.notifyDataSetChanged();
                            returnUpdatedProgram();
                        }
                    }
                }
        );
    }

    private SpannableString formatExercise(FullExercise ex) {
        String line = ex.getName() + "\n" + ex.getMuscle() + "\nsets: " + ex.getSets() + "   reps: " + ex.getReps();
        SpannableString spannable = new SpannableString(line);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, ex.getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Διαγραφή Προγράμματος")
                .setMessage("Είσαι σίγουρος ότι θες να διαγράψεις αυτό το πρόγραμμα;")
                .setPositiveButton("Ναι", (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("deletedProgramIndex", programIndex);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton("Όχι", null)
                .show();
    }

    private void showExerciseOptions(FullExercise exercise, int index) {
        String[] options = {"Επεξεργασία sets/reps", "Διαγραφή Άσκησης"};

        new AlertDialog.Builder(this)
                .setTitle("Επιλογές Άσκησης")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showEditDialog(exercise, index);
                    } else {
                        program.getFullExercises().remove(index);
                        exerciseDescriptions.remove(index);
                        adapter.notifyDataSetChanged();
                        returnUpdatedProgram(); // ✅
                    }
                })
                .show();
    }

    private void showEditDialog(FullExercise exercise, int index) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 10);

        EditText setsInput = new EditText(this);
        setsInput.setHint("Sets");
        setsInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        setsInput.setText(String.valueOf(exercise.getSets()));
        layout.addView(setsInput);

        EditText repsInput = new EditText(this);
        repsInput.setHint("Reps");
        repsInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        repsInput.setText(String.valueOf(exercise.getReps()));
        layout.addView(repsInput);

        new AlertDialog.Builder(this)
                .setTitle("Επεξεργασία Άσκησης")
                .setView(layout)
                .setPositiveButton("Αποθήκευση", (dialog, which) -> {
                    try {
                        int newSets = Integer.parseInt(setsInput.getText().toString());
                        int newReps = Integer.parseInt(repsInput.getText().toString());
                        exercise.setSets(newSets);
                        exercise.setReps(newReps);
                        exerciseDescriptions.set(index, formatExercise(exercise));
                        adapter.notifyDataSetChanged();
                        returnUpdatedProgram(); // ✅
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Μη έγκυρο input", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Ακύρωση", null)
                .show();
    }

    private void showRenameDialog() {
        EditText input = new EditText(this);
        input.setText(program.getName());
        input.setSelection(program.getName().length());

        new AlertDialog.Builder(this)
                .setTitle("Μετονομασία Προγράμματος")
                .setView(input)
                .setPositiveButton("Αποθήκευση", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        program.setName(newName);
                        programNameTextView.setText(newName);
                        returnUpdatedProgram(); // ✅
                    } else {
                        Toast.makeText(this, "Το όνομα δεν μπορεί να είναι κενό", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Ακύρωση", null)
                .show();
    }

    private void openAddExerciseActivity() {
        Intent intent = new Intent(this, AddExercises.class);
        addExerciseLauncher.launch(intent);
    }

    private void returnUpdatedProgram() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedProgram", program);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    public void onBackPressed() {
        returnUpdatedProgram(); // ✅ Τελευταία επιστροφή πριν φύγεις
        super.onBackPressed();
    }
}
