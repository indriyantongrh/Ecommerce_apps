package com.PersonalLancer.ryudigitalprinting.Api;


import com.PersonalLancer.ryudigitalprinting.Model.ModelProfilUser;
import com.PersonalLancer.ryudigitalprinting.Model.ModelStatusPesanan;
import com.PersonalLancer.ryudigitalprinting.Model.ModelTransaksiUser;

public class JSONResponse {

    private ModelProfilUser[] datauser;

    public ModelProfilUser[] getDatauser() { return datauser; }

/*    private ModelDetailTransaksi[] datatransaksi;

    public ModelDetailTransaksi[] getTransaksi() { return datatransaksi; }*/

    private ModelTransaksiUser[] datatransaksi;

    public ModelTransaksiUser[] getTransaksi() { return datatransaksi; }


    private ModelStatusPesanan[] listpesanan;

    public ModelStatusPesanan[] getStatusPesanan() {
        return listpesanan;
    }

/*    private ModelProfilUser[] user;

    public ModelProfilUser[] getUser() {
        return user;
    }

    private ModelIklan[] iklan;

    public ModelIklan[] getIklan() {
        return iklan;
    }

    private ModelBeranda[] beranda;

    public ModelBeranda[] getBeranda() {
        return beranda;
    }

    private ModelArtikel[] artikel;

    public ModelArtikel[] getArtikel() {
        return artikel;
    }

    private ModelLomba[] lomba;

    public ModelLomba[] getLomba() {
        return lomba;
    }

    private ModelKomentar[] komentar;

    public ModelKomentar[] getKomentar() {
        return komentar;
    }

    private ModelListBurung[] list_burung;

    public ModelListBurung[] getList_burung() {
        return list_burung;
    }*/

}
