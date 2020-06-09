package com.example.activitymonitor.model;

public class User {
    private int Age;
    private String FirstName;
    private String LastName;
    private String PhoneNumber;
    private int UserType;
    private double Weight;

    // TODO: 2020-06-09 How will User objects store & access CommentCollection objects?

    public User(int age, String firstName, String lastName, String phoneNumber, int userType, double weight) {
        Age = age;
        FirstName = firstName;
        LastName = lastName;
        PhoneNumber = phoneNumber;
        UserType = userType;
        Weight = weight;
    }

    public User(){

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
}
