package com.example.fitnessapp;

import java.io.Serializable;

public class FullExercise  extends Exercise implements Serializable {

    private int sets;
    private int reps;

    public FullExercise(String name, int id, String description, String muscle, int sets, int reps) {
        super(name, id, description, muscle);
        this.sets = sets;
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    @Override
    public String toString() {
        return super.toString() + ", sets=" + sets + ", reps=" + reps;
    }
}

