package com.example.levelapp.MainPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Wisata {
    private String nama;
    private String lokasi;
    private int foto;

    public Wisata(String nama, String lokasi, int foto) {
        this.nama = nama;
        this.lokasi = lokasi;
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public int getFoto() {
        return foto;
    }
}