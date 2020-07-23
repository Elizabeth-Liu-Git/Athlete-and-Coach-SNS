package com.example.activitymonitor;

/**
 * databaseLayer()
 * this interface defines interactions with the database
 */
public interface databaseLayer {

    public  void saveExerciseData(String uid, String id, long time, boolean done);

    public void readRelevantActivityIds(AsynchCallback asynchCallback);

    public void readRelevantAssignedActivities(AsynchCallback asynchCallback);
}
