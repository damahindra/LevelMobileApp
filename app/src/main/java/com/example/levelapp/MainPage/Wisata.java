package com.example.levelapp.MainPage;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

public class Wisata {
    private int id;
    private String name;
    private String place;
    private int price;
    private boolean favorite;
    private Uri picture;

    public Wisata(int id, String name, String place, int price, boolean favorite) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.price = price;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public int getPrice() {
        return price;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public Uri getPicture() {
        return picture;
    }
}