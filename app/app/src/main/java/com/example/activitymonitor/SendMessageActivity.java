package com.example.activitymonitor;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitymonitor.model.Message;
import com.example.activitymonitor.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Will Robbins
 *
 * CommentsPage: This page will allow athletes and coaches to create, view, and reply to comments
 * with respect to individual activities. The page will only display comments that are within a
 * coach-athlete relationship. Users can choose to create or reply to a comment within this page,
 * and will be redirected to the CreateComment page.
 */

public class SendMessageActivity extends AppCompatActivity {

    public static final String TAG = "SendMessageActivity";
    private Spinner contactSpinner;
    List<User> userList;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        // Populate spinner with contacts
        contactSpinner = findViewById(R.id.contacts_spinner);
        userList = new ArrayList<>();
        getContacts();

        ArrayAdapter<User> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        contactSpinner.setAdapter(adapter);

        contactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getSelectedItem();
                displayContactData(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // TODO: 2020-06-10 Need to be sure about data model structure. This is for testing:
        Message testMessage = new Message("sender info","receiver info","test content");
        List<Message> testMsgList = new ArrayList<>();
        testMsgList.add(testMessage);

        mMessageRecycler = findViewById(R.id.recycler_view_messagelist);
        mMessageAdapter = new MessageListAdapter(this, testMsgList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getContacts() {

        // Retrieve CollectionReference of Users collection in Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference UsersRef = db.collection("Users");

        // Add each documents in Users to userList
        UsersRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                userList.add(doc.toObject(User.class));

                                // TODO: 2020-06-10 logd needed
                            }
                        } else {
                            // TODO: 2020-06-10 logd needed
                        }
                    }
                });
    }

    public void getSelectedContact(View v) {

        User user = (User) contactSpinner.getSelectedItem();
        displayContactData(user);
    }

    private void displayContactData(User user) {

        String UID = user.getUserID();
        String userData = "UID: " + UID;

        Toast.makeText(this, userData, Toast.LENGTH_LONG).show();
    }
}
