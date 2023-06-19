package com.example.levelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckoutPage extends AppCompatActivity {
    TextView place, Location, Title, Description, Price, Ticket, Book, Min, Plus;
    ImageView photo;
    int jumlah = 1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);
        place = findViewById(R.id.placeDet);
        Location = findViewById(R.id.locationDet);
        Title = findViewById(R.id.titleDesc);
        Description = findViewById(R.id.desc);
        Price = findViewById(R.id.priceTag);
        Ticket = findViewById(R.id.ticketCount);
        Book = findViewById(R.id.bookButton);
        Min = findViewById(R.id.minBtn);
        Plus = findViewById(R.id.plusButton);
        photo = findViewById(R.id.imageCheckout);

        Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlah--;
                String ticket = Integer.toString(jumlah);
                Ticket.setText(ticket);
                }
        });
        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlah++;
                String ticket = Integer.toString(jumlah);
                Ticket.setText(ticket);
            }
        });

        Bundle bundle = getIntent().getExtras();
            String Place = bundle.getString("name");
            place.setText(Place);
            String location = bundle.getString("place");
            Location.setText(location);
            String price = bundle.getString("price");
            Price.setText(price + "/Package");
            int Photo = bundle.getInt("image");
            photo.setImageResource(Photo);
        }
    }