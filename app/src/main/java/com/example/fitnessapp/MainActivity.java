package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView caloriesIntakeTextView;
    private TextView proteinIntakeTextView;
    private TextView fatIntakeTextView;
    private TextView carbsIntakeTextView;
    private TextView caloriesRamaingTextView;
    private  TextView eatText;

    private ActivityResultLauncher<Intent> calorieInputLauncher;
    private ActivityResultLauncher<Intent> foodResultLauncher;
    private ActivityResultLauncher<Intent> programresultLuncher;
    private  ActivityResultLauncher<Intent> allFoodLuncher;
    private ActivityResultLauncher<Intent> programDeleteResultLauncher;

    private int caloriesIntake = 0;
    private int caloriesRemaing = 0;
    private boolean flag=true;
    private int Proteins = 0;
    private int Fats = 0;
    private int Carbs = 0;
    ArrayList<Program> programList = new ArrayList<>();
    ProgramAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Food> ListOfFood=new ArrayList<>();
    SharedPreferences prefs;

DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper=new DatabaseHelper(this);
        eatText=findViewById(R.id.Eat_text);
        caloriesIntakeTextView = findViewById(R.id.Calories_intake);
        proteinIntakeTextView = findViewById(R.id.Protein_num);
        fatIntakeTextView = findViewById(R.id.Fat_num);
        carbsIntakeTextView = findViewById(R.id.Carbs_num);
        caloriesRamaingTextView = findViewById(R.id.Calories_remaining);
        recyclerView = findViewById(R.id.program_view);
        adapter = new ProgramAdapter(programList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email != null) {
            User currentUser = DatabaseHelper.getUserByEmail(getApplicationContext(), email);
            if (currentUser != null) {
                caloriesIntake = currentUser.getCaloriesIntake();
                caloriesRemaing = currentUser.getCaloriesRemaining();
                Proteins = currentUser.getProteins();
                Fats = currentUser.getFats();
                Carbs = currentUser.getCarbs();
                ListOfFood = currentUser.getListOfFood();
                programList = currentUser.getProgramList();
                adapter.setProgramList(programList); // νέα μέθοδος στο adapter
                adapter.notifyDataSetChanged();
            }
        }
        // Launcher για θερμίδες στόχου
        calorieInputLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null &&flag) {
                        int calories = result.getData().getIntExtra("calories", 0);
                        caloriesRemaing = calories;
                        caloriesRamaingTextView.setText(String.valueOf(caloriesRemaing));
                        flag=false;
                        saveUserData();
                    }
                    else if(result.getResultCode() == RESULT_OK && result.getData() != null &&!flag)
                    {
                        int calories = result.getData().getIntExtra("calories", 0);
                        caloriesRemaing = calories-caloriesIntake;
                        caloriesRamaingTextView.setText(String.valueOf(caloriesRemaing));
                        saveUserData();
                    }
                }
        );

        // Launcher για επιστροφή macros από SecondActivity
        foodResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        if(caloriesRemaing!=0)
                        {Intent data = result.getData();
                            int calories = data.getIntExtra("calories_total", 0);
                            int protein = data.getIntExtra("protein_total", 0);
                            int fat = data.getIntExtra("fat_total", 0);
                            int carbs = data.getIntExtra("carbs_total", 0);
                            ArrayList<Food> foodFlag=(ArrayList<Food>)data.getSerializableExtra("list");
                            if(ListOfFood!=null){
                                ListOfFood.addAll(foodFlag);
                            }else{
                                ListOfFood=new ArrayList<>();
                            ListOfFood.addAll(foodFlag);}
                            caloriesIntake += calories;
                            Proteins += protein;
                            Fats += fat;
                            Carbs += carbs;

                            // Ενημέρωση TextViews
                            caloriesIntakeTextView.setText(String.valueOf(caloriesIntake));
                            proteinIntakeTextView.setText(String.valueOf(Proteins));
                            fatIntakeTextView.setText(String.valueOf(Fats));
                            carbsIntakeTextView.setText(String.valueOf(Carbs));

                            // Αφαιρούμε θερμίδες από υπόλοιπο
                            caloriesRemaing -= calories;
                            caloriesRamaingTextView.setText(String.valueOf(caloriesRemaing));
                            saveUserData();
                            if(ListOfFood!=null){
                                TextView caloriesText = findViewById(R.id.Calories_intake);
                                caloriesText.setPaintFlags(caloriesText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                                TextView eatText = findViewById(R.id.Eat_text);
                                eatText.setPaintFlags(eatText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            }

                            Toast.makeText(this, "Προστέθηκαν macros!", Toast.LENGTH_SHORT).show(); }
                        else{ Toast.makeText(this, "Εισάγεται Θερμίδες", Toast.LENGTH_SHORT).show(); }
                    }
                }
        );

        programresultLuncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        ArrayList<FullExercise> fullExercises = (ArrayList<FullExercise>) data.getSerializableExtra("list");
                        String name = data.getStringExtra("name");

                        if (name != null && !name.isEmpty() && fullExercises != null && !fullExercises.isEmpty()) {
                            Program program = new Program(name, fullExercises);
                            adapter.addProgram(program);  // Πρόσθεσε το πρόγραμμα
                            adapter.notifyItemInserted(adapter.getItemCount() - 1);  // Ενημέρωση του νέου στοιχείου στο RecyclerView
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1); // Μετακίνηση στο τελευταίο πρόγραμμα
                            saveUserData();
                        } else {
                            Toast.makeText(this, "Program name or exercises missing", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




// Launcher για την εκκίνηση της δραστηριότητας AllFoodDetail
        allFoodLuncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data=result.getData();
                        int calories = data.getIntExtra("calories_removed", 0);
                        int protein = data.getIntExtra("proteins_removed", 0);
                        int fat = data.getIntExtra("fats_removed", 0);
                        int carbs = data.getIntExtra("carbs_removed", 0);
                        caloriesIntake -= calories;
                        Proteins -= protein;
                        Fats -= fat;
                        Carbs -= carbs;
                        caloriesIntakeTextView.setText(String.valueOf(caloriesIntake));
                        proteinIntakeTextView.setText(String.valueOf(Proteins));
                        fatIntakeTextView.setText(String.valueOf(Fats));
                        carbsIntakeTextView.setText(String.valueOf(Carbs));
                        caloriesRemaing += calories;
                        caloriesRamaingTextView.setText(String.valueOf(caloriesRemaing));
                        ArrayList<Food> foodFlag=(ArrayList<Food>)data.getSerializableExtra("list");
                        ListOfFood=foodFlag;
                        saveUserData();
                    }
                }
        );
        programDeleteResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        int idx = result.getData().getIntExtra("deletedProgramIndex", -1);
                        if (idx >= 0 && idx < programList.size()) {
                            adapter.removeProgram(idx);
                            Toast.makeText(this, "Το πρόγραμμα διαγράφηκε", Toast.LENGTH_SHORT).show();
                            saveUserData();
                        }
                        Intent data=result.getData();
                        Program updatedProgram = (Program)data.getSerializableExtra("updatedProgram");
                        if (updatedProgram != null) {
                            for (int i = 0; i < programList.size(); i++) {
                                if (programList.get(i).getId() == updatedProgram.getId()) {
                                    programList.set(i, updatedProgram);
                                    adapter.notifyItemChanged(i);
                                    break;
                                }
                            }
                            saveUserData();
                        }
                    }
                }
        );
        adapter.setOnProgramClickListener((program, position) -> {
            Intent intent = new Intent(MainActivity.this, ProgramDetailActivity.class);
            intent.putExtra("program", program);
            intent.putExtra("programIndex", position);
            programDeleteResultLauncher.launch(intent);
        });
        eatText.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllFoodDetail.class);
            intent.putExtra("foodList", ListOfFood);  // Προσθήκη της λίστας στο Intent
            allFoodLuncher.launch(intent);
        });

        caloriesIntakeTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllFoodDetail.class);
            intent.putExtra("foodList", ListOfFood);  // Προσθήκη της λίστας στο Intent
            allFoodLuncher.launch(intent);
        });

        if(ListOfFood!=null){
            TextView caloriesText = findViewById(R.id.Calories_intake);
            caloriesText.setPaintFlags(caloriesText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            TextView eatText = findViewById(R.id.Eat_text);
            eatText.setPaintFlags(eatText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        checkIfNewDay();
    }





        // Κλήση για άνοιγμα SecondActivity με launcher
    public void AddFood(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        foodResultLauncher.launch(intent);
    }

    // Κλήση για άνοιγμα CalorieInputActivity με launcher
    public void openCalorieInput(View view) {
        Intent intent = new Intent(this, Calorie_input.class);
        calorieInputLauncher.launch(intent);
    }

    public  void AddProgram(View view){
        Intent intent = new Intent(this, AddProgram.class);
        programresultLuncher.launch(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Καθάρισε τα shared preferences (log out)
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Πήγαινε πίσω στη LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);
        if (email != null) {
            User user = new User();
            user.setEmail(email);
            user.setCaloriesRemaining(caloriesRemaing); // ο στόχος
            user.setCaloriesIntake(caloriesIntake);
            user.setProteins(Proteins);
            user.setFats(Fats);
            user.setCarbs(Carbs);
            user.setListOfFood(ListOfFood);
            user.setProgramList(programList);

            dbHelper.updateUser(user);
        }
        SharedPreferences.Editor editor = prefs.edit();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        editor.putString("lastLoginDate", currentDate);
        editor.apply();
    }
    @Override
protected  void onPause(){
        super.onPause();
    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    String email = prefs.getString("email", null);
    if (email != null) {
        User user = new User();
        user.setEmail(email);
        user.setCaloriesRemaining(caloriesRemaing); // ο στόχος
        user.setCaloriesIntake(caloriesIntake);
        user.setProteins(Proteins);
        user.setFats(Fats);
        user.setCarbs(Carbs);
        user.setListOfFood(ListOfFood);
        user.setProgramList(programList);

        dbHelper.updateUser(user);
    }
}
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email != null) {
            // Ανάκτηση δεδομένων από τη βάση δεδομένων
            User currentUser = dbHelper.getUserByEmail(email);
            if (currentUser != null) {
                caloriesIntake = currentUser.getCaloriesIntake();
                caloriesRemaing = currentUser.getCaloriesRemaining();
                Proteins = currentUser.getProteins();
                Fats = currentUser.getFats();
                Carbs = currentUser.getCarbs();
                ListOfFood = currentUser.getListOfFood();
                programList = currentUser.getProgramList();

                // Ενημέρωση UI
                caloriesIntakeTextView.setText(String.valueOf(caloriesIntake));
                proteinIntakeTextView.setText(String.valueOf(Proteins));
                fatIntakeTextView.setText(String.valueOf(Fats));
                carbsIntakeTextView.setText(String.valueOf(Carbs));
                caloriesRamaingTextView.setText(String.valueOf(caloriesRemaing));

                // Ενημέρωση του RecyclerView
                adapter.setProgramList(programList);
                adapter.notifyDataSetChanged();
            }
        }
        checkIfNewDay();

    }
    private void saveUserData() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email != null) {
            // Δημιουργία και συμπλήρωση του User αντικειμένου
            User user = new User();
            user.setEmail(email);
            user.setCaloriesIntake(caloriesIntake);
            user.setCaloriesRemaining(caloriesRemaing);
            user.setProteins(Proteins);
            user.setFats(Fats);
            user.setCarbs(Carbs);
            user.setListOfFood(ListOfFood);
            user.setProgramList(programList);

            if (dbHelper != null) {
                dbHelper.updateUser(user);
            } else {
                Log.e("saveUserData", "dbHelper is null");
            }

        } else {
            Log.w("UserData", "Δεν βρέθηκε email χρήστη στα SharedPreferences.");
        }
    }
    private void checkIfNewDay() {
        // Ανάκτηση της αποθηκευμένης ημερομηνίας
        String lastLoginDate = prefs.getString("lastLoginDate", null);
        // Παίρνουμε την τρέχουσα ημερομηνία
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        if (lastLoginDate != null && !lastLoginDate.equals(currentDate)) {
            // Αν η ημερομηνία έχει αλλάξει (πέρασαν 24 ώρες)
            resetDailyValues();
        }
    }
    private void resetDailyValues() {
        // Μηδενίζουμε τις μεταβλητές
        caloriesRemaing+=caloriesIntake;
        caloriesIntake = 0;
        Proteins = 0;
        Fats = 0;
        Carbs = 0;
        ListOfFood.clear();


        // Ενημέρωση του UI με τις νέες τιμές
        updateUI();

        // Αποθήκευση των μηδενισμένων τιμών για να μην χαθούν κατά την έξοδο της εφαρμογής
        saveUserData();
    }
    private void updateUI() {
        // Ενημέρωση των TextViews με τις νέες τιμές
        caloriesIntakeTextView.setText(String.valueOf(caloriesIntake));
        caloriesRamaingTextView.setText(String.valueOf(caloriesRemaing));
        proteinIntakeTextView.setText(String.valueOf(Proteins));
        fatIntakeTextView.setText(String.valueOf(Fats));
        carbsIntakeTextView.setText(String.valueOf(Carbs));
    }


}
