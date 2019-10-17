package com.example.ryudigitalprinting.Model;

import com.google.gson.annotations.SerializedName;

public class ModelStatusPesanan {

    @SerializedName("id_transaksi") //ini yg ada di json
    private String id_transaksi;



    @SerializedName("id") //ini yg ada di json
    private String id;


    @SerializedName("jenis_cetak") //ini yg ada di json
    private String jenis_cetak;

    @SerializedName("nomor_invoice") //ini yg ada di json
    private String nomor_invoice;

    @SerializedName("tanggal_pesan") //ini yg ada di json
    private String tanggal_pesan;

    @SerializedName("lebar") //ini yg ada di json
    private String lebar;

    @SerializedName("panjang") //ini yg ada di json
    private String panjang;

    @SerializedName("jumlah_pesanan") //ini yg ada di json
    private String jumlah_pesanan;

    @SerializedName("gambar") //ini yg ada di json
    private String gambar;

    @SerializedName("jumlah_harga") //ini yg ada di json
    private String jumlah_harga;

    @SerializedName("status_pesanan") //ini yg ada di json
    private String status_pesanan;

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

    public String getJenis_cetak() {
        return jenis_cetak;
    }

    public void setJenis_cetak(String jenis_cetak) {
        this.jenis_cetak = jenis_cetak;
    }

    public String getNomor_invoice() {
        return nomor_invoice;
    }

    public void setNomor_invoice(String nomor_invoice) {
        this.nomor_invoice = nomor_invoice;
    }

    public String getTanggal_pesan() {
        return tanggal_pesan;
    }

    public void setTanggal_pesan(String tanggal_pesan) {
        this.tanggal_pesan = tanggal_pesan;
    }

    public String getLebar() {
        return lebar;
    }

    public void setLebar(String lebar) {
        this.lebar = lebar;
    }

    public String getPanjang() {
        return panjang;
    }

    public void setPanjang(String panjang) {
        this.panjang = panjang;
    }

    public String getJumlah_pesanan() {
        return jumlah_pesanan;
    }

    public void setJumlah_pesanan(String jumlah_pesanan) {
        this.jumlah_pesanan = jumlah_pesanan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getJumlah_harga() {
        return jumlah_harga;
    }

    public void setJumlah_harga(String jumlah_harga) {
        this.jumlah_harga = jumlah_harga;
    }

    public String getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(String status_pesanan) {
        this.status_pesanan = status_pesanan;
    }

    public String getStatus_transfer() {
        return status_transfer;
    }

    public void setStatus_transfer(String status_transfer) {
        this.status_transfer = status_transfer;
    }

    @SerializedName("status_transfer") //ini yg ada di json
    private String status_transfer;



}
