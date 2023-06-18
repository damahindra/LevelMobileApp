package com.example.levelapp.MainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.levelapp.MainActivity;
import com.example.levelapp.ProfilePage;
import com.example.levelapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WisataAdapter adapter;
    private List<Wisata> wisataList;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ImageView profilePicture;
    String newProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wisataList = new ArrayList<>();

//      initialization
        profilePicture = findViewById(R.id.profileBtn);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

//      user profile picture path
        if (currentUser != null) newProfilePicture = "images/" + currentUser.getEmail() + "/newProfilePicture.png";

//      set the profile picture by fetching data from firebase storage
        storageRef.child(newProfilePicture).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with( MainPageActivity.this).load(uri).into(profilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Task<Uri> defaultProfilePicture = storageRef.child("images/default.png").getDownloadUrl();
                defaultProfilePicture.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with( MainPageActivity.this).load(uri).into(profilePicture);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainPageActivity.this, "Error while loading profile picture", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Tambahkan data tempat wisata
        wisataList.add(new Wisata("Gunung Semeru", "Jawa Timur", R.drawable.wisata_bromo));
        wisataList.add(new Wisata("Pantai Kuta", "Bali", R.drawable.wisata_kuta));
        wisataList.add(new Wisata("Candi Borobudur", "Jawa Tengah", R.drawable.wisata_borobudur));
        wisataList.add(new Wisata("TMII", "Jakarta", R.drawable.wisata_taman_mini));
        // Tambahkan data tempat wisata lainnya sesuai kebutuhan

        adapter = new WisataAdapter(this, wisataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainPageActivity.this));
        recyclerView.setAdapter(adapter);

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, ProfilePage.class);
                startActivity(intent);
            }
        });

    }

    private void toProfilePage() {
        Intent intent = new Intent(MainPageActivity.this, ProfilePage.class);
        startActivity(intent);
    }
}