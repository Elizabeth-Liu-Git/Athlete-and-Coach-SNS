package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

public class StartExercise extends AppCompatActivity {

    private Chronometer exerciseChrono;
    private boolean chronoRunning;
    private long offSet;
    private boolean doneExercise = false;

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

    public void startExercise(View v){
        //When Chronometer not running
        if(!chronoRunning){
            exerciseChrono.setBase(SystemClock.elapsedRealtime() - offSet);
            exerciseChrono.start();
            chronoRunning=true;
        }

    }
    public void finishExercise(View v){
        //Method for button that submits exercise

    }

    public void pauseExercise(View v){

        if(chronoRunning){
            exerciseChrono.stop();
            offSet = SystemClock.elapsedRealtime()- exerciseChrono.getBase();

            chronoRunning=false;
        }
    }
}
