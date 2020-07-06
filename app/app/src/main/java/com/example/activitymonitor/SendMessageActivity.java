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
import com.google.android.gms.tasks.OnCanceledListener;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
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
    private Button select;
    boolean isSpinnerInitial = true;

    private ArrayList<User> userList;
    private String currentUserID, selectedUserID, messageCollectionID;
    private MessageCollection messageList;
    private HashMap<String, String> senderKeys, receiverKeys;

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
        currentUserID = currentUser.getUid();

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
            }
        });

        // When a contact is selected retrieve the conversation between the two users
        contactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Prevent automatic selection of the first item in the spinner
                if (isSpinnerInitial) {
                    isSpinnerInitial = false;
                } else {
                    // Assigns the selected user in the drop-down menu
                    User selectedUser = userList.get(position);
                    selectedUserID = selectedUser.getUserID();

                    // Identify if the currentUserID is in the selected user's keys list
                    HashMap<String, String> keys = selectedUser.getKeys();

                    // If no conversation exists between users, create one
                    assert keys != null;
                    if (!keys.containsKey(currentUserID)) {

                        // Create a new message collection between the two users
                        createMessageCollection(currentUserID, selectedUserID);
                    }

                    /*// Retrieve messageCollectionID
                    readString(new FirestoreStringCallback() {
                        @Override
                        public void onCallback(String value) {
                            messageCollectionID = value;
                        }
                    });*/

                    // Retrieve the MessageCollection
                    readMessageCollection(new FirestoreMessageCollectionCallback() {
                        @Override
                        public void onCallback(MessageCollection messages) {
                            messageList = messages;
                        }
                    });

                    // Display conversation
                    mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_view_messagelist);

                    //FOR TESTING
                    messageList.getMsgList().add(new Message(currentUserID, selectedUserID, "TEST blahblahblah"));

                    mMessageAdapter = new MessageListAdapter(SendMessageActivity.this, messageList.getMsgList());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void createMessageCollection(final String sendID, final String receiveID) {

        // Create new MessageCollection object
        final MessageCollection msgCo = new MessageCollection(sendID, receiveID);

        // Add MessageCollection to Firebase
        final String msgCoID = db.collection("Communications").document().getId();
        msgCo.setCollectionID(msgCoID);
        messageCollectionID = msgCoID;

        Log.d(TAG, "Adding MessageCollection to Communications.");
        final DocumentReference msgCoRef = db.collection("Communications").document(msgCoID);

        msgCoRef.set(msgCo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        Task<Void> task = db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                transaction.set(msgCoRef, msgCo);
                System.out.println("Transaction entered");
                return null;
            }
        });

        task.addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     System.out.println("Task result - " + task.getResult());
                     System.out.println("Task success - " + task.isSuccessful());
                 }
             }
        );
        task.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                System.out.println("Cancelled");
            }
        });
//        .addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "MessageCollection added.");
//            }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Failure: " + e.toString());
//            }
//        });

        // Add the opposite party's UID to the keys list for both users
        CollectionReference usersRef = db.collection("Users");

        Log.d(TAG, "Adding receiveID to sender keys.");
        final DocumentReference senderRef = usersRef.document(sendID);
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                DocumentSnapshot snapshot = transaction.get(senderRef);
                HashMap<String, String> keys = snapshot.toObject(User.class).getKeys();
                keys.put(receiveID, msgCoID);
                transaction.update(senderRef, "keys", keys);
                return null;
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "sender keys updated.");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failure: " + e.toString());
            }
        });

        Log.d(TAG, "Adding sendID to receiver keys.");
        final DocumentReference receiverRef = usersRef.document(receiveID);
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                DocumentSnapshot snapshot = transaction.get(receiverRef);
                HashMap<String, String> keys = snapshot.toObject(User.class).getKeys();
                keys.put(sendID, msgCoID);
                transaction.update(receiverRef, "keys", keys);
                return null;
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "receiver keys updated.");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failure: " + e.toString());
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

    public void readMessageCollection(final FirestoreMessageCollectionCallback callback) {

        // Get the MessageCollection object
        CollectionReference communicationsRef = db.collection("Communications");
        DocumentReference conversationRef = communicationsRef.document(messageCollectionID);
        conversationRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            callback.onCallback(documentSnapshot.toObject(MessageCollection.class));
                        }
                    }
                });
    }

    public void readSendKeysCollection(final FirestoreKeysCallback callback) {

        DocumentReference userRef = db.collection("Users").document(currentUserID);
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callback.onCallback(documentSnapshot.toObject(User.class).getKeys());
                    }
                });
    }

    // Custom callback to get users
    interface FirestoreCallback {
        void onCallback(ArrayList<User> list);
    }

    // Custom callback to get keys Hashmap
    interface FirestoreKeysCallback {
        void onCallback(HashMap<String, String> keys);
    }

    // Custom callback to get the MessageCollection object
    interface FirestoreMessageCollectionCallback {
        void onCallback(MessageCollection messages);
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
