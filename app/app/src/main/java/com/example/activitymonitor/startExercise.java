package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

public class StartExercise extends AppCompatActivity {

    private Chronometer exerciseChrono; //Chronometer to time activity
    private boolean chronoRunning;//Boolean that keeps track of whether the chronometer is running
    private long offSet;//Offset for calculating the amount of time elapsed
    private boolean doneExercise = false;//Boolean that keeps track of whether the exercise has been completed


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
     * Serves to start the exercise and start the exercise timer
     * @param v Contains the view
     *
     */
    public void startExercise(View v){
        //When Chronometer not running
        if(!chronoRunning){
            exerciseChrono.setBase(SystemClock.elapsedRealtime() - offSet);
            exerciseChrono.start();
            chronoRunning=true;
        }

    }

    /**
     * finishExercise()
     * Serves to finish and submit the exercise information to the database
     * @param v contains View
     */
    public void finishExercise(View v){
        //TODO Method for button that submits exercise

    }

    /**
     * pauseExercise()
     * serves to pause the exercise and pause the timer
     * @param v contains View
     */
    public void pauseExercise(View v){

        if(chronoRunning){
            exerciseChrono.stop();
            offSet = SystemClock.elapsedRealtime()- exerciseChrono.getBase();

            chronoRunning=false;
        }
    }
}
