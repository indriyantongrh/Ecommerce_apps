package com.PersonalLancer.ryudigitalprinting.Api;



import com.PersonalLancer.ryudigitalprinting.Model.ModelProduct;

import okhttp3.MultipartBody;
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
    Call<SuccessMessage> loginuser(@Field("id") String id,
                                   @Field("email") String email,
                                   @Field("nomortelepon") String nomortelepon,
                                    @Field("password") String password);


    //detail profil
    @GET("detailuser.php")
    Call<JSONResponse> getProfilUser(@Query("id") String id);

    @FormUrlEncoded
    @POST("updateprofile.php")
    Call<SuccessMessage> updateUsers(@Field("id") String id,
                                    @Field("namalengkap") String namalengkap,
                                    @Field("email") String email,
                                    @Field("nomortelepon") String nomortelepon);

    @FormUrlEncoded
    @POST("updatepassword.php")
    Call<SuccessMessage> updatePassword(@Field("id") String id,
                                     @Field("password") String password);


    //detail transaksi
    @GET("detailtransaksi.php")
    Call<JSONResponse> getTransaksi(@Query("id") String id);

    @GET("listproduct.php")
    Call<JSONResponse> getProduct();

    @GET("liststatuspesanan.php")
    Call<JSONResponse> getStatuspesanan(@Query("id") String id);


    @FormUrlEncoded
    @POST("ceknomorinvoice.php")
    Call<SuccessMessage> getkonfirmasipembayaran(@Field("id_transfer") String id_transfer,
                                                 @Field("id") String id,
                                                 @Field("namalengkap") String namalengkap,
                                                 @Field("nomortelepon") String nomortelepon,
                                                 @Field("id_transaksi") String id_transaksi,
                                                 @Field("nomor_invoice") String nomor_invoice,
                                                 @Field("nama_bank") String nama_bank,
                                                 @Field("jumlah_bayar") String jumlah_bayar);




}
