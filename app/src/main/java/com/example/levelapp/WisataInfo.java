package com.example.levelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class WisataInfo extends AppCompatActivity {

    ImageView imageWisata, backBtn;
    TextView namaWisata, lokasiWisata, wisataPrice, wisataDescription;
    TextView Price, Ticket, warning;
    Button checkout_btn;

    Uri info_image;

    String price;

    Button Plus, Min;
    int jumlah = 1 ;

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
        backBtn = findViewById(R.id.info_back_btn);

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
                toReceiptPage();
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
        checkout.putExtra("destination_name", namaWisata.getText().toString());
        checkout.putExtra("destination_location", lokasiWisata.getText().toString());
        checkout.putExtra("total_price", Price.getText().toString());
        startActivity(checkout);
    }
}