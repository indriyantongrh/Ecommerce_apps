package com.PersonalLancer.ecommerceapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.PersonalLancer.ecommerceapps.Adapter.adapterProduct;
import com.PersonalLancer.ecommerceapps.Api.JSONResponse;
import com.PersonalLancer.ecommerceapps.Model.ListproductItem;
import com.PersonalLancer.ecommerceapps.Model.ModelProfilUser;
import com.PersonalLancer.ecommerceapps.Api.RequestInterface;
import com.PersonalLancer.ecommerceapps.LoginRegister.LoginUser;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.PersonalLancer.ecommerceapps.BuildConfig.BASE_URL;
import static com.PersonalLancer.ecommerceapps.LoginRegister.LoginUser.TAG_ID;
import static com.PersonalLancer.ecommerceapps.LoginRegister.LoginUser.TAG_NAMA_LENGKAP;
import static com.PersonalLancer.ecommerceapps.LoginRegister.LoginUser.TAG_TOKEN;

public class MainActivity extends AppCompatActivity {

    TextView txtnama;
    CardView btnkonfirmasi, btncetak, btnkontakcs, btnstatusorder, btneditprofile, btnlogout;
    String id;
    SharedPreferences sharedpreferences;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView listProduct;
    com.PersonalLancer.ecommerceapps.Adapter.adapterProduct adapterProduct;

    private ArrayList<ListproductItem> mArrayList;
    private ArrayList<ModelProfilUser> mArrayListUser;

    public final static String TAG_ID_USER = "id";
    public final static String TAG_ID_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listProduct = (RecyclerView) findViewById(R.id.listProduct);
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

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
//        txtnama = findViewById(R.id.txtnama);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
        ///Toast.makeText(this, "ini id ke-" +id, Toast.LENGTH_SHORT).show();
        initViews();
        loadJSON();
//        ambilProfilUser();

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.profile:
                        Intent intent = new Intent(MainActivity.this, EditProfile.class);
                        startActivity(intent);
                        break;
                    //Toast.makeText(MainActivity.this, "My profile", Toast.LENGTH_SHORT).show();
                   // break;
                    case R.id.ubah_password:
                        Intent ubahPassword = new Intent(MainActivity.this, ubahPassword.class);
                        startActivity(ubahPassword);
//                        Toast.makeText(MainActivity.this, "Ubah password", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(LoginUser.session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_NAMA_LENGKAP, null);
                        editor.putString(TAG_NAMA_LENGKAP, null);
                        editor.putString(TAG_TOKEN, null);
                        editor.apply();

                        Intent intentLogout = new Intent(MainActivity.this, LoginUser.class);
                        startActivity(intentLogout);
                        finish();
                        Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });

//        btnlogout = findViewById(R.id.btnlogout);
//        btnlogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putBoolean(LoginUser.session_status, false);
//                editor.putString(TAG_ID, null);
//                editor.putString(TAG_NAMA_LENGKAP, null);
//                editor.putString(TAG_NAMA_LENGKAP, null);
//                editor.putString(TAG_TOKEN, null);
//                editor.apply();
//
//                Intent intent = new Intent(MainActivity.this, LoginUser.class);
//                startActivity(intent);
//                finish();
//
//            }
//        });
    }
    private void initViews(){

        ///list_lowonganpelamar.setAdapter(adapterRecyclerViewLowonganPelamar);
        GridLayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
        listProduct.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listProduct.setLayoutManager(mLayoutManager);


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
        ///progressBar.setVisibility(android.view.View.VISIBLE);
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getProduct();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
               // progressBar.setVisibility(android.view.View.INVISIBLE);
                swipeRefresh.setRefreshing(false);
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getListproduct()));
                //mAdapter = new AdapterPencarianMenu(mArrayList);

                adapterProduct = new adapterProduct(getApplicationContext(),mArrayList);
                listProduct.setAdapter(adapterProduct);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    public void ambilProfilUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getProfilUser(id);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();

                mArrayListUser = new ArrayList<>(Arrays.asList(jsonResponse.getDatauser()));
                String namalengkap = mArrayListUser.get(0).getNamalengkap();
                String email = mArrayListUser.get(0).getEmail();
                String nomortelepon = mArrayListUser.get(0).getNomortelepon();

                txtnama.setText(namalengkap);
                /// txt_telepon.setText(nomor_hp);
                //txt_email.setText(email);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        /// builder.setTitle("Keluar ");
        builder.setMessage("Apakah kamu yakin ingin keluar dari aplikasi ?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                moveTaskToBack(true);
                finish();
                //// new DetailAplikasiSaya.HapusData().execute();
                dialog.dismiss();
            }

        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        //// Toast.makeText(this,"Keluar aplikasi!", Toast.LENGTH_LONG).show();

    }


}
