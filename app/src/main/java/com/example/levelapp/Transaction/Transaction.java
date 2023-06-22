package com.example.levelapp.Transaction;

public class Transaction {
    String username, place_name, place_location, email, dateTime;
    int qty;
    long total_price;

    public Transaction(String username, String place_name, String place_location, String email, int qty, long total_price) {
        this.username = username;
        this.place_name = place_name;
        this.place_location = place_location;
        this.email = email;
        this.qty = qty;
        this.total_price = total_price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_location() {
        return place_location;
    }

    public void setPlace_location(String place_location) {
        this.place_location = place_location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public long getTotal_price() {
        return total_price;
    }

    public void setTotal_price(long total_price) {
        this.total_price = total_price;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
