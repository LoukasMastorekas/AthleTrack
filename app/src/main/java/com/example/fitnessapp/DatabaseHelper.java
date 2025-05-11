package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FitnessApp.db";  // Όνομα της βάσης δεδομένων
    private static final int DATABASE_VERSION = 4; // Έκδοση της βάσης δεδομένων

    // Πίνακας φαγητών
    public static final String TABLE_FOODS = "foods";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CALORIES = "calories_per_gram";
    public static final String COLUMN_PROTEIN = "protein_per_gram";
    public static final String COLUMN_FAT = "fat_per_gram";
    public static final String COLUMN_CARBS = "carbs_per_gram";

    // Πίνακας ασκήσεων
    public static final String TABLE_EXERCISE = "exercise";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_MUSCLES = "muscle";

    public static final String TABLE_USERS = "User";

    public static final String COL_ID = "id";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_CAL_INTAKE = "caloriesIntake";
    public static final String COL_CAL_REMAINING = "caloriesRemaining";
    public static final String COL_PROTEINS = "proteins";
    public static final String COL_FATS = "fats";
    public static final String COL_CARBS = "carbs";
    public static final String COL_FOOD_LIST = "foodList";
    public static final String COL_PROGRAM_LIST = "programList";

    private Gson gson = new Gson();


    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Δημιουργία του πίνακα όταν η βάση δεδομένων δημιουργείται
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FOODS_TABLE = "CREATE TABLE " + TABLE_FOODS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CALORIES + " REAL, " +
                COLUMN_PROTEIN + " REAL, " +
                COLUMN_FAT + " REAL, " +
                COLUMN_CARBS + " REAL)";
        db.execSQL(CREATE_FOODS_TABLE);

        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_MUSCLES + " TEXT)";
        db.execSQL(CREATE_EXERCISE_TABLE);
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_CAL_INTAKE + " INTEGER, " +
                COL_CAL_REMAINING + " INTEGER, " +
                COL_PROTEINS + " INTEGER, " +
                COL_FATS + " INTEGER, " +
                COL_CARBS + " INTEGER, " +
                COL_FOOD_LIST + " TEXT, " +
                COL_PROGRAM_LIST + " TEXT)";
        db.execSQL(createTable);

        insertInitialFoods(db);
        insertInitialExercises(db);
    }

    // Εισαγωγή των αρχικών φαγητών στη βάση δεδομένων
    private void insertInitialFoods(SQLiteDatabase db) {
        insertFood(db, "Κοτόπουλο", 1.65, 0.31, 0.03, 0.00);
        insertFood(db, "Μοσχάρι", 2.50, 0.26, 0.15, 0.00);
        insertFood(db, "Σολωμός", 2.08, 0.20, 0.13, 0.00);
        insertFood(db, "Αυγό", 1.43, 0.13, 0.10, 0.01);
        insertFood(db, "Ρύζι", 1.30, 0.02, 0.003, 0.28);
        insertFood(db, "Πατάτα", 0.77, 0.02, 0.01, 0.17);
        insertFood(db, "Μπρόκολο", 0.34, 0.028, 0.003, 0.07);
        insertFood(db, "Γιαούρτι", 0.59, 0.10, 0.03, 0.04);
        insertFood(db, "Αμύγδαλα", 5.76, 0.21, 0.49, 0.22);
        insertFood(db, "Μπανάνα", 0.89, 0.011, 0.003, 0.23);
        insertFood(db, "Μήλο", 0.52, 0.003, 0.002, 0.14);
        insertFood(db, "Φακές", 1.16, 0.09, 0.004, 0.20);
        insertFood(db, "Τόνος", 1.32, 0.23, 0.007, 0.00);
        insertFood(db, "Ελιές", 1.45, 0.01, 0.15, 0.003);
        insertFood(db, "Σπανάκι", 0.23, 0.029, 0.004, 0.036);
        insertFood(db, "Ψωμί", 2.65, 0.09, 0.034, 0.49);
        insertFood(db, "Μέλι", 3.04, 0.003, 0.00, 0.82);
        insertFood(db, "Μακαρόνια", 1.31, 0.05, 0.005, 0.25);
        insertFood(db, "Γάλα", 0.64, 0.03, 0.03, 0.05);
        insertFood(db, "Καρπούζι", 0.30, 0.006, 0.001, 0.08);
        insertFood(db, "Φράουλα", 0.32, 0.007, 0.003, 0.08);
        insertFood(db, "Σταφύλι", 0.69, 0.007, 0.001, 0.18);
        insertFood(db, "Καρότο", 0.41, 0.009, 0.002, 0.10);
        insertFood(db, "Γραβιέρα", 3.91, 0.25, 0.32, 0.01);
        insertFood(db, "Βρώμη", 3.89, 0.17, 0.07, 0.66);
        insertFood(db, "Φιστίκι", 5.67, 0.20, 0.49, 0.28);
        insertFood(db, "Σοκολάτα", 5.46, 0.07, 0.31, 0.59);
        insertFood(db, "Καφές", 0.02, 0.000, 0.000, 0.00);
        insertFood(db, "Λάδι", 8.84, 0.00, 1.00, 0.00);
        insertFood(db, "Μπύρα", 0.43, 0.004, 0.000, 0.03);
    }

    private void insertFood(SQLiteDatabase db, String name, double cal, double pro, double fat, double carb) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CALORIES, cal);
        values.put(COLUMN_PROTEIN, pro);
        values.put(COLUMN_FAT, fat);
        values.put(COLUMN_CARBS, carb);
        db.insert(TABLE_FOODS, null, values);
    }

    // Εισαγωγή των αρχικών ασκήσεων στη βάση δεδομένων
    private void insertInitialExercises(SQLiteDatabase db) {
        insertExercise(db, "Push Up", "A bodyweight exercise where you push yourself up and down, working the chest, triceps, and shoulders.", "Chest, Triceps, Shoulders");
        insertExercise(db, "Squat", "A lower body exercise that involves bending the knees to lower the body, working the quadriceps, hamstrings, and glutes.", "Legs, Glutes, Hamstrings");
        insertExercise(db, "Deadlift", "A strength exercise where you lift a barbell from the ground to hip level, targeting the back, glutes, and hamstrings.", "Back, Glutes, Hamstrings");
        insertExercise(db, "Bench Press", "A compound movement where you press a barbell away from your chest, working the chest, shoulders, and triceps.", "Chest, Triceps, Shoulders");
        insertExercise(db, "Pull Up", "A bodyweight exercise where you pull yourself up on a bar, focusing on the back and biceps.", "Back, Biceps");
        insertExercise(db, "Barbell Row", "A strength exercise where you pull a barbell towards your torso, working the back, biceps, and rear deltoids.", "Back, Biceps, Shoulders");
        insertExercise(db, "Lunges", "A lower body exercise where you step forward and lower your body, focusing on the quads, hamstrings, and glutes.", "Legs, Glutes, Hamstrings");
        insertExercise(db, "Dumbbell Bicep Curl", "An exercise where you curl dumbbells towards your shoulders, isolating the biceps.", "Biceps");
        insertExercise(db, "Tricep Dips", "An exercise where you lower and raise your body using parallel bars, focusing on the triceps.", "Triceps");
        insertExercise(db, "Leg Press", "A machine-based exercise where you push a weighted platform away from your body, working the legs and glutes.", "Legs, Glutes, Hamstrings");
        insertExercise(db, "Shoulder Press", "An overhead pressing movement with dumbbells or a barbell, targeting the shoulders and triceps.", "Shoulders, Triceps");
        insertExercise(db, "Lat Pulldown", "A machine-based exercise where you pull a bar towards your chest, primarily working the back and biceps.", "Back, Biceps");
        insertExercise(db, "Barbell Squat", "A squat variation using a barbell, focusing on the legs, glutes, and lower back.", "Legs, Glutes, Lower Back");
        insertExercise(db, "Romanian Deadlift", "A variation of the deadlift that targets the hamstrings and glutes by keeping the legs straighter.", "Hamstrings, Glutes, Lower Back");
        insertExercise(db, "Chest Fly", "An isolation exercise where you open your arms wide to target the chest muscles.", "Chest");
    }

    private void insertExercise(SQLiteDatabase db, String name, String desc, String muscles) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESC, desc);
        values.put(COLUMN_MUSCLES, muscles);
        db.insert(TABLE_EXERCISE, null, values);
    }

    // Μέθοδος για την αναζήτηση των φαγητών στη βάση δεδομένων
    public ArrayList<Food> getAllFoods() {
        ArrayList<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FOODS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Food food = new Food(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_CALORIES)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_PROTEIN)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_FAT)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_CARBS))
                );
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodList;
    }

    // Μέθοδος για την αναζήτηση των ασκήσεων στη βάση δεδομένων
    public ArrayList<Exercise> getAllExercises() {
        ArrayList<Exercise> exerciseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXERCISE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Exercise exercise = new Exercise(
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESC)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MUSCLES))
                );
                exerciseList.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exerciseList;
    }
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PASSWORD, user.getPassword());
        values.put(COL_CAL_INTAKE, user.getCaloriesIntake());
        values.put(COL_CAL_REMAINING, user.getCaloriesRemaining());
        values.put(COL_PROTEINS, user.getProteins());
        values.put(COL_FATS, user.getFats());
        values.put(COL_CARBS, user.getCarbs());

        String foodJson = gson.toJson(user.getListOfFood());
        String programJson = gson.toJson(user.getProgramList());

        values.put(COL_FOOD_LIST, foodJson);
        values.put(COL_PROGRAM_LIST, programJson);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }


    public User getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email=? AND password=?", new String[]{email, password});

        if (cursor.moveToFirst()) {
            // Create a User object with the cursor data
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex(COL_EMAIL));
            @SuppressLint("Range") String userPassword = cursor.getString(cursor.getColumnIndex(COL_PASSWORD));
            @SuppressLint("Range") int intake = cursor.getInt(cursor.getColumnIndex(COL_CAL_INTAKE));
            @SuppressLint("Range") int remaining = cursor.getInt(cursor.getColumnIndex(COL_CAL_REMAINING));
            @SuppressLint("Range") int proteins = cursor.getInt(cursor.getColumnIndex(COL_PROTEINS));
            @SuppressLint("Range") int fats = cursor.getInt(cursor.getColumnIndex(COL_FATS));
            @SuppressLint("Range") int carbs = cursor.getInt(cursor.getColumnIndex(COL_CARBS));

            @SuppressLint("Range") String foodJson = cursor.getString(cursor.getColumnIndex(COL_FOOD_LIST));
            @SuppressLint("Range") String programJson = cursor.getString(cursor.getColumnIndex(COL_PROGRAM_LIST));

            // Deserialize the food and program lists
            Type foodListType = new TypeToken<ArrayList<Food>>(){}.getType();
            ArrayList<Food> foodList = gson.fromJson(foodJson, foodListType);

            Type programListType = new TypeToken<ArrayList<Program>>(){}.getType();
            ArrayList<Program> programList = gson.fromJson(programJson, programListType);

            // Create and return the User object
            User user = new User(id, userEmail, userPassword, intake, remaining, proteins, fats, carbs, foodList, programList);
            cursor.close();
            db.close();
            return user;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    @SuppressLint("Range")
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COL_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COL_PASSWORD)));
            user.setCaloriesIntake(cursor.getInt(cursor.getColumnIndex(COL_CAL_INTAKE)));
            user.setCaloriesRemaining(cursor.getInt(cursor.getColumnIndex(COL_CAL_REMAINING)));
            user.setProteins(cursor.getInt(cursor.getColumnIndex(COL_PROTEINS)));
            user.setFats(cursor.getInt(cursor.getColumnIndex(COL_FATS)));
            user.setCarbs(cursor.getInt(cursor.getColumnIndex(COL_CARBS)));

            // Deserialize JSON to Lists
            Type foodType = new TypeToken<ArrayList<Food>>() {}.getType();
            @SuppressLint("Range") ArrayList<Food> foodList = gson.fromJson(cursor.getString(cursor.getColumnIndex(COL_FOOD_LIST)), foodType);
            user.setListOfFood(foodList);

            Type programType = new TypeToken<ArrayList<Program>>() {}.getType();
            @SuppressLint("Range") ArrayList<Program> programList = gson.fromJson(cursor.getString(cursor.getColumnIndex(COL_PROGRAM_LIST)), programType);
            user.setProgramList(programList);

            cursor.close();
            db.close();
            return user;
        }
        cursor.close();
        db.close();
        return null;
    }
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_ID},        // Πάρε μόνο το ID
                COL_EMAIL + " = ?",
                new String[]{email},
                null, null, null
        );

        int userId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            cursor.close();
        }

        db.close();
        return userId;
    }
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_CAL_INTAKE, user.getCaloriesIntake());
        values.put(COL_CAL_REMAINING, user.getCaloriesRemaining());
        values.put(COL_PROTEINS, user.getProteins());
        values.put(COL_FATS, user.getFats());
        values.put(COL_CARBS, user.getCarbs());

        String foodJson = gson.toJson(user.getListOfFood());
        String programJson = gson.toJson(user.getProgramList());

        values.put(COL_FOOD_LIST, foodJson);
        values.put(COL_PROGRAM_LIST, programJson);

        db.update(TABLE_USERS, values, COL_EMAIL + "=?", new String[]{user.getEmail()});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            // Δημιουργία της νέας δομής των δεδομένων και ενημέρωση της βάσης
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }
    public static User getUserByEmail(Context context, String email) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        return dbHelper.getUserByEmail(email);
    }
}
