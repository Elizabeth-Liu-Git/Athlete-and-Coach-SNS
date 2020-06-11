package com.example.activitymonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.activitymonitor.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    Button signUp, signIn, submit, buttonCoach, buttonAthlete;
    EditText emailText, passwordText;
    TextView passwordView, emailView;

    FirebaseFirestore db;
    String TAG = "EmailandPassword";
    private FirebaseAuth mAuth;
    String USERID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        signUp = findViewById(R.id.buttonSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpPage();
            }
        });

        signIn = findViewById(R.id.buttonSignIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInPage();
            }
        });

    }

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

    private void SignUpPage(){
        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword);
        emailView = findViewById(R.id.textViewEmail);
        passwordView = findViewById(R.id.textViewPassword);
        submit = findViewById(R.id.buttonSubmit);

        findViewById(R.id.Layout2).setVisibility(View.VISIBLE);
        findViewById(R.id.Layout1).setVisibility(View.INVISIBLE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                createAccount(email, password);
            }
        });
    }

    private void SignInPage(){
        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword);
        emailView = findViewById(R.id.textViewEmail);
        passwordView = findViewById(R.id.textViewPassword);
        submit = findViewById(R.id.buttonSubmit);

        findViewById(R.id.Layout2).setVisibility(View.VISIBLE);
        findViewById(R.id.Layout1).setVisibility(View.INVISIBLE);

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

    private void updateUI(FirebaseUser currentUser) {

        if(currentUser == null){
            findViewById(R.id.Layout2).setVisibility(View.INVISIBLE);
            findViewById(R.id.Layout1).setVisibility(View.VISIBLE);
        }
        else{
            DocumentReference docRef = db.collection("Users").document(currentUser.getUid());

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            long num = document.toObject(User.class).getUserType();

                            if(num == 0){
                                switchPageSelect();
                            }
                            else if(num == 2){
                                switchPageLandingAthlete();
                            }
                            else{
                                switchPageLandingCoach();
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

    private void switchPageSelect(){
        findViewById(R.id.Layout1).setVisibility(View.INVISIBLE);
        findViewById(R.id.Layout2).setVisibility(View.INVISIBLE);
        findViewById(R.id.Layout3).setVisibility(View.VISIBLE);

        final Context context = this;
        buttonAthlete = findViewById(R.id.buttonAthlete);
        buttonCoach = findViewById(R.id.buttonCoach);

        buttonAthlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AthletePage.class);
                startActivity(intent);
            }
        });
        buttonCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CoachPage.class);
                startActivity(intent);
            }
        });


    }

    private void switchPageLandingAthlete(){
        Intent intent = new Intent (this, AthletePage.class);
        startActivity(intent);
    }

    private void switchPageLandingCoach(){
        Intent intent = new Intent (this, CoachPage.class);
        startActivity(intent);
    }

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

                            db.collection("Users").document(USERID).set(u)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SignIn.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignIn.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            updateUI(firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

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

    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SignIn.class);
        this.startActivity(intent);
        this.finishAffinity();
    }
}