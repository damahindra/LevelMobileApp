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
    Button checkout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata_info);

        //        initialization
        imageWisata = findViewById(R.id.desc_image);
        namaWisata = findViewById(R.id.namaWisata);
        lokasiWisata = findViewById(R.id.lokasiWisata);
        wisataPrice = findViewById(R.id.price_info);
        wisataDescription = findViewById(R.id.tv_desc);
        wisataDescription.setMaxLines(3);
        wisataDescription.setMovementMethod(new ScrollingMovementMethod());

        checkout_btn = findViewById(R.id.checkoutBtn);
        backBtn = findViewById(R.id.info_back_btn);

        Intent intent = getIntent();

        // Check if the intent has extras
        if (intent != null) {
            // Extract the extras from the intent
            String name = intent.getStringExtra("name");
            String price = intent.getStringExtra("price");
            String place = intent.getStringExtra("place");
            String description = intent.getStringExtra("description");
            Uri image = intent.getParcelableExtra("image");

            //        set the info page
            Glide.with(WisataInfo.this).load(image).into(imageWisata);
            namaWisata.setText(name);
            lokasiWisata.setText(place);
            wisataPrice.setText(price);
            wisataDescription.setText(description);
        }

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
                toCheckoutPage();
            }
        });
    }

    private void toCheckoutPage() {
        Intent checkout = new Intent(WisataInfo.this, CheckoutPage.class);
        checkout.putExtra("name", namaWisata.getText().toString());
        checkout.putExtra("place", lokasiWisata.getText().toString());
        checkout.putExtra("price", Integer.parseInt(wisataPrice.getText().toString().replaceAll("Rp", "")));
        startActivity(checkout);
    }
}