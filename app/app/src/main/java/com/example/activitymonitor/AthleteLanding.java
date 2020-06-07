package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AthleteLanding extends AppCompatActivity {

    Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_page);

        signOut = findViewById(R.id.buttonSignOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
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
