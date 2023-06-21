package com.example.levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.levelapp.Model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Receipt extends AppCompatActivity {
    TextView resi_no, resi_name, resi_email, destination_name, destination_place, destination_price, resi_qty;

    Button continueBtn;
    String id;

//    for fetching user related data
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userDataPath;

    FirebaseDatabase userDatabase;
    DatabaseReference databaseRef;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

//        initialization
        resi_no = findViewById(R.id.resi_no);
        resi_name = findViewById(R.id.resi_nama);
        resi_email = findViewById(R.id.resi_email);
        destination_name = findViewById(R.id.resi_destinasi);
        destination_place = findViewById(R.id.resi_lokasi);
        destination_price = findViewById(R.id.resi_biaya);
        resi_qty = findViewById(R.id.resi_qty);
        continueBtn = findViewById(R.id.btn_download);

//        Generating id for receipt

        Intent intent = getIntent();

        // Check if the intent has extras
        if (intent != null) {
            // Extract the extras from the intent
            String uniqueId = intent.getStringExtra("uniqueId");
            String buyer_name = intent.getStringExtra("buyer_name");
            String buyer_email = intent.getStringExtra("buyer_email");
            String intent_name = intent.getStringExtra("destination_name");
            String intent_price = intent.getStringExtra("total_price");
            String intent_place = intent.getStringExtra("destination_location");
            String intent_qty = intent.getStringExtra("qty");

            //        set the info page
            resi_no.setText(uniqueId);
            resi_name.setText(buyer_name);
            resi_email.setText(buyer_email);
            destination_name.setText(intent_name);
            destination_place.setText(intent_place);
            destination_price.setText(intent_price);
            resi_qty.setText(intent_qty);

        }

//        on click listener
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}