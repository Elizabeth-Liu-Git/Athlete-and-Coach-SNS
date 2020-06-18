package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

/**
 * StartExercise()
 * Contains the screen which displays the timer and allows an athlete to start a particular exercise and record data
 */
public class StartExercise extends AppCompatActivity {

    private Chronometer exerciseChrono; //Chronometer object (stopwatch)
    private long offSet;//Offset used to calculate duration of the exercise

    protected static boolean currentlyExercising = false;//Boolean that indicates whether the exercise is currently occurring
    protected static boolean doneExercise = false; //Boolean that indicates whether the exercise is done

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);

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
        if(!currentlyExercising){
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
    public static void finishExercise(View v){
        doneExercise=true;
        //TODO implement functionality to store collected data in FireStore

    }

    /**
     * pauseExercise()
     * Method used to pause the chronometer and data collection
     * @param v View needed for chronometer
     */
    public void pauseExercise(View v){

        if(currentlyExercising){
            exerciseChrono.stop();
            offSet = SystemClock.elapsedRealtime()- exerciseChrono.getBase();
            currentlyExercising =false;
        }

    }
}