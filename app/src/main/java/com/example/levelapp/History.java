package com.example.levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.levelapp.MainPage.Wisata;
import com.example.levelapp.Model.UserData;
import com.example.levelapp.Transaction.Transaction;
import com.example.levelapp.Transaction.TransactionAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class History extends AppCompatActivity {
    RecyclerView rv_history;
    List<Transaction> transactionList;
    Button clear;
    ImageView backBtn;
    SwipeRefreshLayout refresh;
    TransactionAdapter adapter;

//    fetch transaction data from Firebase
    FirebaseDatabase userDatabase;
    DatabaseReference databaseRef;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userPath;

    String fullName;

    String fetched_name, fetched_location_name, fetched_location, fetched_dateTime, fetched_email;
    int fetched_qty;
    long fetched_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        backBtn = findViewById(R.id.history_backBtn);
        rv_history = findViewById(R.id.rv_history);
        refresh = findViewById(R.id.refresh);

        clear = findViewById(R.id.btn_delete_history);

        userDatabase = FirebaseDatabase.getInstance();
        databaseRef = userDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) userPath = "users/" + currentUser.getEmail().replaceAll("@gmail.com", "");
        fetchData();

        //        on refresh listener
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform the refresh operation here, such as reloading data
                fetchData();
                // After refreshing is complete, call setRefreshing(false) to stop the loading indicator
                refresh.setRefreshing(false);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearHistory();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clearHistory() {
        databaseRef.child(userPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    fullName = userData.getNamaDepan() + " " + userData.getNamaBelakang();

//                    fetching transactions
                    databaseRef.child("transactions").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot nestedSnapshot : childSnapshot.getChildren()) {
                                    // Retrieve the key and value of each nested child
                                    String nestedKey = nestedSnapshot.getKey();
                                    Object nestedValue = nestedSnapshot.getValue();

                                    // Process the nested child data as needed
                                    switch(Objects.requireNonNull(nestedKey)) {
                                        case "username" :
                                            fetched_name = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "place_location" :
                                            fetched_location = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "place_name" :
                                            fetched_location_name = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "total_price" :
                                            fetched_price = (long) Objects.requireNonNull(nestedValue);
                                            break;
                                        case "dateTime" :
                                            fetched_dateTime = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "email" :
                                            fetched_email = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "qty" :
                                            fetched_qty = (int)(long) Objects.requireNonNull(nestedValue);
                                            break;
                                    }
                                }
                                if (fullName.equals(fetched_name)) childSnapshot.getRef().removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(History.this, "Failed to remove data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "Failed to remove data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void fetchData() {
        transactionList = new ArrayList<>();
        databaseRef.child(userPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    fullName = userData.getNamaDepan() + " " + userData.getNamaBelakang();

//                    fetching transactions
                    databaseRef.child("transactions").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot nestedSnapshot : childSnapshot.getChildren()) {
                                    // Retrieve the key and value of each nested child
                                    String nestedKey = nestedSnapshot.getKey();
                                    Object nestedValue = nestedSnapshot.getValue();

                                    // Process the nested child data as needed
                                    switch(Objects.requireNonNull(nestedKey)) {
                                        case "username" :
                                            fetched_name = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "place_location" :
                                            fetched_location = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "place_name" :
                                            fetched_location_name = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "total_price" :
                                            fetched_price = (long) Objects.requireNonNull(nestedValue);
                                            break;
                                        case "dateTime" :
                                            fetched_dateTime = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "email" :
                                            fetched_email = String.valueOf(Objects.requireNonNull(nestedValue));
                                            break;
                                        case "qty" :
                                            fetched_qty = (int)(long) Objects.requireNonNull(nestedValue);
                                            break;
                                    }
                                }
                                if (fullName.equals(fetched_name)) transactionList.add(new Transaction(fetched_name, fetched_location_name, fetched_location, fetched_email, fetched_qty, fetched_price));
                            }
                            adapter = new TransactionAdapter(transactionList, History.this);
                            rv_history.setLayoutManager(new LinearLayoutManager(History.this));
                            rv_history.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "Failed to remove data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}