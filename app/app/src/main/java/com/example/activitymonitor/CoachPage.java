package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


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
    public void openNewExercise(){
        Intent intent = new Intent (this, CreateExercise.class);
        startActivity(intent);
    }
}
