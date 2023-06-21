package com.example.levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.levelapp.MainPage.MainPageActivity;
import com.example.levelapp.MainPage.Wisata;
import com.example.levelapp.MainPage.WisataAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Favorites extends AppCompatActivity {
    RecyclerView rv_favorite;
    WisataAdapter adapter;
    List<Wisata> wisataList;
    SwipeRefreshLayout refresh;

    FirebaseDatabase userDatabase;
    DatabaseReference databaseRef;

    FirebaseStorage storage;
    StorageReference storageRef;
    String postPath;

    ImageView backBtn;

    //    variables for storing wisata datas
    private int id, price, icon;
    private String name, place;
    private boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

//        components
        backBtn = findViewById(R.id.favorite_backBtn);
        refresh = findViewById(R.id.refresh);
        rv_favorite = findViewById(R.id.rv_favorite);

//      initialization
        userDatabase = FirebaseDatabase.getInstance();
        databaseRef = userDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        postPath = "posts";

//        fetch data
        fetchData(postPath);


//        on click listener
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMainPage();
                finish();
            }
        });

//        on refresh listener
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform the refresh operation here, such as reloading data
                fetchData(postPath);
                // After refreshing is complete, call setRefreshing(false) to stop the loading indicator
                refresh.setRefreshing(false);
            }
        });
    }

    private void fetchData(String postPath) {
        wisataList = new ArrayList<>();
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
                    if (favorite) wisataList.add(new Wisata(id, name, place, price, favorite, icon));
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
                                    if (fileIndex <= wisataList.size()) {
                                        Wisata currentPlace = wisataList.get(fileIndex - 1);
                                        currentPlace.setPicture(uri);
                                        adapter.notifyItemChanged(fileIndex - 1);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors that occurred while retrieving the download URL
                                    Toast.makeText(Favorites.this, "Failed to fetch image", Toast.LENGTH_SHORT).show();
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
                adapter = new WisataAdapter(Favorites.this, wisataList);
                rv_favorite.setLayoutManager(new LinearLayoutManager(Favorites.this));
                rv_favorite.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Favorites.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toMainPage() {
        Intent main = new Intent(Favorites.this, MainPageActivity.class);
        startActivity(main);
    }
}