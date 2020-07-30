package com.example.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateRelationship extends AppCompatActivity {
private FirebaseFirestore db;
private static final String TAG = "CreateRelationship";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_relationship);
        ArrayList<String> coaches = new ArrayList<>();
        ArrayList<String> coachIDs = new ArrayList<>();
        String pleaseSelectACoach = "Please select a coach";
        String iWillAddCoachLater = "I will add coach later";
        coaches.add(pleaseSelectACoach);
        coaches.add(iWillAddCoachLater);
        coachIDs.add(pleaseSelectACoach);
        coachIDs.add(iWillAddCoachLater);
        final Spinner coachDropDown = findViewById(R.id.coachNames);
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("userType", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("firstName").toString() + " " + document.get("lastName").toString();
                                String userID = document.getId();
                                coachIDs.add(userID);
                                coaches.add(name);
                            }
                        }
                    }
                });
        ArrayAdapter<String> adaptercoach = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, coaches);
//set the spinners adapter to the previously created one.
        coachDropDown.setAdapter(adaptercoach);
        Button confirmCoachButton = findViewById(R.id.confirm);
        confirmCoachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thecoachName = coachDropDown.getSelectedItem().toString();
                // String to be scanned to find the pattern.
                if (thecoachName.equals("Please select a coach")) {
                    Toast.makeText(CreateRelationship.this, "Please select a coach",
                            Toast.LENGTH_SHORT).show();
                } else if (thecoachName.equals("I will add coach later")){
                        NotAssignCoath ();
                    }
                    else{Toast.makeText(CreateRelationship.this, "You choosed a coach",
                            Toast.LENGTH_SHORT).show();
                    String thecoachID = coachIDs.get(coaches.indexOf(thecoachName));
                    final Map<String, Object> Athlete = new HashMap<>();
                    String theAthleteID = SignIn.USERID;
                    Athlete.put("approval", false);
                    Athlete.put("Id", theAthleteID);
                    db.collection("Users")
                            .document(thecoachID)
                            .collection("Athlete")
                            .add(Athlete)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                    db.collection("Users")
                            .document(SignIn.USERID)
                            .update("Coach", thecoachID,
                                    "approval", 2);

                    NotAssignCoath ();

                }
            }
        });
    }
    private void NotAssignCoath (){
        Intent intent = new Intent(CreateRelationship.this, AthletePage.class);
        startActivity(intent);
    }

}