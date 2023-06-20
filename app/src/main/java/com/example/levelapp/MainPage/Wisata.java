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
    private String description = "Lorem ipsum dolor sit amet, " +
                                 "consectetur adipiscing elit, " +
                                 "sed do eiusmod tempor incididunt " +
                                 "ut labore et dolore magna aliqua. " +
                                 "Ut enim ad minim veniam, quis nostrud " +
                                 "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                                 "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore " +
                                 "eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, " +
                                 "sunt in culpa qui officia deserunt mollit anim id est laborum.";

    private int icon;

    public Wisata(int id, String name, String place, int price, boolean favorite, int icon) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.price = price;
        this.favorite = favorite;
        this.icon = icon;
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

    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}