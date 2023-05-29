package com.example.levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SignupPage extends AppCompatActivity {
    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthListener;
    AuthMethodPickerLayout mAuthLayout = new AuthMethodPickerLayout.Builder(R.layout.activity_login_page)
                                                                   .setGoogleButtonId(R.id.google_btn)
                                                                   .build();
    public static final int RC_SIGN_IN = 1;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );
    EditText user_email, user_pw;
    Button signup, google_btn;
    TextView toSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        user_email = findViewById(R.id.user_email);
        user_pw = findViewById(R.id.user_password);
        signup = findViewById(R.id.signup_btn);
        toSignin = findViewById(R.id.toSignin);
        google_btn = findViewById(R.id.google_btn);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                checking if the current user is available
                FirebaseUser user = mAuth.getCurrentUser();

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(providers)
                                .setAuthMethodPickerLayout(mAuthLayout)
                                .build(),
                        RC_SIGN_IN);

            }
        };
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.addAuthStateListener(mAuthListener);
            }
        });

        toSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignin = new Intent(SignupPage.this, LoginPage.class);
                startActivity(toSignin);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emptyFieldError = "Field cannot be empty";
                if (user_email.getText().toString().isEmpty() && user_pw.getText().toString().isEmpty()) {
                    Log.w("Email and Password Empty", emptyFieldError);
                    user_email.setError(emptyFieldError);
                    user_pw.setError(emptyFieldError);
                    return;
                }
                else if (user_email.getText().toString().isEmpty()) {
                    Log.w("Email Empty", emptyFieldError);
                    user_email.setError(emptyFieldError);
                    return;
                }
                else if (user_pw.getText().toString().isEmpty()) {
                    Log.w("Password Empty", emptyFieldError);
                    user_pw.setError(emptyFieldError);
                    return;
                }
                String email = user_email.getText().toString();
                String password = user_pw.getText().toString();
                signUp(email, password);
            }
        });
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignupPage.this, "Account Created!", Toast.LENGTH_SHORT).show();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    toInsertData(currentUser);
                }
                else Toast.makeText(SignupPage.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        // here we are calling remove auth
        // listener method on stop.
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void toInsertData(FirebaseUser user) {
        if (user != null) {
            Intent i = new Intent(SignupPage.this, InsertData.class);
            startActivity(i);
        }
    }
}