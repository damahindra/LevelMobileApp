package com.example.levelapp.MainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.levelapp.Favorites;
import com.example.levelapp.Model.UserData;
import com.example.levelapp.ProfilePage;
import com.example.levelapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WisataAdapter adapter;
    private List<Wisata> wisataList;
    FirebaseDatabase userDatabase;
    DatabaseReference databaseRef;
    UserData userData;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ImageView profilePicture;
    String newProfilePicture, userDataPath;
    TextView username;
    String postPath;

//    Button to move to favorites activity
    FloatingActionButton favorites;

//    variables for storing wisata datas
    private int id, price, icon;
    private String name, place;
    private boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wisataList = new ArrayList<>();

//      initialization
        userDatabase = FirebaseDatabase.getInstance();
        databaseRef = userDatabase.getReference();
        profilePicture = findViewById(R.id.profileBtn);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        username = findViewById(R.id.username);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        favorites = findViewById(R.id.favorites);
        postPath = "posts";

//      user profile picture path
        if (currentUser != null) {
            newProfilePicture = "images/" + currentUser.getEmail() + "/newProfilePicture.png";
            userDataPath = "users/" + currentUser.getEmail().replaceAll("@gmail.com", "");
        }

        databaseRef.child(userDataPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    String namaDepan = userData.getNamaDepan() + "!";

//                    setting the textview to user's front name
                    username.setText(namaDepan);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainPageActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

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

//        fetching all data for posts
        databaseRef.child(postPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot nestedSnapshot : childSnapshot.getChildren()) {
                        // Retrieve the key and value of each nested child
                        String nestedKey = nestedSnapshot.getKey();
                        Object nestedValue = nestedSnapshot.getValue();

                        // Process the nested child data as needed
                        switch(Objects.requireNonNull(nestedKey)) {
                            case "id" :
                                id = (int)(long) Objects.requireNonNull(nestedValue);
                                break;
                            case "name" :
                                name = String.valueOf(Objects.requireNonNull(nestedValue));
                                break;
                            case "place" :
                                place = String.valueOf(Objects.requireNonNull(nestedValue));
                                break;
                            case "price" :
                                price = (int)(long) Objects.requireNonNull(nestedValue);
                                break;
                            case "favorite" :
                                favorite = (boolean) Objects.requireNonNull(nestedValue);
                                icon = favorite? R.drawable.favorite_icon_clicked : R.drawable.favorite_icon;
                                break;
                        }
                    }
                    wisataList.add(new Wisata(id, name, place, price, favorite, icon));
                }
                //        fetching all images for posts
                storageRef.child(postPath).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> results = listResult.getItems();
                        for (StorageReference result : results) {
                            // Get the download URL of the item
                            result.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
//                            add picture into model
                                    String filename = uri.getLastPathSegment();
                                    int fileIndex = Integer.parseInt(filename.substring(6, filename.lastIndexOf(".")));
                                    for (Wisata wisata : wisataList) {
                                        if (fileIndex == wisata.getId()) {
                                            wisata.setPicture(uri);
                                            adapter.notifyItemChanged(wisataList.indexOf(wisata));
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors that occurred while retrieving the download URL
                                    Toast.makeText(MainPageActivity.this, "Failed to fetch image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred while retrieving the download URL
//                        code goes here....

                    }
                });
                adapter = new WisataAdapter(MainPageActivity.this, wisataList);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainPageActivity.this));
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainPageActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProfilePage();
            }
        });
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFavorites();
            }
        });

    }

    private void toProfilePage() {
        Intent intent = new Intent(MainPageActivity.this, ProfilePage.class);
        startActivity(intent);
    }

    private void toFavorites() {
        Intent intent = new Intent(MainPageActivity.this, Favorites.class);
        startActivity(intent);
        finish();
    }
}