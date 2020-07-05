package com.example.activitymonitor;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private boolean userIsInteracting;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    // Retrieve CollectionReference of Users collection in Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference UsersRef = db.collection("Users");
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        contactSpinner = (Spinner) findViewById(R.id.contacts_spinner);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Populate userList with users to display in spinner
        readData(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<User> list) {
                Log.d(TAG, list.toString());

                userList = list;

                // Set these users to be displayed in the spinner
                ArrayAdapter<User> adapter = new ArrayAdapter<>(SendMessageActivity.this,
                        android.R.layout.simple_list_item_1, userList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                contactSpinner.setAdapter(adapter);

                contactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // Assigns the selected user in the drop-down menu
                        User selectedUser = (User) parent.getItemAtPosition(position);

                        // Get the ID of this user and the selected user
                        String currentUserID = currentUser.getUid();
                        String selectedUserID = selectedUser.getUserID();

                        // Identify if the currentUserID is in the selected user's keys list
                        ArrayList<String> listIDs = selectedUser.getKeys();

                        // If no conversation exists between users, create one
                        if (!listIDs.contains(currentUserID)) {
                            // Create a new message collection between the two users
                            createMessageCollection(currentUserID, selectedUserID);

                        }

                        // Display conversation
                        
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }
        });

        /*contactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Assigns the selected user in the drop-down menu
                User selectedUser = (User) parent.getSelectedItem();

                // Get the ID of this user and the selected user
                String currentUserID = currentUser.getUid();
                String selectedUserID = selectedUser.getUserID();

                // Identify if the currentUserID is in the selected user's keys list
                List<String> listIDs = selectedUser.getKeys();

                if (!listIDs.contains(selectedUserID)) {
                    // Create a new message collection between the two users
                    createMessageCollection(currentUserID, selectedUserID);

                } else {

                }


                //Toast.makeText(SendMessageActivity.this,"User: " + user.getUserID(), Toast.LENGTH_SHORT).show();

                // Create a MessageCollection when the user makes an actual selection
                *//*if (((User) parent.getSelectedItem()).getUserID() != null) {
                    displayContactData(user);
                    createMessageCollection(user);

                    // Get MessageCollection relevant to these two users


                    // TODO: 2020-06-10 Need to be sure about data model structure. This is for testing:
                    *//**//*Message testMessage = new Message("sender info","receiver info","test content");
                    List<Message> testMsgList = new ArrayList<>();
                    testMsgList.add(testMessage);

                    mMessageRecycler = findViewById(R.id.recycler_view_messagelist);
                    mMessageAdapter = new MessageListAdapter(SendMessageActivity.this, testMsgList);
                    mMessageRecycler.setLayoutManager(new LinearLayoutManager(SendMessageActivity.this));*//**//*
                }*//*

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    private void createMessageCollection(final String sendID, final String receiveID) {

        // Create new MessageCollection object
        MessageCollection msgCo = new MessageCollection(sendID, receiveID);

        // Add the opposite party's UID to the keys list for both users
        CollectionReference usersRef = db.collection("Users");

        DocumentReference senderRef = usersRef.document(sendID);
        senderRef.update("keys", FieldValue.arrayUnion(receiveID));

        DocumentReference receiverRef = usersRef.document(receiveID);
        receiverRef.update("keys", FieldValue.arrayUnion(sendID));

        // Add MessageCollection to Firebase
        final String msgCoID = db.collection("Communications").document().getId();
        msgCo.setCollectionID(msgCoID);

        db.collection("Communications").document(msgCoID)
                .set(msgCo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: " + msgCoID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void readData(final FirestoreCallback callback) {

        // Add users from Firebase to arraylist
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
