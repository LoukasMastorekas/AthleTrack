package com.example.fitnessapp;

import java.io.Serializable;

public class Exercise implements Serializable {

    private String name;
    private int id;
    private String description;
    private String muscle;

    public Exercise(String name, int id, String description, String muscle) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.muscle = muscle;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", muscle='" + muscle + '\'' +
                '}';
    }
}
