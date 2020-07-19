package com.example.activitymonitor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
 * SendMessageActivity: This page will allow athletes and coaches to create, view, and reply to comments
 * with respect to individual activities. The page will only display comments that are within a
 * coach-athlete relationship. Users can choose to create or reply to a comment within this page.
 */

public class SendMessageActivity extends AppCompatActivity {

    public static final String TAG = "SendMessageActivity";
    private Spinner contactSpinner;
    private Button send;
    private EditText messageText;
    boolean isSpinnerInitial = true;

    private ArrayList<User> userList;
    private String currentUserID, selectedUserID, messageCollectionID;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference UsersRef = db.collection("Users");
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        contactSpinner = (Spinner) findViewById(R.id.contacts_spinner);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = currentUser.getUid();

        /*
         * Populates ArrayList userList with User objects, then connects them to an ArrayAdapter
         * to be displayed in a Spinner
         */
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
                    } else {
                        messageCollectionID = keys.get(currentUserID);
                        readMessages();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Creates a MessageCollection object to store Message objects. Stores the object in Firebase
     * in the "Communications" collection. Stores the User ID and the MessageCollection ID in each
     * of the two User's "keys" field as a key:value pair.
     * Once the MessageCollection object has been created, calls the ReadMessages() method.
     * @param sendID ID of User the starts the conversation.
     * @param receiveID ID of the recipient.
     */
    private void createMessageCollection(final String sendID, final String receiveID) {

        // Create new MessageCollection object
        final MessageCollection msgCo = new MessageCollection(sendID, receiveID);

        // Add MessageCollection to Firebase
        final String msgCoID = db.collection("Communications").document().getId();
        msgCo.setCollectionID(msgCoID);
        messageCollectionID = msgCoID;

        Log.d(TAG, "Adding MessageCollection to Communications.");
        final DocumentReference msgCoRef = db.collection("Communications").document(msgCoID);

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

                                           // Display messages in message collection
                                           readMessages();
                                       }
                                   }
        );
        task.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                System.out.println("Cancelled");
            }
        });

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

    /**
     * Custom callback to populate userList ArrayList with User objects from Firebase.
     * @param callback FirestoreCallback to be overridden when implemented.
     */
    private void readData(final FirestoreCallback callback) {

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

    /**
     * Retrieves the MessageCollection object between two users using the messageCollectionID.
     * Passes the object into the loadMessages() method.
     */
    private void readMessages() {

        // Get the MessageCollection object
        DocumentReference conversationRef = db.collection("Communications").document(messageCollectionID);

        conversationRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            MessageCollection messages = documentSnapshot.toObject(MessageCollection.class);
                            loadMessages(messages);
                        }
                    }
                });
    }

    /**
     * Takes the messages from a MessageCollection object and sets them to a RecyclerView.
     * The adapter is an instance of the MessageListAdapter class. The messages are then displayed
     * in descending order (from the most-recent message in the list to the first).
     * When the "SEND" button is clicked, the MessageCollection object is passed into the
     * sendMessage() method.
     * @param messages MessageCollection object received from the readMessages() method.
     */
    private void loadMessages(final MessageCollection messages) {

        mMessageRecycler = findViewById(R.id.recycler_view_messagelist);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        mMessageAdapter = new MessageListAdapter(this, messages.getMsgList());
        mMessageRecycler.setAdapter(mMessageAdapter);

        // Accept input text for messages
        send = findViewById(R.id.button_chatbox_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(messages);
            }
        });
    }

    /**
     * Called when the "SEND" button is clicked. Retrieves the text from the "Enter message" box, and
     * then adds the text as a String to the MessageCollection using the addMessage() method. The input
     * box is then cleared. The MessageCollection is then updated in Firebase, and when the task is
     * complete the readMessages() method is called to display the sent message as the most recent message.
     * @param messages MessageCollection received from loadMessages() method.
     */
    private void sendMessage(final MessageCollection messages) {

        // Get the message content
        messageText = findViewById(R.id.edittext_chatbox);
        String messageContent = messageText.getText().toString();

        // Add the message to the msgList of the MessageCollection
        messages.addMessage(messageContent, currentUserID, selectedUserID);
        messageText.getText().clear();

        // Update the MessageCollection in Firestore, and refresh RecyclerView
        final DocumentReference msgCoRef = db.collection("Communications").document(messageCollectionID);
        Task<Void> task = db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                transaction.set(msgCoRef, messages);
                return null;
            }
        });

        // When task is complete, refresh RecyclerView
        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                readMessages();
            }
        });
    }

    /**
     * Interface implemented by readData method. Contains onCallback() method to be overridden
     * by the implementation.
     */
    interface FirestoreCallback {
        void onCallback(ArrayList<User> list);
    }
}
