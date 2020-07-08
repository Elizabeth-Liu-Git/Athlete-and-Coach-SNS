package com.example.activitymonitor;

/**
 * HistoricalActivity allows for the saving of items retrieved from firebase
 */
public class HistoricalActivity {

    private String ActivityName; //Name of Activity
    private String athlete;//Coach who created activity
    private String InstructionalNotes;//Notes for particular Activity
    private String Reps;//Amount of repetitions for particular activity
    private String Sets;//Amount of sets for particular activity
    private boolean complete;//if the exercise is complete
    private int timer;//how long the exercise took (seconds)

    /**
     * @param activityName - name of activity
     * @param athlete - name of athlete that activity is assigned to
     * @param instructionalNotes - notes from coach
     * @param reps - number of reps
     * @param sets - number of sets
     * @param complete - boolean if the activity has been finished
     * @param timer - how long activity took
     */
    public HistoricalActivity (String activityName, String athlete, String instructionalNotes, String reps, String sets, Boolean complete, int timer) {
        ActivityName = activityName;
        this.athlete = athlete;
        InstructionalNotes = instructionalNotes;
        Reps = reps;
        Sets = sets;
        this.complete = complete;
        this.timer = timer;
    }

    /**
     * getters and setters
     */
    public String getAthleteName(){
        return athlete;
    }
    public String getActivityName(){
        return ActivityName;
    }

    public String getInstructionalNotes() {
        return InstructionalNotes;
    }

    public String getReps() {
        return Reps;
    }

    public String getSets() {
        return Sets;
    }

    public boolean isComplete() {
        return complete;
    }

    //returns time in minutes
    public int getTimer() {
        return timer/60;
    }
}
