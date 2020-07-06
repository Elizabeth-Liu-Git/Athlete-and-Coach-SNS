package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class upcomingrow extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_row);
    }
    public void detail(View view){
        Intent intent=new Intent();
        TextView namebox = (TextView) findViewById(R.id.exercise_name);
        String name = namebox.getText().toString();
        intent.putExtra("name", name);
        intent.setClass(this, Detail.class);
        startActivityForResult(intent, 0x002);
    }
}
