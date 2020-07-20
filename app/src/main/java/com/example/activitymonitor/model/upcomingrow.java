package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class upcomingrow extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_row);
        db = FirebaseFirestore.getInstance();

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
