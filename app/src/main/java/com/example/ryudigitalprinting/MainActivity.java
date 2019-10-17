package com.example.ryudigitalprinting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryudigitalprinting.Api.JSONResponse;
import com.example.ryudigitalprinting.Model.ModelProfilUser;
import com.example.ryudigitalprinting.Api.RequestInterface;
import com.example.ryudigitalprinting.ListCetak.ListCetak;
import com.example.ryudigitalprinting.LoginRegister.LoginUser;
import com.example.ryudigitalprinting.StatusOrder.StatusOrder;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.ryudigitalprinting.BuildConfig.BASE_URL;
import static com.example.ryudigitalprinting.LoginRegister.LoginUser.TAG_ID;
import static com.example.ryudigitalprinting.LoginRegister.LoginUser.TAG_NAMA_LENGKAP;
import static com.example.ryudigitalprinting.LoginRegister.LoginUser.TAG_TOKEN;

public class MainActivity extends AppCompatActivity {

    TextView txtnama;
    CardView btnkonfirmasipembararan, btncetak, btnkontakcs, btnstatusorder, btneditprofile, btnlogout;
    String id;
    SharedPreferences sharedpreferences;

    private ArrayList<ModelProfilUser> mArrayListUser;

    public final static String TAG_ID_USER = "id";
    public final static String TAG_ID_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtnama = findViewById(R.id.txtnama);

        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
        Toast.makeText(this, "ini id ke-" +id, Toast.LENGTH_SHORT).show();

        ambilProfilUser();

        btncetak = findViewById(R.id.btncetak);
        btncetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCetak.class);
                startActivity(intent);
            }
        });

        btneditprofile = findViewById(R.id.btneditprofile);
        btneditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditProfile.class);
                startActivity(intent);
            }
        });


        btnstatusorder = findViewById(R.id.btnstatusorder);
        btnstatusorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatusOrder.class);
                startActivity(intent);
            }
        });

        btnkontakcs = findViewById(R.id.btnkontakcs);
        btnkontakcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWhatsapp = new Intent("android.intent.action.MAIN");
                intentWhatsapp.setAction(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=" + "6285803000346" + "&text=Halo admin, Mau reservasi Outbound nih....";

                intentWhatsapp.setData(Uri.parse(url));
                intentWhatsapp.setPackage("com.whatsapp");
                startActivity(intentWhatsapp);
            }
        });

        btnlogout = findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginUser.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_NAMA_LENGKAP, null);
                editor.putString(TAG_NAMA_LENGKAP, null);
                editor.putString(TAG_TOKEN, null);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, LoginUser.class);
                startActivity(intent);
                finish();

            }
        });
    }


    public void ambilProfilUser(){
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
                Log.d("Error",t.getMessage());
            }
        });
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
