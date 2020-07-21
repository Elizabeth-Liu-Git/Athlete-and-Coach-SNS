package com.example.activitymonitor.model;

public class AssignedActivity {
    private String ActivityID;
    private String CoachNotes;
    private String Date;
    private String ExerciseName;

    private Boolean Complete;

    private String UserID;
    private String CoachID;


    public AssignedActivity() {
        //no arg constructor for firestore
    }

    public AssignedActivity(String activityID, String coachNotes, String date, String exerciseName, Boolean complete, String userID, String coachID) {
        ActivityID = activityID;
        CoachNotes = coachNotes;
        Date = date;
        ExerciseName = exerciseName;
        Complete = complete;
        UserID = userID;
        CoachID = coachID;
    }

    public String getActivityID() {
        return ActivityID;
    }

    public String getCoachNotes() {
        return CoachNotes;
    }

    public String getDate() {
        return Date;
    }

    public String getExerciseName() {
        return ExerciseName;
    }

    public Boolean getComplete() {
        return Complete;
    }

    public String getUserID() {
        return UserID;
    }

    public String getCoachID() {
        return CoachID;
    }
}
