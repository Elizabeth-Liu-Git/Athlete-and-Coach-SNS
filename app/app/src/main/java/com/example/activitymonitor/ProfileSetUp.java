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

/**
 * Sets up the user profile after the user has been authorized by the DB
 */
public class ProfileSetUp extends AppCompatActivity {
    /**
     * holds the user ID returned from getUID()
     */
    String USERID;
    /**
     * Database information in a variable
     */
    FirebaseFirestore db;

    /**
     * where the user enters their first name
     */
    EditText first;
    /**
     * where the user enters their last name
     */
    EditText last;
    /**
     * holds firstname information
     */
    String firstString;
    /**
     * holds lastname information
     */
    String lastString;

    /**
     * When the activity is loaded add listeners and functions to buttons, if user submits we check setup information
     * @param savedInstanceState the instance of the user variable used for access
     */
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
                            db.collection("Users").document(USERID).update("firstName", firstString, "lastName", lastString, "userType", 1);
                            coachPage();
                        }
                        else{
                            db.collection("Users").document(USERID).update("firstName", firstString, "lastName", lastString, "userType", 2);
                            athletePage();
                        }
                    }
                }

            }
        });
    }

    /**
     * send user to the athlete page
     */
    private void athletePage() {
        Intent intent = new Intent(this, AthletePage.class);
        this.startActivity(intent);
    }

    /**
     * send user to the coach page
     */
    private void coachPage() {
        Intent intent = new Intent(this, CoachPage.class);
        this.startActivity(intent);
    }
}
