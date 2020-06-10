package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePage extends AppCompatActivity {

    Button confirmExerciseButton;
    LinearLayout sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        confirmExerciseButton = findViewById(R.id.buttonSignOut);
        confirmExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
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
                Intent msgIntent = new Intent(ProfilePage.this, CommentsPage.class);

                // Get user profile to pass to CommentsPage
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                msgIntent.putExtra("USERID", currentUser.getUid());
                startActivity(msgIntent);
            }
        });
    }

    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SignIn.class);
        this.startActivity(intent);
        this.finishAffinity();
    }
}
