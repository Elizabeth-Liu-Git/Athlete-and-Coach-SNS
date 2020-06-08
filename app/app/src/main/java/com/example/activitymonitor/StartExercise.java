package com.example.activitymonitor;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class StartExercise extends AppCompatActivity implements SensorEventListener{
    SensorManager sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    Sensor stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);
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



