package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.api.core.ApiFuture;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Detail extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        ApiFuture<QuerySnapshot> query = (ApiFuture<QuerySnapshot>) db.collection("Activities").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
        for (DocumentSnapshot document : documents) {
            if (document.getString("Activityname").equals(name)) {
                String detail = document.getString("Detail");
                TextView detailBox = (TextView) findViewById(R.id.Detail);
                detailBox.setText(detail);
            }
        }
    }
    public void back(View view){
        Intent intent=new Intent();
        intent.setClass(this, upcomingrow.class);
    }
}