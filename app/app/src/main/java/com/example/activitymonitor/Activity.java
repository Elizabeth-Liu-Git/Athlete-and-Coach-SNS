package com.example.activitymonitor;

public class Activity {
    private String ActivityName;
    private String Creator;
    private String InstructionalNotes;
    private int Reps;
    private int Sets;

    public Activity(String activityName, String creator, String instructionalNotes, int reps, int sets) {
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

    public int getReps() {
        return Reps;
    }

    public void setReps(int reps) {
        Reps = reps;
    }

    public int getSets() {
        return Sets;
    }

    public void setSets(int sets) {
        Sets = sets;
    }
}
