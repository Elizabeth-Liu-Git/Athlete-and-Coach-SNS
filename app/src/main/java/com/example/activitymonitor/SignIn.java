package com.example.activitymonitor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Signs the user into the application using a sign in form
 */
public class SignIn extends AppCompatActivity {


    /**
     * The SignUp button that submits when the user would like to sign up
     */
    Button signUp;
    /**
     * The submit button that initiates running {@Link #createAccount(String email, String password)}
     */
    Button submit;
    /**
     * where user enters the email
     */
    EditText emailText;

    /**
     * where user enters the password
     */
    EditText passwordText;

    /**
     * Database information in a variable
     */
    FirebaseFirestore db;
    /**
     * The TAG used in Error Reporting for this section
     */
    String TAG = "EmailandPassword";
    /**
     * holds the instance of the user information
     */
    private FirebaseAuth mAuth;

    /**
     * holds the user ID returned from getUID()
     */
    public static String USERID;


    /**
     * When the activity is loaded add listeners and functions to buttons, if user submits we check signIn()
     * @param savedInstanceState the instance of the user variable used for access
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //creating notification channel
        createNotificationChannel();

        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword);

        signUp = findViewById(R.id.buttonSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpPage();
            }
        });

        submit = findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                if(email.equals("") || password.equals("")){
                    Toast.makeText(SignIn.this, "Please Fill Out The Form",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    signIn(email, password);
                }
            }
        });

    }

    /**
     * on the start of the activity check if there is a user instance and updateUI(currentUser)
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            USERID = currentUser.getUid();
        }
        updateUI(currentUser);
    }

    /**
     *Send the user to the SignUpPage activity
     */
    private void SignUpPage(){
        Intent intent = new Intent(this, SignUp.class);
        this.startActivity(intent);
    }

    /**
     * updates the UI if the user can be found in the database, send to appropriate page
     * @param currentUser the FirebaseUser that is currently using the program
     */
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            long num = (long) document.get("userType");
                            if(num == 0){
                                profileSetup();
                            }
                            else if(num == 2){
                                athletePage();
                            }
                            else{
                                coachPage();
                            }
                        } else {
                            SignOut();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    /**
     *Send user to athlete page
     */
    private void athletePage() {
        Intent intent = new Intent(this, AthletePage.class);
        this.startActivity(intent);
    }

    /**
     *Send user to coach page
     */
    private void coachPage() {
        Intent intent = new Intent(this, CoachPage.class);
        this.startActivity(intent);
    }

    /**
     *send user to profile setup if they don't have a userType
     */
    private void profileSetup() {
        Intent intent = new Intent(this, ProfileSetUp.class);
        this.startActivity(intent);
    }

    /**
     * Verifies the user using signInWithEmailAndPassword() and connects to the DB
     * @param email the email that the user has entered
     * @param password the password that the user has entered
     */
    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            USERID = user.getUid();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }

    /**
     *Signs the user out and redirects them to the SignIn page
     */
    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SignIn.class);
        this.startActivity(intent);
        this.finishAffinity();
    }

    /**
     *Used to initialize notification channel on application start
     */
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            //channel properties for notification
            CharSequence name = "ActivityMonitorNotificationChannel";
            String description = "Channel for Reminders for Activity Monitor";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            //Creating and setting channel properties
            NotificationChannel channel = new NotificationChannel("notifyActivityMonitor", name, importance);

            channel.setDescription(description);

            //notification manager
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);





        }
    }
}