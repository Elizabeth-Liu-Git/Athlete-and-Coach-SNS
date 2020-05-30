package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Coaches page, this allows the coach to create a new exercise
 */

public class CoachPage extends AppCompatActivity {

    Button buttonCreateExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_page);

        buttonCreateExercise = findViewById(R.id.buttonNewExercise);
        buttonCreateExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewExercise();
            }
        });
    }

    /**
     * Takes the user to a new activity (CreateExercise) when clicked
     */
    public void openNewExercise(){
        Intent intent = new Intent (this, CreateExercise.class);
        startActivity(intent);
    }
}
