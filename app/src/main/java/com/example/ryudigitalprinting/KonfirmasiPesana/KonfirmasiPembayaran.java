package com.example.ryudigitalprinting.KonfirmasiPesana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ryudigitalprinting.Adapter.AdapterKonfirmasiPembayaran;
import com.example.ryudigitalprinting.Adapter.AdapterStatusPesanan;
import com.example.ryudigitalprinting.Api.JSONResponse;
import com.example.ryudigitalprinting.Api.RequestInterface;
import com.example.ryudigitalprinting.LoginRegister.LoginUser;
import com.example.ryudigitalprinting.Model.ModelStatusPesanan;
import com.example.ryudigitalprinting.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.ryudigitalprinting.BuildConfig.BASE_URL;

public class KonfirmasiPembayaran extends AppCompatActivity {

    RecyclerView listkonfirmasipesanan;
    private ArrayList<ModelStatusPesanan> mArrayList;
    AdapterKonfirmasiPembayaran adapterKonfirmasiPembayaran;
    SharedPreferences sharedpreferences;
    String id, id_transaksi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran);


        listkonfirmasipesanan = (RecyclerView) findViewById(R.id.listkonfirmasipesanan);

        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
        Toast.makeText(this, "Data id ke "+ id, Toast.LENGTH_SHORT).show();

/*
        id = getIntent().getStringExtra("id");
        Toast.makeText(this, "Data id ke "+ id, Toast.LENGTH_SHORT).show();
        id_transaksi = getIntent().getStringExtra("id_transaksi");
        Toast.makeText(this, "id Transaksi "+ id_transaksi, Toast.LENGTH_SHORT).show();
*/

        initViews();
        loadJSON();
    }

    private void initViews(){

        ///list_lowonganpelamar.setAdapter(adapterRecyclerViewLowonganPelamar);
        listkonfirmasipesanan.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listkonfirmasipesanan.setLayoutManager(layoutManager);


    }
    private void loadJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient().newBuilder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getStatuspesanan(id);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getStatusPesanan()));
                //mAdapter = new AdapterPencarianMenu(mArrayList);
                adapterKonfirmasiPembayaran = new AdapterKonfirmasiPembayaran(getApplicationContext(),mArrayList);
                listkonfirmasipesanan.setAdapter(adapterKonfirmasiPembayaran);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

}
