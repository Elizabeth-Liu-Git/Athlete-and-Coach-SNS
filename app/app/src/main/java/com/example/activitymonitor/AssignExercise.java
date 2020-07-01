package com.example.activitymonitor;
/*
 *Project: CSCI 3130 Acitivity App
 * Author: Rita Liu, Zacary Long, Android Tutorial, Nicolas Tyler
 * //https://www.tutlane.com/tutorial/android/android-datepicker-with-examples
   //https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
 * Time : June 11, 2020
 */



import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AssignExercise extends AppCompatActivity {
    private FirebaseFirestore db;

    String exerciseNameString, atheleteNameString, noteString;
    EditText note;
    private static final String TAG = "MainActivity";

    DatePickerDialog picker;
    EditText eText;
    TextView tvw;
    String getDateString;
    ArrayList <String> atheletes = new ArrayList<>();
    ArrayList <String> exercises = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_exercise);
        final Spinner exerciseDropDown = findViewById(R.id.exercisedropdown);
        final Spinner atheleteDropDown = findViewById(R.id.athlete);
//a list of scanner needs to be implemented

        String element1 = "choose an athlete";
        atheletes.add(element1);

        String element2 = "choose an exercise";
        String newExercise = "create a new exercise";
        exercises.add(element2);
        exercises.add(newExercise);

        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("userType", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("firstName").toString() + " " + document.get("lastName").toString();
                                atheletes.add(name);
                            }
                        }
                    }
                });
        db.collection("Activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("ActivityName").toString();
                                exercises.add(name);
                            }
                        }
                    }
                });

        ArrayAdapter<String> adapterexercise = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, exercises);
//set the spinners adapter to the previously created one.
        exerciseDropDown.setAdapter(adapterexercise);
        exerciseDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long l) {

                String item = exerciseDropDown.getSelectedItem().toString();
                switch(item) {
                    case "create a new exercise":
                        Intent newExercise = new Intent(AssignExercise.this, CreateExercise.class);
                        startActivityForResult(newExercise,1);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//create a list of items for the spinner.
//
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterathlete = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, atheletes);
//set the spinners adapter to the previously created one.
        atheleteDropDown.setAdapter(adapterathlete);

        eText=(EditText) findViewById(R.id.editText1);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AssignExercise.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                getDateString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Button confirmExerciseButton = findViewById(R.id.button2);
        confirmExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseNameString = exerciseDropDown.getSelectedItem().toString();
                atheleteNameString = atheleteDropDown.getSelectedItem().toString();
                note = findViewById(R.id.NoteBox);
                noteString = note.getText().toString();
                showDialog();
            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    public void showDialog() {
        Map<String, Object> AssignedExercise = new HashMap<>();
        AssignedExercise.put("Exercise name", exerciseNameString);
        AssignedExercise.put("Coach notes", noteString);
        AssignedExercise.put("Date", getDateString);
        AssignedExercise.put("Athlete name", atheleteNameString);

// Add a new document with a generated ID
        db.collection("Assigned Exercise")
                .add(AssignedExercise)
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
        AlertDialog.Builder window = new AlertDialog.Builder(AssignExercise.this);
        window.setTitle("Please Confirm Exercise Below")
                .setMessage("Exercise name: " + exerciseNameString + '\n' + "Coach notes: " + noteString+
                        '\n' + "Athlete name: " +  atheleteNameString+ '\n' + "Date " + getDateString+ '\n')
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goBack();
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog alert = window.create();
        alert.show();

    }
    private void goBack(){
        Intent intent = new Intent (this, CoachPage.class);
        startActivity(intent);
    }
        }
