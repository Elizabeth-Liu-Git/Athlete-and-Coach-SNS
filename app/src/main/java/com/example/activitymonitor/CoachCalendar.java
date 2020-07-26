package com.example.activitymonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Calender page, allows coach to pick dates that activity's were
 * assigned to
 */
public class CoachCalendar extends AppCompatActivity {

    static String date;
    Button button;
    private FirebaseFirestore db;
    static ArrayList<String> exerciseNameArray = new ArrayList<>();
    static ArrayList<String> athleteNameArray = new ArrayList<>();
    static ArrayList<String> noteArray = new ArrayList<>();
    static ArrayList<String> dateArray = new ArrayList<>();
    static ArrayList<String> athleteSpinnerArray = new ArrayList<>();
    public String selectedAthlete;
    public String selectedFullAthlete;

    ArrayList <String> userIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_calendar);
        button = findViewById(R.id.button);
        athleteSpinnerArray.clear();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Creates the drop down to filer by completed or not exercises
        Spinner completedSpinner = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[] {"Completed Exercises", "Not Completed Exercises"};
        ArrayAdapter<String> completedAdaptor = new ArrayAdapter<String>(CoachCalendar.this, android.R.layout.simple_spinner_dropdown_item, items);
        completedAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        completedSpinner.setAdapter(completedAdaptor);

        //Creates drop down to filter by athlete
        Spinner athleteSpinner = (Spinner) findViewById(R.id.spinnerAthlete);
        athleteSpinnerArray.add("Choose an Athlete");
        getNamesFirebase();
        ArrayAdapter<String> atheleteAdaptor = new ArrayAdapter<String>(CoachCalendar.this, android.R.layout.simple_spinner_dropdown_item, athleteSpinnerArray);
        atheleteAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        athleteSpinner.setAdapter(atheleteAdaptor);
        athleteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //saves the athlete name selected and gets all exercises from firebase
                selectedFullAthlete = athleteSpinner.getSelectedItem().toString();
                if (selectedFullAthlete.equals("Choose an Athlete")){}
                else {
                    String[] parts = selectedFullAthlete.split(" ");
                    selectedAthlete = parts[1];
                    getUID();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Intent intent = new Intent (this, PastActivity.class);
        //Brings user to new page with athletes exercises
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExercise1();
                //allows for delay to get info from DB
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (noteArray.isEmpty()) showDialog();
                        else startActivity(intent);
                    }
                }, 500);
            }
        });
    }

    /**
     * retrieves all the names of athlete users present in the DB
     */
    private void getNamesFirebase(){
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("userType", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                athleteSpinnerArray.add(document.get("firstName").toString() + " " + document.get("lastName").toString());
                            }
                        }
                    }
                });
    }

    /**
     * retrieves assignedExercise activity from firebase and saves to to arrayList
     */

    private void getExercise1(){
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(userIDs.get(0))
                .collection("AssignedExercise")
                //.whereEqualTo("lastName", selectedAthlete)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                exerciseNameArray.add(document.get("Exercise name").toString());
                                athleteNameArray.add(selectedFullAthlete);
                                noteArray.add(document.get("Coach notes").toString());
                                dateArray.add(document.get("Date").toString());
                            }
                        }
                    }
                });
    }

    /**
     * gets the UID of the selected user
     */
    private void getUID (){
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("lastName", selectedAthlete)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                String userID = document.getId();
                                userIDs.add(userID);
                                System.out.println(userID);
                            }
                        }
                    }
                });
    }

    /**
     * Shows dialog box if day selected has no activity assigned
     */
    public void showDialog() {
        AlertDialog.Builder window = new AlertDialog.Builder(CoachCalendar.this);
        window.setTitle("")
                .setMessage("There are NO activities assigned on this day")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alert = window.create();
        alert.show();
    }
}
