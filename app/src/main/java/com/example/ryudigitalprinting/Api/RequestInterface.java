package com.example.ryudigitalprinting.Api;



import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RequestInterface {

    /*API RYU*/
    //Insert User Baru
    @FormUrlEncoded
    @POST("registeruser.php")
    Call<SuccessMessage> registrasi(@Field("id") String id,
                                    @Field("namalengkap") String namalengkap,
                                    @Field("email") String email,
                                    @Field("nomortelepon") String nomortelepon,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("login.php")
    Call<SuccessMessage> loginuser(@Field("email") String email,
                                    @Field("password") String password);


    /*Batas API*/

    //detail profil
    @GET("detail_profil.php")
    Call<JSONResponse> getProfilUser(@Query("id_user") String id_user);

    //jml iklan dari profil
    @GET("jumlah_iklan_akun.php")
    Call<JSONResponse> getJmlIklan(@Query("id_user") String id_user);

    @GET("list_burung.php")
    Call<JSONResponse> getListBurung();

    //insert data booking
    @FormUrlEncoded
    @POST("input_iklan.php")
    Call<SuccessMessage> insert_iklan(@Field("id_user") String id_user,
                                      @Field("kategori") String kategori,
                                      @Field("judul") String judul,
                                      @Field("harga") String harga,
                                      @Field("deskripsi") String deskripsi,
                                      @Field("nama_burung") String nama_burung,
                                      @Field("foto_1") String foto_1,
                                      @Field("foto_2") String foto_2,
                                      @Field("foto_3") String foto_3,
                                      @Field("video") String video,
                                      @Field("latitude") String latitude,
                                      @Field("longitude") String longitude,
                                      @Field("kota") String kota,
                                      @Field("tanggal_posting") String tanggal_posting,
                                      @Field("jam_posting") String jam_posting,
                                      @Field("status") String status);

    @Multipart
    @POST("uploadtoserver.php")
    Call<SuccessMessage> uploadVideoToServer(@Part MultipartBody.Part video);


    //pencarian filter
    @GET("list_jual_beli_terbaru.php")
    Call<JSONResponse> getJualBeliTerbaru(@Query("kategori") String kategori);

    @GET("list_jual_beli_termurah.php")
    Call<JSONResponse> getJualBeliTermurah(@Query("kategori") String kategori);

    @GET("list_jual_beli_termahal.php")
    Call<JSONResponse> getJualBeliTermahal(@Query("kategori") String kategori);

    //pencarian dengan parameter id_user untuk akun
    @GET("list_jual_beli_akun.php")
    Call<JSONResponse> getJualBeliAkun(@Query("id_user") String id_user);

    //dengan limit
    @GET("list_artikel_limit.php")
    Call<JSONResponse> getArtikelLimit();

    //tanpa limit
    @GET("list_artikel.php")
    Call<JSONResponse> getArtikel();

    //dengan limit
    @GET("list_lomba_limit.php")
    Call<JSONResponse> getLombaLimit();

    //tanpa limit
    @GET("list_lomba.php")
    Call<JSONResponse> getLomba();


    @GET("list_beranda.php")
    Call<JSONResponse> getBeranda();


    //insert data booking
    @FormUrlEncoded
    @POST("input_komentar.php")
    Call<SuccessMessage> insert_komentar(@Field("id_iklan") String id_iklan,
                                         @Field("id_user") String id_user,
                                         @Field("isi_komentar") String isi_komentar);
    @GET("list_komentar.php")
    Call<JSONResponse> getKomentarIklan(@Query("id_iklan") String id_iklan);



    //insert data booking
    @FormUrlEncoded
    @POST("edit_profile_user.php")
    Call<SuccessMessage> edit_profile_user(@Field("id_user") String id_user,
                                           @Field("nama_lengkap") String nama_lengkap,
                                           @Field("email") String email,
                                           @Field("nomor_hp") String nomor_hp,
                                           @Field("alamat") String alamat);

    //edit foto user
    @FormUrlEncoded
    @POST("edit_foto.php")
    Call<SuccessMessage> edit_foto(@Field("id_user") String id_user,
                                   @Field("foto") String foto);

    //edit password user
    @FormUrlEncoded
    @POST("edit_password.php")
    Call<SuccessMessage> edit_password(@Field("id_user") String id_user,
                                       @Field("password") String password);

    //edit foto user
    @FormUrlEncoded
    @POST("edit_token.php")
    Call<SuccessMessage> edit_token(@Field("id_user") String id_user,
                                    @Field("token") String token);

    //edit status iklan
    @FormUrlEncoded
    @POST("edit_status_iklan.php")
    Call<SuccessMessage> edit_status_iklan(@Field("id_iklan") String id_iklan,
                                           @Field("status") String status);


    //insert klik pcb
    @FormUrlEncoded
    @POST("input_klik_pcb.php")
    Call<SuccessMessage> insert_klik_pcb(@Field("id_user") String id_user,
                                         @Field("id_iklan") String id_iklan);

}
