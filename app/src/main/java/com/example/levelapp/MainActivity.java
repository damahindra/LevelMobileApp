package com.example.levelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ImageButton profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Initialization
        profileBtn = findViewById(R.id.profileBtn);

//        on click listener
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toProfilePage();
            }
        });
    }

    private void toProfilePage() {
        Intent profile = new Intent(MainActivity.this, ProfilePage.class);
        startActivity(profile);
    }
}