package com.example.activitymonitor.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    // Auto-generated ID for each User
    private String UserID;

    // Set of keys: UserID of the other user and the CollectionID of the MessageCollection
    HashMap<String, String> keys = new HashMap<>();

    private int Age;
    private String FirstName;
    private String LastName;
    private String PhoneNumber;
    private int UserType;
    private double Weight;

    public User() {}

    public User(int age, String firstName, String lastName, String phoneNumber, int userType, double weight) {
        Age = age;
        FirstName = firstName;
        LastName = lastName;
        PhoneNumber = phoneNumber;
        UserType = userType;
        Weight = weight;
    }

    @Override
    @NonNull
    public String toString() {
        return FirstName + " " + LastName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public HashMap<String, String> getKeys() {
        return keys;
    }

    public void setKeys(HashMap<String, String> keys) {
        this.keys = keys;
    }

}
