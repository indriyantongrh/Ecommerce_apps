package com.example.ryudigitalprinting.Model;

import com.google.gson.annotations.SerializedName;

public class ModelDetailTransaksi {
    @SerializedName("id_transaksi") //ini yg ada di json
    private String id_transaksi;

    @SerializedName("id") //ini yg ada di json
    private String id;

    @SerializedName("namalengkap") //ini yg ada di json
    private String namalengkap;

    @SerializedName("nomor_invoice") //ini yg ada di json
    private String nomor_invoice;

    @SerializedName("jumlah_harga") //ini yg ada di json
    private String jumlah_harga;

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

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

    public String getNomor_invoice() {
        return nomor_invoice;
    }

    public void setNomor_invoice(String nomor_invoice) {
        this.nomor_invoice = nomor_invoice;
    }

    public String getJumlah_harga() {
        return jumlah_harga;
    }

    public void setJumlah_harga(String jumlah_harga) {
        this.jumlah_harga = jumlah_harga;
    }
}
