package com.PersonalLancer.ryudigitalprinting.Api;

public class User {

    private int id;
    private String namalengkap, email, nomortelepon, password;

    public User(int id, String namalengkap, String email, String nomortelepon) {
        this.id = id;
        this.namalengkap = namalengkap;
        this.email = email;
        this.nomortelepon = nomortelepon;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNamalengkap() {
        return namalengkap;
    }

    public String getEmail() {
        return email;
    }

    public String getNomortelepon() {
        return nomortelepon;
    }

    public String getPassword() {
        return password;
    }
}
