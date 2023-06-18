package com.example.levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.levelapp.MainPage.MainPageActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LandingPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signup_btn = findViewById(R.id.btn_signup);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignupPage = new Intent(LandingPage.this, SignupPage.class);
                startActivity(toSignupPage);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent toMainPage = new Intent(LandingPage.this, MainPageActivity.class);
            startActivity(toMainPage);
        }
    }
}