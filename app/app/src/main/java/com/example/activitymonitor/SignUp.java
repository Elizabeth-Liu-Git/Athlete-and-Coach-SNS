package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.activitymonitor.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Class that controls the Sign-Up of a user and when finished directs to ProfileSetUp
 */
public class SignUp extends AppCompatActivity {

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
    String USERID;

    /**
     *
     * @param savedInstanceState the instance of the android application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword);

        submit = findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick for submitButton that validates the user input
             * @param v View information
             */
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                if(email.equals("") || password.equals("")){
                    Toast.makeText(SignUp.this, "Please Fill Out The Form",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    createAccount(email, password);
                }
            }
        });

    }

    /**
     *
     * @param email the email taken from emailText
     * @param password the password taken from passwordText
     */
    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;

                            User u = new User();

                            USERID = firebaseUser.getUid();
                            u.setUserID(USERID);
                            u.setFirstName("Default");
                            u.setLastName("Default");
                            db.collection("Users").document(USERID).set(u)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            profileSetup();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUp.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed. Please Try Again.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    /**
     * Initiates switching the current Activity to the ProfileSetup activity
     * */
    private void profileSetup() {
        Intent intent = new Intent(this, ProfileSetUp.class);
        this.startActivity(intent);
    }
}