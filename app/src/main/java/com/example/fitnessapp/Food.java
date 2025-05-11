package com.example.fitnessapp;

import java.io.Serializable;

public class Food implements Serializable {

    private int id;
    private String name;
    private double caloriesPerGram;
    private double proteinPerGram;
    private double fatPerGram;
    private double carbsPerGram;
    private int calories;
    private int proteins;
    private int fats;
    private int carbs;



    public Food(int id, String name, double caloriesPerGram, double proteinPerGram, double fatPerGram, double carbsPerGram) {
        this.id = id;
        this.name = name;
        this.caloriesPerGram = caloriesPerGram;
        this.proteinPerGram = proteinPerGram;
        this.fatPerGram = fatPerGram;
        this.carbsPerGram = carbsPerGram;
    }
    public  Food (String name,int calories,int fats,int proteins,int carbs)
    {
        this.name=name;
        this.calories=calories;
        this.fats=fats;
        this.proteins=proteins;
        this.carbs=carbs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCaloriesPerGram() {
        return caloriesPerGram;
    }

    public void setCaloriesPerGram(double caloriesPerGram) {
        this.caloriesPerGram = caloriesPerGram;
    }

    public double getProteinPerGram() {
        return proteinPerGram;
    }

    public void setProteinPerGram(double proteinPerGram) {
        this.proteinPerGram = proteinPerGram;
    }

    public double getFatPerGram() {
        return fatPerGram;
    }

    public void setFatPerGram(double fatPerGram) {
        this.fatPerGram = fatPerGram;
    }

    public double getCarbsPerGram() {
        return carbsPerGram;
    }

    public void setCarbsPerGram(double carbsPerGram) {
        this.carbsPerGram = carbsPerGram;
    }

    // ΝΕΕΣ μέθοδοι ανά 100 γραμμάρια
    public double getCaloriesper100() {
        return caloriesPerGram * 100;
    }
    public double getProteinper100() {
        return proteinPerGram * 100;
    }

    public double getFatper100() {
        return fatPerGram * 100;
    }

    public double getCarbsper100() {
        return carbsPerGram * 100;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", caloriesPerGram=" + caloriesPerGram +
                ", proteinPerGram=" + proteinPerGram +
                ", fatPerGram=" + fatPerGram +
                ", carbsPerGram=" + carbsPerGram +
                '}';
    }
}
