package com.example.ryudigitalprinting.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryudigitalprinting.Api.RequestInterface;
import com.example.ryudigitalprinting.Api.SharedPrefManager;
import com.example.ryudigitalprinting.Api.SuccessMessage;
import com.example.ryudigitalprinting.BuildConfig;
import com.example.ryudigitalprinting.MainActivity;
import com.example.ryudigitalprinting.R;
import com.google.gson.Gson;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginUser extends AppCompatActivity {

    CardView btnlogin;
    TextView btndaftar;
    EditText txtemail, txtpassword;


    ProgressDialog pDialog;

    Context mContext;


    public final static String TAG_ID = "id";
    public final static String TAG_NAMA_LENGKAP = "namalengkap";
    public final static String TAG_NOMORHP = "nomortelepon";
    public final static String TAG_TOKEN = "token";

    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, value_nama_lengkap, value_token, value_nomorhp;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        txtemail = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        value_nama_lengkap = sharedpreferences.getString(TAG_NAMA_LENGKAP, null);
        value_token = sharedpreferences.getString(TAG_TOKEN, null);
        value_nomorhp = sharedpreferences.getString(TAG_NOMORHP, null);


        if (session) {
            Intent intent = new Intent(LoginUser.this, MainActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_NAMA_LENGKAP, value_nama_lengkap);
            intent.putExtra(TAG_NOMORHP, value_nomorhp);
            intent.putExtra(TAG_TOKEN, value_token);

            finish();
            startActivity(intent);
        }



        btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////pDialog = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                TambahUser();
            }
        });

        btndaftar = findViewById(R.id.btndaftar);
        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginUser.this, RegisterUser.class);
                startActivity(intent);
            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    public void TambahUser() {
        //membuat progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Login ...");
        pDialog.show();

        //mengambil data dari edittext
        ///String namalengkap = txtnamalengkap.getText().toString();
        String email = txtemail.getText().toString();
        ////String nomortelepon = txtnomortelepon.getText().toString();
        String password = txtpassword.getText().toString();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();

        RequestInterface api = retrofit.create(RequestInterface.class);
        Call<SuccessMessage> call = api.loginuser( email,  password);
        call.enqueue(new Callback<SuccessMessage>() {
            @Override
            public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                String success = response.body().getSuccess();
                String message = response.body().getMessage();
                pDialog.dismiss();
                if (success.equals("1")) {


                    // menyimpan login ke session
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(session_status, true);
                    editor.putString(TAG_ID, id);
                    editor.putString(TAG_NAMA_LENGKAP, value_nama_lengkap);
                    editor.putString(TAG_NOMORHP, value_nomorhp);
                    editor.putString(TAG_TOKEN, value_token);

                    editor.apply();
                    Toast.makeText(LoginUser.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginUser.this, MainActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginUser.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessMessage> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(LoginUser.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginUser.this);

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
