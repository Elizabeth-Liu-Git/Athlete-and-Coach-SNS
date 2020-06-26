package com.example.activitymonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

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

/**
 * Calender page, allows coach to pick dates that activity's were
 * assigned to
 */
public class CoachCalendar extends AppCompatActivity {

    static String date;
    CalendarView calendarView;
    Button button;
    private FirebaseFirestore db;
    static ArrayList<String> exerciseNameArray = new ArrayList<>();
    static ArrayList<String> athleteNameArray = new ArrayList<>();
    static ArrayList<String> noteArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_calendar);
        button = findViewById(R.id.button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendarView = (CalendarView) findViewById(R.id.calender);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + (month+1) + "/" + year;
                getExercise1();

            }
        });

        final Intent intent = new Intent (this, PastActivity.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteArray.isEmpty()) showDialog();
               else startActivity(intent);

            }
        });
    }
    /**
     * retrieves assignedExercise activity from firebase and saves to to arrayList
     */
    private void getExercise1(){
        exerciseNameArray.clear();
        athleteNameArray.clear();
        noteArray.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("Assigned Exercise")
                .whereEqualTo("Date", date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                exerciseNameArray.add(document.get("Exercise name").toString());
                                athleteNameArray.add(document.get("Athlete name").toString());
                                noteArray.add(document.get("Coach notes").toString());
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
