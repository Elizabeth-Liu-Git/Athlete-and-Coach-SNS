package com.example.activitymonitor.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    // Auto-generated ID for each User
    @DocumentId
    private String UserID;

    /*
    Map object to store keys to MessageCollection objects.
    e.g. UID of MessageCollection : true/false
     */
    //private Map<String, Object> msgConvo;

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
        //msgConvo = new HashMap<>();
    }

    @Exclude
    public String getUserID() {
        return UserID;
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

    /*public Map<String, Object> getMsgConvo() {
        return msgConvo;
    }

    public void setMsgConvo(Map<String, Object> msgConvo) {
        this.msgConvo = msgConvo;
    }*/
}