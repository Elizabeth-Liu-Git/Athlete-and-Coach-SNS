package com.example.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.provider.SyncStateContract.Helpers.update;
import static com.google.firebase.firestore.FieldValue.delete;

public class AcceptRelationship extends AppCompatActivity {
    private FirebaseFirestore db;
    private static final String TAG = "AcceptRelationship";
    private String theAthleteID;
    private String theDocumentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_relationship);
        final Spinner pendingAthleteDropDown = findViewById(R.id.spinner);

        ArrayList<String> athleteIDs = new ArrayList<>();
        ArrayList<String> athleteNames = new ArrayList<>();
        ArrayList<String> documentIDs = new ArrayList<>();
        String pleaseSelectAnAthlete = "These are the athlete who wants to train with you";
        String DoNotSelectAnyAthlete = "I do not want to select any athlete, go back to main page";

        athleteIDs.add(pleaseSelectAnAthlete);
        athleteNames.add(pleaseSelectAnAthlete);
        documentIDs.add(pleaseSelectAnAthlete);

        athleteIDs.add(DoNotSelectAnyAthlete);
        athleteNames.add(DoNotSelectAnyAthlete);
        documentIDs.add(DoNotSelectAnyAthlete);

        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(SignIn.USERID)
                .collection("Athlete")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                String userID = document.get("Id").toString();
                                boolean approved = document.getBoolean("approval");
                                athleteIDs.add(userID);
                                documentIDs.add(documentId);
                                if(!approved) {
                                    db.collection("Users")
                                            .document(userID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        String name = document.get("firstName").toString() + " " + document.get("lastName").toString();
                                                        athleteNames.add(name);
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    }
                });

        ArrayAdapter<String> adapterAthleteNames = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, athleteNames);
//set the spinners adapter to the previously created one.
        pendingAthleteDropDown.setAdapter(adapterAthleteNames);
        pendingAthleteDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long l) {

                String item = pendingAthleteDropDown.getSelectedItem().toString();
                switch(item) {
                    case "I do not want to select any athlete, go back to main page":
                        Intent newExercise = new Intent(AcceptRelationship.this, CoachPage.class);
                        startActivityForResult(newExercise,1);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button acceptRelationButton = findViewById(R.id.button3);

        acceptRelationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String athleteNameString = pendingAthleteDropDown.getSelectedItem().toString();

                if (athleteNameString.equals("These are the athlete who wants to train with you")) {
                    Toast.makeText(AcceptRelationship.this, "Please select an athlete or exit with the second select option",
                            Toast.LENGTH_SHORT).show();
                } else {
                    theAthleteID = athleteIDs.get(athleteNames.indexOf(athleteNameString));
                    theDocumentID = documentIDs.get(athleteNames.indexOf(athleteNameString));

                    db.collection("Users")
                            .document(theAthleteID)
                            .update("approval", 1);
                    db = FirebaseFirestore.getInstance();
                    db.collection("Users")
                            .document(SignIn.USERID)
                            .collection("Athlete")
                            .document(theDocumentID)
                            .update("approval", true);

                    Toast.makeText(AcceptRelationship.this, "You have accepted the request",
                            Toast.LENGTH_SHORT).show();

                    Intent newExercise = new Intent(AcceptRelationship.this, CoachPage.class);
                    startActivityForResult(newExercise,1);

                }


            }
        });
        Button denyRelationButton = findViewById(R.id.button4);
        denyRelationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String athleteNameString = pendingAthleteDropDown.getSelectedItem().toString();

                if (athleteNameString.equals("These are the athlete who wants to train with you")) {
                    Toast.makeText(AcceptRelationship.this, "Please select an athlete or exit with the second select option",
                            Toast.LENGTH_SHORT).show();
                } else {
                    theAthleteID = athleteIDs.get(athleteNames.indexOf(athleteNameString));
                    theDocumentID = documentIDs.get(athleteNames.indexOf(athleteNameString));
                    System.out.println(theDocumentID);
                    db.collection("Users")
                            .document(theAthleteID)
                            .update("approval", 0, "Coach", "");

                    db.collection("Users")
                            .document(SignIn.USERID)
                            .collection("Athlete")
                            .document(theDocumentID)
                            .delete();


                    Toast.makeText(AcceptRelationship.this, "You have rejected the request",
                            Toast.LENGTH_SHORT).show();
                    Intent newExercise = new Intent(AcceptRelationship.this, CoachPage.class);
                    startActivityForResult(newExercise,1);
                }


            }
        });
    }
}