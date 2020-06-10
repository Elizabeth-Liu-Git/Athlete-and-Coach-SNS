package com.example.activitymonitor;
//https://www.tutlane.com/tutorial/android/android-datepicker-with-examples
//https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;


public class AssignExercise extends AppCompatActivity {

    String exerciseNameString, dateString, atheleteNameString, noteString;
    EditText note;

    DatePickerDialog picker;
    EditText eText;
    TextView tvw;
    String getDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_exercise);
        final Spinner exerciseDropDown = findViewById(R.id.exercisedropdown);
//a list of scanner needs to be implemented
        String[] exercises = new String[]{"choose an exercise", "create a new exercise", "exercise 1", "exercise 2", "exercise3"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterexercise = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, exercises);
//set the spinners adapter to the previously created one.
        exerciseDropDown.setAdapter(adapterexercise);
        exerciseDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long l) {

                String item = exerciseDropDown.getSelectedItem().toString();

                switch(item) {
                    case "create a new exercise":
                        Intent newExercise = new Intent(AssignExercise.this, CreateExercise.class);
                        startActivity(newExercise);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


                Spinner atheleteDropDown = findViewById(R.id.athlete);
//create a list of items for the spinner.
        String[] atheletes = new String[]{"choose an athelete", "athelete 1", "athelete 2", "athelete 3"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterathlete = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, atheletes);
//set the spinners adapter to the previously created one.
        atheleteDropDown.setAdapter(adapterathlete);


        tvw=(TextView)findViewById(R.id.textView1);
        eText=(EditText) findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AssignExercise.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                getDateString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
//        btnGet=(Button)findViewById(R.id.button2);
//        btnGet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvw.setText("Selected Date: "+ eText.getText());
//            }
//        });
        Button confirmExerciseButton = findViewById(R.id.button2);
        confirmExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseNameString = exerciseDropDown.getSelectedItem().toString();
//                dateString = repNum.getText().toString();
                note = findViewById(R.id.NoteBox);
                noteString = note.getText().toString();
                showDialog();
            }
        });

    }

    public void showDialog() {

        AlertDialog.Builder window = new AlertDialog.Builder(AssignExercise.this);
        window.setTitle("Please Confirm Exercise Below")
                .setMessage("Exercise name: " + exerciseNameString + '\n' + "Coach notes: " + noteString+
                        '\n' + "Date " + getDateString+ '\n')
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
