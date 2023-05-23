package com.example.levelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupPage extends AppCompatActivity {
    EditText user_email, user_pw;
    Button signup;
    TextView toSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        user_email = findViewById(R.id.user_email);
        user_pw = findViewById(R.id.user_password);
        signup = findViewById(R.id.signup_btn);
        toSignin = findViewById(R.id.toSignin);
    }
}