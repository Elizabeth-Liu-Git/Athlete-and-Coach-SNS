package com.example.activitymonitor;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

/**
 * StartExercise()
 * Contains the screen which displays the timer and allows an athlete to start a particular exercise and record data
 */
public class StartExercise extends AppCompatActivity implements SensorEventListener {

    private Chronometer exerciseChrono; //Chronometer object (stopwatch)
    private long offSet;//Offset used to calculate duration of the exercise

    protected static boolean currentlyExercising = false;//Boolean that indicates whether the exercise is currently occurring
    protected static boolean doneExercise = false; //Boolean that indicates whether the exercise is done

    SensorManager sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    Sensor stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
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
        start(v);
    }

    /**
     * finishExercise()
     * Method used once exercise is completed
     * @param v View needed for chronometer
     */
    public void finishExercise(View v){
        doneExercise=true;
        //TODO implement functionality to store collected data in FireStore
        stop(v);

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
        show(v);
    }
    @Override
    protected void onResume() {

        super.onResume();

        sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }
    @Override
    protected void onStop() {
        super.onStop();
        sManager.unregisterListener(this, stepSensor);
    }
    private long steps = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }
        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.  Unlike
     * onSensorChanged(), this is only called when this accuracy value changes.
     *
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    //function to determine the distance run in kilometers using average step length for men and number of steps
    public float getDistanceRun(long steps){
        float distance = (float)(steps*78)/(float)100000;
        return distance;
    }
    public void start(View view){
        TextView tv = findViewById(R.id.tv);
        onResume();
        tv.setText(String.valueOf(getDistanceRun(steps)));
    }
    public void stop(View view){
        TextView tv = findViewById(R.id.tv);
        tv.setText(String.valueOf(getDistanceRun(steps)));
        onStop();
    }
    public void show(View view){
        TextView tv = findViewById(R.id.tv);
        tv.setText(String.valueOf(getDistanceRun(steps)));
    }
}

