package com.example.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * StartExercise()
 * Contains the screen which displays the timer and allows an athlete to start a particular exercise and record data
 */
public class StartExercise extends AppCompatActivity{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Chronometer exerciseChrono; //Chronometer object (stopwatch)
    private long offSet;//Offset used to calculate duration of the exercise

    protected static boolean currentlyExercising = false;//Boolean that indicates whether the exercise is currently occurring
    protected static boolean doneExercise = false; //Boolean that indicates whether the exercise is done

    private TextView exercise_name, reps, sets, notes_from_coach;
    private Activity currentActivity;

    private static final String TAG = "StartExercise";


    private databaseInteraction dataB= new databaseInteraction(db);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);



        //Setting the text fields
        exercise_name = findViewById(R.id.ip_exercise_name);
        reps = findViewById(R.id.ip_exercise_reps);
        sets = findViewById(R.id.ip_exercise_sets);
        notes_from_coach = findViewById(R.id.ip_exercise_notes);

        //Getting Activity Object that was Clicked on
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentActivity = (Activity) getIntent().getSerializableExtra("ACTIVITY"); //Obtaining data

            exercise_name.setText(currentActivity.getActivityName());
            reps.setText(currentActivity.getReps() );
            sets.setText(currentActivity.getSets() );
            notes_from_coach.setText(currentActivity.getInstructionalNotes() );

        }

        //Initializing the timer
        exerciseChrono = findViewById(R.id.exercise_chrono);
        exerciseChrono.setFormat("Exercise Time: %s");
        exerciseChrono.setBase(SystemClock.elapsedRealtime());

        //Listener that goes off every 10 second
        exerciseChrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer exerciseChrono){
                //Every 10 s
                if((SystemClock.elapsedRealtime() - exerciseChrono.getBase()) >= 10000){

                    //TODO collecting biometrics every 10s
                }
            }
        });
    }

    /**
     * startExercise()
     * Method used to start the exercise, including the stopwatch
     * @param v View needed for chronometer
     */
    public void startExercise(View v){
        //When Chronometer not running
        if(!currentlyExercising && !doneExercise){
            exerciseChrono.setBase(SystemClock.elapsedRealtime() - offSet);
            exerciseChrono.start();
            currentlyExercising =true;
        }
    }

    /**
     * finishExercise()
     * Method used once exercise is completed
     * @param v View needed for chronometer
     */
    public void finishExercise(View v){
        long exerciseTime = SystemClock.elapsedRealtime()- exerciseChrono.getBase();
        String activity_id_to_be_saved_to = currentActivity.getDocumentId();
        doneExercise=true;
        dataB.saveExerciseData(SignIn.USERID, activity_id_to_be_saved_to, exerciseTime, doneExercise);

    }


    /**
     * pauseExercise()
     * Method used to pause the chronometer and data collection
     * @param v View needed for chronometer
     */
    public void pauseExercise(View v){

        if(currentlyExercising && !doneExercise){
            exerciseChrono.stop();
            offSet = SystemClock.elapsedRealtime()- exerciseChrono.getBase();
            currentlyExercising =false;
        }

    }
}
