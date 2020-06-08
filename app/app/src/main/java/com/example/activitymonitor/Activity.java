package com.example.activitymonitor;

import com.google.common.collect.Sets;

public class Activity {
    private String ActivityName;
    private String Creator;
    private String InstructionalNotes;
    private String Reps;
    private String Sets;


    public Activity() {}


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
