package com.example.activitymonitor.model;

import com.google.common.collect.Sets;

/**
 * Activity() class which is used to store Activity objects retreived from the firebase cloud storage
 */
public class Activity {
    private String ActivityName; //Name of Activity
    private String Creator;//Coach who created activity
    private String InstructionalNotes;//Notes for particular Activity
    private String Reps;//Amount of repetitions for particular activity
    private String Sets;//Amount of sets for particular activity


    /**
     * Activity() empty constructor for FireStore
     */
    public Activity() {}


    /**
     * @param activityName Name of Activity
     * @param creator Coach who created activity
     * @param instructionalNotes Notes for particular Activity
     * @param reps Amount of repetitions for particular activity
     * @param sets Amount of sets for particular activity
     */
    public Activity(String activityName, String creator, String instructionalNotes, String reps, String sets) {
        ActivityName = activityName;
        Creator = creator;
        InstructionalNotes = instructionalNotes;
        Reps = reps;
        Sets = sets;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getInstructionalNotes() {
        return InstructionalNotes;
    }

    public void setInstructionalNotes(String instructionalNotes) {
        InstructionalNotes = instructionalNotes;
    }

    public String getReps() {
        return Reps;
    }

    public void setReps(String reps) {
        Reps = reps;
    }

    public String getSets() {
        return Sets;
    }

    public void setSets(String sets) {
        Sets = sets;
    }
}
