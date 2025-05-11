package com.example.fitnessapp;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private int userId;
    private String password;
    private String email;
    private int caloriesIntake;
    private int caloriesRemaining;
    private int proteins;
    private int fats;
    private int carbs;
    private ArrayList<Food> listOfFood;
    private ArrayList<Program> programList;

    public User( int userId,String email,String password,  int caloriesIntake, int caloriesRemaining, int proteins, int fats, int carbs, ArrayList<Food> listOfFood, ArrayList<Program> programList) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.caloriesIntake = caloriesIntake;
        this.caloriesRemaining = caloriesRemaining;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
        this.listOfFood = listOfFood;
        this.programList = programList;
    }

    public User() {
        this.listOfFood = new ArrayList<>();
        this.programList = new ArrayList<>();
    }

    public ArrayList<Food> getListOfFood() {
        return listOfFood;
    }

    public void setListOfFood(ArrayList<Food> listOfFood) {
        this.listOfFood = listOfFood;
    }

    public ArrayList<Program> getProgramList() {
        return programList;
    }

    public void setProgramList(ArrayList<Program> programList) {
        this.programList = programList;
    }

    public int getUserId() {
        return userId;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCaloriesIntake() {
        return caloriesIntake;
    }

    public void setCaloriesIntake(int caloriesIntake) {
        this.caloriesIntake = caloriesIntake;
    }

    public int getCaloriesRemaining() {
        return caloriesRemaining;
    }

    public void setCaloriesRemaining(int caloriesRemaining) {
        this.caloriesRemaining = caloriesRemaining;
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
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", caloriesIntake=" + caloriesIntake +
                ", caloriesRemaining=" + caloriesRemaining +
                ", proteins=" + proteins +
                ", fats=" + fats +
                ", carbs=" + carbs +
                ", foodListSize=" + (listOfFood != null ? listOfFood.size() : 0) +
                ", programListSize=" + (programList != null ? programList.size() : 0) +
                '}';
    }

}
