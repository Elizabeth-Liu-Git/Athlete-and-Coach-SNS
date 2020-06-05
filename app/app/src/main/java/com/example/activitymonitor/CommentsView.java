package com.example.activitymonitor;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Will Robbins
 * CommentsView: This page will allow athletes and coaches to create, view, and reply to comments
 * with respect to individual activities. The page will only display comments that are within a
 * coach-athlete relationship. Users can choose to create or reply to a comment within this page,
 * and will be redirected to the CreateComment page.
 */

public class CommentsView extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_view_page);


    }
    
}
