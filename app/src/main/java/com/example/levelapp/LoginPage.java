package com.example.levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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

public class LoginPage extends AppCompatActivity {

//    Components
    EditText user_email, user_password;
    Button login_btn;
    TextView signup;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

//        initialization
        user_email = findViewById(R.id.etEmail);
        user_password = findViewById(R.id.etPassword);
        signup = findViewById(R.id.tvSignUp);
        login_btn = findViewById(R.id.login_btn);

//        firebase login
        mAuth = FirebaseAuth.getInstance();

//        on click listener
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = user_email.getText().toString();
                String password = user_password.getText().toString();
                login(email, password);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSignUpPage();
            }
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginPage.this, "Login Success!", Toast.LENGTH_SHORT).show();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    toMainActivity(currentUser);
                }
                else Toast.makeText(LoginPage.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toMainActivity(FirebaseUser user) {
        if (user != null) {
            Intent main = new Intent(LoginPage.this, MainActivity.class);
            startActivity(main);
        }
    }

    private void toSignUpPage() {
        Intent signUp = new Intent(LoginPage.this, SignupPage.class);
        startActivity(signUp);
    }

}