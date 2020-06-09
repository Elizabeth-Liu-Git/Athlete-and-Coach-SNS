package com.example.activitymonitor;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Will Robbins
 *
 * CreateComment: This class allows users to create comments, which will then be stored for later
 * access by CommentsView. This class can be called when users are creating comments in
 * CommentsView, and when users are submitting an activity.
 */

public class CreateComment extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);

        
    }

}
