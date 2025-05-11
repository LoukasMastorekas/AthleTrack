package com.example.fitnessapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Program implements Serializable {

    String name;
    int id;
    private static int idCounter = 0;

    ArrayList<FullExercise> fullExercises;

    public Program(String name, ArrayList<FullExercise> fullExercises) {
        this.id = idCounter++;  // Μοναδικό id
        this.name = name;
        this.fullExercises = fullExercises;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FullExercise> getFullExercises() {
        return fullExercises;
    }

    public void setFullExercises(ArrayList<FullExercise> fullExercises) {
        this.fullExercises = fullExercises;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
