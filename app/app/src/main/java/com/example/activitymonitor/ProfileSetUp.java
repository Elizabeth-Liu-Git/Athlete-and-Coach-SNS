package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileSetUp extends AppCompatActivity {
    private static String USERID = "";
    FirebaseFirestore db;
    EditText first, last;
    String firstString, lastString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        db = FirebaseFirestore.getInstance();

        first = findViewById(R.id.editTextTextPersonName3);
        last = findViewById(R.id.editTextTextPersonName4);
        final CheckBox checkBox = findViewById(R.id.checkBox);

        Button confirmExerciseButton = findViewById(R.id.button2);
        confirmExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null){
                    USERID = currentUser.getUid();
                    firstString = first.getText().toString();
                    lastString = last.getText().toString();

                    if(firstString.equals("") || lastString.equals("")){
                        Toast.makeText(ProfileSetUp.this, "Please Fill Out The Form",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(checkBox.isChecked()) {
                            db.collection("Users").document(USERID).update("First Name", firstString, "Last Name", lastString, "userType", 1);
                            coachPage();
                        }
                        else{
                            db.collection("Users").document(USERID).update("First Name", firstString, "Last Name", lastString, "userType", 2);
                            athletePage();
                        }
                    }
                }

            }
        });
    }
    private void athletePage() {
        Intent intent = new Intent(this, AthletePage.class);
        this.startActivity(intent);
    }

    private void coachPage() {
        Intent intent = new Intent(this, CoachPage.class);
        this.startActivity(intent);
    }
}
