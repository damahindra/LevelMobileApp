package com.example.levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.levelapp.Model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InsertData extends AppCompatActivity {

    EditText etNamaDepan, etNamaBelakang, etNoHP, etTanggalLahir;
    Button sendBtn;
    FirebaseDatabase userDatabase;
    DatabaseReference userDatabaseReference;

    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

//        initialization
        etNamaDepan = findViewById(R.id.etNamaDepan);
        etNamaBelakang = findViewById(R.id.etNamaBelakang);
        etNoHP = findViewById(R.id.etNoHP);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        sendBtn = findViewById(R.id.send_btn);

//        Firebase realtime database
        userDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = userDatabase.getReference();

        userData = new UserData();

//        onClickListeners
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaDepan = etNamaDepan.getText().toString();
                String namaBelakang = etNamaBelakang.getText().toString();
                String noHP = etNoHP.getText().toString();
                String tanggalLahir = etTanggalLahir.getText().toString();
                addDataToFirebase(namaDepan, namaBelakang, noHP, tanggalLahir);
            }
        });
    }

    private void addDataToFirebase(String namaDepan, String namaBelakang, String noHP, String tanggaLahir) {
        userData.setNamaDepan(namaDepan);
        userData.setNamaBelakang(namaBelakang);
        userData.setNoHP(noHP);
        userData.setTanggalLahir(tanggaLahir);

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDatabaseReference.setValue(userData);
                Toast.makeText(InsertData.this, "Data Added to Database", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InsertData.this, "Failed to add data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toMainActivity() {
        Intent main = new Intent(InsertData.this, MainActivity.class);
        startActivity(main);
    }
}