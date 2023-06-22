package com.example.levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.levelapp.Model.UserData;
import com.example.levelapp.Transaction.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WisataInfo extends AppCompatActivity {

    ImageView imageWisata, backBtn;
    TextView namaWisata, lokasiWisata, wisataPrice, wisataDescription;
    TextView Price, Ticket, warning;
    Button checkout_btn;

    Uri info_image;

    String price;

    Button Plus, Min;
    int jumlah = 1 ;

//    for user data
    FirebaseDatabase userDatabase;
    DatabaseReference databaseRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String fullName, email, uniqueId;
    String formattedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata_info);

        //        initialization
        Plus = findViewById(R.id.plusButton);
        Min = findViewById(R.id.minBtn);
        Price = findViewById(R.id.priceTag);
        warning = findViewById(R.id.warning_text);
        Ticket = findViewById(R.id.ticketCount);
        imageWisata = findViewById(R.id.desc_image);
        namaWisata = findViewById(R.id.namaWisata);
        lokasiWisata = findViewById(R.id.lokasiWisata);
        wisataPrice = findViewById(R.id.price_info);
        wisataDescription = findViewById(R.id.tv_desc);
        wisataDescription.setMaxLines(3);
        wisataDescription.setMovementMethod(new ScrollingMovementMethod());

        checkout_btn = findViewById(R.id.book_btn);
        backBtn = findViewById(R.id.favorite_backBtn);

        Intent intent = getIntent();

        // Check if the intent has extras
        if (intent != null) {
            // Extract the extras from the intent
            String name = intent.getStringExtra("name");
            price = intent.getStringExtra("price");
            String place = intent.getStringExtra("place");
            String description = intent.getStringExtra("description");
            info_image = intent.getParcelableExtra("image");

            //        set the info page
            Glide.with(WisataInfo.this).load(info_image).into(imageWisata);
            namaWisata.setText(name);
            lokasiWisata.setText(place);
            wisataPrice.setText(price);
            wisataDescription.setText(description);
        }

        warning.setText("");
        Price.setText(price);

//        onClickListeners
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the current date and time
                LocalDateTime now = LocalDateTime.now();

                // Define the desired date and time format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Format the date and time using the formatter
                formattedDateTime = now.format(formatter);

//              User data
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                userDatabase = FirebaseDatabase.getInstance();
                databaseRef = userDatabase.getReference();
                if (currentUser != null) {
                    String username = "users/" + currentUser.getEmail().substring(0, currentUser.getEmail().lastIndexOf("@"));
                    databaseRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserData userData = snapshot.getValue(UserData.class);
                            if (userData != null) {
                                fullName = userData.getNamaDepan() + " " + userData.getNamaBelakang();
                                email = currentUser.getEmail();
                            }

                            postTransaction(databaseRef, formattedDateTime);
                            toReceiptPage();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlah > 1) {
                    if (jumlah == 5) warning.setText("");
                    jumlah--;
                    Ticket.setText(String.valueOf(jumlah));
                    Price.setText("Rp" + Integer.parseInt(price.replaceAll("Rp", ""))*jumlah);
                }
            }
        });
        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlah < 5) {
                    jumlah++;
                    Ticket.setText(String.valueOf(jumlah));
                    Price.setText("Rp" + Integer.parseInt(price.replaceAll("Rp", ""))*jumlah);
                }
                else {
                    warning.setText("Max 5 tickets only");
                }
            }
        });
    }

    private void toReceiptPage() {
        Intent checkout = new Intent(WisataInfo.this, Receipt.class);
        checkout.putExtra("uniqueId", uniqueId);
        checkout.putExtra("buyer_name", fullName);
        checkout.putExtra("buyer_email", email);
        checkout.putExtra("destination_name", namaWisata.getText().toString());
        checkout.putExtra("destination_location", lokasiWisata.getText().toString());
        checkout.putExtra("total_price", Price.getText().toString());
        checkout.putExtra("qty", String.valueOf(jumlah));
        checkout.putExtra("dateTime", formattedDateTime);
        startActivity(checkout);
    }

    private void postTransaction(DatabaseReference databaseRef, String dateTime) {
        Transaction transaction = new Transaction(fullName, namaWisata.getText().toString(), lokasiWisata.getText().toString(), email, jumlah, Long.parseLong(Price.getText().toString().replaceAll("Rp", "")));
        transaction.setDateTime(dateTime);
        DatabaseReference newRef = databaseRef.child("transactions").push();
        newRef.setValue(transaction);
        uniqueId = newRef.getKey();
    }
}