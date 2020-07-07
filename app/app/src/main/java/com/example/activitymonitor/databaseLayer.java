package com.example.activitymonitor;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public interface databaseLayer {

    public  void saveExerciseData(String uid, String id, long time, boolean done, FirebaseFirestore db);

    public  ArrayList<String> getRelevantActivityIds(String uid, FirebaseFirestore db);
}
