package com.example.activitymonitor;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitymonitor.model.Message;
import com.example.activitymonitor.model.MessageCollection;
import com.example.activitymonitor.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    private List<User> userList;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    // Retrieve CollectionReference of Users collection in Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference UsersRef = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);


        // TODO: 2020-06-10 Need to be sure about data model structure. This is for testing:
        Message testMessage = new Message("sender info","receiver info","test content");
        List<Message> testMsgList = new ArrayList<>();
        testMsgList.add(testMessage);

        mMessageRecycler = findViewById(R.id.recycler_view_messagelist);
        mMessageAdapter = new MessageListAdapter(this, testMsgList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.contactSpinner = (Spinner) findViewById(R.id.contacts_spinner);

        readData(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<User> list) {
                Log.d(TAG, list.toString());

                userList = list;

                ArrayAdapter<User> adapter = new ArrayAdapter<>(SendMessageActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, userList);
                adapter.notifyDataSetChanged();
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                contactSpinner.setAdapter(adapter);
            }
        });

        contactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                User user = (User) parent.getSelectedItem();

                //Toast.makeText(SendMessageActivity.this,"User: " + user.getUserID(), Toast.LENGTH_SHORT).show();

                // Create a MessageCollection when the user makes an actual selection
                if (((User) parent.getSelectedItem()).getUserID() != null) {
                    displayContactData(user);
                    createMessageCollection(user);
                }

                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createMessageCollection(User receiver) {

        // When a contact is selected, generate a new MessageCollection
        String sendID = FirebaseAuth.getInstance().getUid();
        String receiveID = receiver.getUserID();

        MessageCollection msgCo = new MessageCollection(sendID, receiveID);

        // Add MessageCollection to Firebase
        db.collection("Communications").add(msgCo);
    }

    public void readData(final FirestoreCallback callback) {

        final ArrayList<User> uList = new ArrayList<>();

        UsersRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                uList.add(documentSnapshot.toObject(User.class));
                            }

                            callback.onCallback(uList);
                        } else {
                            Log.d(TAG, "Error retrieving users: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                    }
                });

    }

    // Custom callback to get data
    interface FirestoreCallback {
        void onCallback(ArrayList<User> list);
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
