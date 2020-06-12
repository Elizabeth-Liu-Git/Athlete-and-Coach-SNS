package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Coaches page, this allows the coach to create a new exercise
 */

public class CoachPage extends AppCompatActivity implements OnClickListener {

    Button buttonCreateExercise;
    Button buttonAssignExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_page);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        buttonCreateExercise = findViewById(R.id.buttonNewExercise);
        buttonCreateExercise.setOnClickListener(this);
        buttonAssignExercise = findViewById(R.id.buttonAssignExercise);
        buttonAssignExercise.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNewExercise:
                Intent createIntent = new Intent(this, CreateExercise.class);
                startActivity(createIntent);
                break;
            case R.id.buttonAssignExercise:
                Intent assignIntent = new Intent(this, AssignExercise.class);
                startActivity(assignIntent);
                break;
        }
    }

        // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.miCompose:
                Intent intent = new Intent(this, ProfilePage.class);
                this.startActivity(intent);
                break;
            default:
                break;

        }
        return true;
    }
}
