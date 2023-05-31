package com.example.levelapp.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    private String namaDepan, namaBelakang, noHP, tanggalLahir;

    public UserData(String namaDepan, String namaBelakang, String noHP, String tanggalLahir) {
        this.namaDepan = namaDepan;
        this.namaBelakang = namaBelakang;
        this.noHP = noHP;
        this.tanggalLahir = tanggalLahir;
    }

    public UserData() {
    }

    public String getNamaDepan() {
        return namaDepan;
    }

    public void setNamaDepan(String namaDepan) {
        this.namaDepan = namaDepan;
    }

    public String getNamaBelakang() {
        return namaBelakang;
    }

    public void setNamaBelakang(String namaBelakang) {
        this.namaBelakang = namaBelakang;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}
