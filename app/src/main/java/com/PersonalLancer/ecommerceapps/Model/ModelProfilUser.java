package com.PersonalLancer.ecommerceapps.Model;

import com.google.gson.annotations.SerializedName;

public class ModelProfilUser {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamalengkap() {
        return namalengkap;
    }

    public void setNamalengkap(String namalengkap) {
        this.namalengkap = namalengkap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomortelepon() {
        return nomortelepon;
    }

    public void setNomortelepon(String nomortelepon) {
        this.nomortelepon = nomortelepon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SerializedName("id") //ini yg ada di json
    private String id;

    @SerializedName("namalengkap") //ini yg ada di json
    private String namalengkap;

    @SerializedName("email") //ini yg ada di json
    private String email;

    @SerializedName("nomortelepon") //ini yg ada di json
    private String nomortelepon;

    @SerializedName("password") //ini yg ada di json
    private String password;




}
