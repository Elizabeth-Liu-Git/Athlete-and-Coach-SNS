package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CoachPage extends AppCompatActivity implements OnClickListener {

    Button buttonCreateExercise;
    Button buttonAssignExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_page);

        buttonCreateExercise = findViewById(R.id.buttonNewExercise);
        buttonCreateExercise.setOnClickListener(this);
        buttonAssignExercise = findViewById(R.id.buttonAssignExercise);
        buttonAssignExercise.setOnClickListener(this);
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.buttonNewExercise:
                Intent createIntent = new Intent (this, CreateExercise.class);
                startActivity(createIntent);
                break;
            case R.id.buttonAssignExercise:
                Intent assignIntent = new Intent (this, AssignExercise.class);
                startActivity(assignIntent);
                break;
        }
    }
}
