package com.example.asus.instagramclonefirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    //Initialize
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //--buttons vs.

    Button b_sıgnIn;
    Button b_sıgnUp;
    EditText edit_email;
    EditText edit_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_sıgnIn = findViewById(R.id.b_signIn);
        b_sıgnUp = findViewById(R.id.b_signUp);
        edit_email = findViewById(R.id.edit_email);
        edit_pass = findViewById(R.id.edit_pass);





    mAuth = FirebaseAuth.getInstance();
    mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        }
    };


    }//onCreate


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener !=null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    //BUTTON LİSTENER
    public void signIn(View view) {

        mAuth.signInWithEmailAndPassword(edit_email.getText().toString(),edit_pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "SIGN IN SUCCEED.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),FeedActivity.class);
                            startActivity(i);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if(e != null){
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //BUTTON LİSTENER

    public void signUp(View view) {

        mAuth.createUserWithEmailAndPassword(edit_email.getText().toString(),edit_pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {  //COMPLETE THE SIGN UP
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "SIGN UP SUCCEED.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),FeedActivity.class);
                            startActivity(i);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {                        // fail the sign up
                if(e != null){
                    Toast.makeText(getApplicationContext(), "Authentication failed.\n"+e.getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}//class
