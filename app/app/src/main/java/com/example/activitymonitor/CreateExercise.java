package com.example.activitymonitor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateExercise extends AppCompatActivity {

    String exerciseNameString, repNumString, setNumString, noteString, phoneNumString;
    EditText exerciseName, repNum, setNum, note, phoneNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        exerciseName = findViewById(R.id.ExerciseNameBox);
        repNum = findViewById(R.id.RepNumBox);
        setNum = findViewById(R.id.SetNumBox);
        note = findViewById(R.id.Notes);
        phoneNum = findViewById(R.id.AthleteNumber);



        Button confirmExerciseButton = findViewById(R.id.button);
        confirmExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseNameString = exerciseName.getText().toString();
                repNumString = repNum.getText().toString();
                setNumString = setNum.getText().toString();
                noteString = note.getText().toString();
                phoneNumString = phoneNum.getText().toString();
                showDialog();
            }
        });
    }

    public void showDialog() {

        AlertDialog.Builder window = new AlertDialog.Builder(CreateExercise.this);
        window.setTitle("Please Confirm Info Below")
                .setMessage("Exercise name: " + exerciseNameString + '\n' + "Reps: " + repNumString+
                        '\n' + "Sets: " + setNumString + '\n' + "Coach notes: " + noteString+
                        '\n' + "Athlete Phone Number: " + phoneNumString)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goBack();
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog alert = window.create();
        alert.show();

    }

    private void goBack(){
        Intent intent = new Intent (String.valueOf(CreateExercise.class));
        startActivity(intent);
    }
}
