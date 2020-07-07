package com.example.activitymonitor;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public interface databaseLayer {

    public  void saveExerciseData(String uid, String id, long time, boolean done);

    public  ArrayList<String> getRelevantActivityIds(String uid);
}
