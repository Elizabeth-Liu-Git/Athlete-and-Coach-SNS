package com.example.activitymonitor;

public interface databaseLayer {

    public  void saveExerciseData(String uid, String id, long time, boolean done);

    public void readRelevantActivityIds(AsynchCallback asynchCallback);
}
