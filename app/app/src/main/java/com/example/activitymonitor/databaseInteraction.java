package com.example.activitymonitor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * dataBaseInteraction class is used for various database interactions as defined by the dataBase layer interface
 */
public class databaseInteraction implements databaseLayer{
    private static final String TAG = "database";
    public FirebaseFirestore db;

    public databaseInteraction(FirebaseFirestore inDb){
        db = inDb;
    }

    /**
     * saveExerciseData()
     * @param id the id of the document under which a collection of exercise instances will be saved
     * @param time length of exercise as a long
     * @param done whether the exercise is complete
     */
    public  void saveExerciseData(String uid, String id, long time, boolean done){
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


    /**
     * readRelevantActivityIds() serves to get an arraylist of activity ids tgar are relevant (assigned) to the signed in athlete
     * @param asynchCallback pass through an asynch callback object so the data can be used once retreived frm firebase
     */
    public void readRelevantActivityIds(AsynchCallback asynchCallback){

        db.collection("Users").document(SignIn.USERID).collection("AssignedExercise")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Collecting the data from the db
                            ArrayList<String> idList = new ArrayList<>();

                            //Adding to the newly cleared arraylist of activity ids relevant to the athlete
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idList.add(document.get("Activity ID").toString());

                            }
                            asynchCallback.onCallback(idList);
                            Log.d(TAG+" idList", idList.toString());
                        }
                    }
                });


    }



}