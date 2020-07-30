package com.example.activitymonitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Placeholder Activity that allows the user to sign out, will show user info later
 */
public class ProfilePage extends AppCompatActivity {
    private FirebaseFirestore db;
    private static final String TAG = "ProfilePage";
    Long userType = 0L;
    String userID = "";

    LinearLayout sendMessageButton;

    /**
     * When the activity is loaded add listeners and functions to buttons, if user submits we sign them out
     * @param savedInstanceState the instance of the user variable used for access
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        /*TextView newName = (TextView) findViewById(R.id.name);
        newName.setText("Default Name");*/

        Button confirmExerciseButton = findViewById(R.id.buttonSignOut);
        confirmExerciseButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });
        LinearLayout addFriend = findViewById(R.id.addACoach);
        db = FirebaseFirestore.getInstance();

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = db.collection("Users").document(SignIn.USERID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            userType = document.getLong("userType");
                            Long coach = 1L;
                            if (userType.equals(coach)) {
                                Intent accept_request_intent = new Intent(ProfilePage.this, AcceptRelationship.class);
                                startActivity(accept_request_intent);
                                Toast.makeText(ProfilePage.this, "You are accepting request as a coach",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                long approved = 1L;
                                long pending = 2L;
                                if(document.getLong("approval").equals(approved)){
                                    Toast.makeText(ProfilePage.this, "You can not choose another coach, you already have a coach",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else if(document.getLong("approval").equals(pending)){
                                    Toast.makeText(ProfilePage.this, "You can not choose another coach, you already send request for a coach",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Intent send_request_intent = new Intent(ProfilePage.this, CreateRelationship.class);
                                    startActivity(send_request_intent);
                                    Toast.makeText(ProfilePage.this, "You are sending request as an athlete",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


            }
        });

        /*
        If user clicks on "Send a message" button, redirect to CommentsPage
        (Will)
         */
        sendMessageButton = findViewById(R.id.send_message_button);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msgIntent = new Intent(ProfilePage.this, SendMessageActivity.class);

                // Get user profile to pass to CommentsPage
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                msgIntent.putExtra("USERID", currentUser.getUid());
                startActivity(msgIntent);
            }
        });
    }

    /**
     * Allows the user to sign out and directs to the signin activity
     */
    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SignIn.class);
        this.startActivity(intent);
        this.finishAffinity();
    }
}
