package com.PersonalLancer.ryudigitalprinting.KonfirmasiPesana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.PersonalLancer.ryudigitalprinting.Adapter.AdapterKonfirmasiPembayaran;
import com.PersonalLancer.ryudigitalprinting.Api.JSONResponse;
import com.PersonalLancer.ryudigitalprinting.Api.RequestInterface;
import com.PersonalLancer.ryudigitalprinting.LoginRegister.LoginUser;
import com.PersonalLancer.ryudigitalprinting.Model.ModelStatusPesanan;
import com.PersonalLancer.ryudigitalprinting.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.PersonalLancer.ryudigitalprinting.BuildConfig.BASE_URL;

public class KonfirmasiPembayaran extends AppCompatActivity {

    RecyclerView listkonfirmasipesanan;
    private ArrayList<ModelStatusPesanan> mArrayList;
    AdapterKonfirmasiPembayaran adapterKonfirmasiPembayaran;
    SharedPreferences sharedpreferences;
    String id, id_transaksi;


    SwipeRefreshLayout swipeRefresh;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran);


        listkonfirmasipesanan = (RecyclerView) findViewById(R.id.listkonfirmasipesanan);

        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
       /// Toast.makeText(this, "Data id ke "+ id, Toast.LENGTH_SHORT).show();

/*
        id = getIntent().getStringExtra("id");
        Toast.makeText(this, "Data id ke "+ id, Toast.LENGTH_SHORT).show();
        id_transaksi = getIntent().getStringExtra("id_transaksi");
        Toast.makeText(this, "id Transaksi "+ id_transaksi, Toast.LENGTH_SHORT).show();
*/

        swipeRefresh = findViewById(R.id.swipeRefresh);
        progressBar = findViewById(R.id.progressBar);

        swipeRefresh.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                loadJSON();
                swipeRefresh.setRefreshing(false);
            }
        });


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
        progressBar.setVisibility(android.view.View.VISIBLE);
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getStatuspesanan(id);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                progressBar.setVisibility(android.view.View.INVISIBLE);
                swipeRefresh.setRefreshing(false);
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
