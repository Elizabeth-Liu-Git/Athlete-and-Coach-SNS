package com.example.activitymonitor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class databaseInteraction implements databaseLayer{
    private FirebaseFirestore db;
    private static final String TAG = "database";

    /**
     * @param id the id of the document under which a collection of exercise instances will be saved
     * @param time length of exercise as a long
     * @param done whether the exercise is complete
     */
    public void saveExerciseData(String uid, String id, long time, boolean done){
        //Instance where completed exercise data is to be stored
        final Map<String, Object> CompletedExerciseInstance = new HashMap<>();

        CompletedExerciseInstance.put("ExerciseTimeLength", time);
        CompletedExerciseInstance.put("ExerciseDone", done);


        //Database interaction to save data
        db.collection("Users")
                .document(uid)
                .collection("AssignedExercise")
                .document(id)
                .collection("ExerciseSessions")
                .add(CompletedExerciseInstance)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document for exercise session instance", e);
                    }
                });

    }

}
