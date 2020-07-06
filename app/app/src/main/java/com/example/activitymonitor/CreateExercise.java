package com.example.activitymonitor;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows the user to create a new exercise
 */
public class CreateExercise extends AppCompatActivity {

    String exerciseNameString, repNumString, setNumString, noteString,detailString;
    EditText exerciseName, repNum, setNum, note,detail;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        exerciseName = findViewById(R.id.ExerciseNameBox);
        repNum = findViewById(R.id.RepNumBox);
        setNum = findViewById(R.id.SetNumBox);
        note = findViewById(R.id.Notes);
        detail= findViewById(R.id.Detail);

        db = FirebaseFirestore.getInstance();

        Button confirmExerciseButton = findViewById(R.id.button);
        confirmExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseNameString = exerciseName.getText().toString();
                repNumString = repNum.getText().toString();
                setNumString = setNum.getText().toString();
                noteString = note.getText().toString();
                detailString = detail.getText().toString();
                showDialog();
            }
        });
    }

    /**
     * opens the pop-up window that allows the user to confirm the info they entered
     * once confirmed will add to database
     */
    public void showDialog() {
        AlertDialog.Builder window = new AlertDialog.Builder(CreateExercise.this);
        window.setTitle("Please Confirm Info Below")
                .setMessage("Exercise name: " + exerciseNameString + '\n' + "Reps: " + repNumString+
                        '\n' + "Sets: " + setNumString + '\n' + "Coach notes: " + noteString+"Details:"+detailString)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadData(exerciseNameString, repNumString, setNumString, noteString,detailString);
                        goBack();
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog alert = window.create();
        alert.show();
    }

    /**
     * Adds the data to the data base
     * @param name - the name of exercise
     * @param rep - number of reps set
     * @param set - number of sets
     * @param note - any notes that the coach may have
     */
    private void uploadData(String name, String rep, String set, String note,String detail){
        Map<String, Object> activity = new HashMap<>();
        activity.put("ActivityName", name);
        activity.put("Creator", ""); // Creator ID is left empty for now
        activity.put("Instructional Notes", note);
        activity.put("Reps", rep);
        activity.put("Sets", set);
        activity.put("Detail",detail);

        db.collection("Activities").document().set(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CreateExercise.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateExercise.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Takes user back a page
     */
    private void goBack(){
        finish();
    }
}
