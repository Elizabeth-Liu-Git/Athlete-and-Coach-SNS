package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Main login page that allows user to pick either coach or athlete
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCoach = findViewById(R.id.buttonCoach);
        buttonCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCoach();
            }
        });

        //Athlete Button Event listener
        Button buttonAthlete = findViewById(R.id.buttonAthlete);
        buttonAthlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAthlete();
            }
        });
    }



    /**
     * Opens coach activity after clicking coach button
     */
    public void openCoach(){
        Intent intent = new Intent (this, CoachPage.class);
        startActivity(intent);
    }

    /**
     * Opens Athlete Page
     */
    public void openAthlete(){
        Intent intent = new Intent (this, AthletePage.class);
        startActivity(intent);
    }
}
