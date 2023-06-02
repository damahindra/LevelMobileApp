package com.example.levelapp.MainPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.levelapp.MainActivity;
import com.example.levelapp.ProfilePage;
import com.example.levelapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WisataAdapter adapter;
    private List<Wisata> wisataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wisataList = new ArrayList<>();
        ImageButton myImageButton = findViewById(R.id.profileHeader);

        // Tambahkan data tempat wisata
        wisataList.add(new Wisata("Taman Nasional Bromo Tengger Semeru", "Jawa Timur", R.drawable.wisata_bromo));
        wisataList.add(new Wisata("Pantai Kuta", "Bali", R.drawable.wisata_kuta));
        wisataList.add(new Wisata("Candi Borobudur", "Jawa Tengah", R.drawable.wisata_borobudur));
        wisataList.add(new Wisata("Taman Mini Indonesia Indah", "Jakarta", R.drawable.wisata_taman_mini));
        // Tambahkan data tempat wisata lainnya sesuai kebutuhan

        adapter = new WisataAdapter(this, wisataList);
        recyclerView.setAdapter(adapter);

        myImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, ProfilePage.class);
                startActivity(intent);
            }
        });

    }
}