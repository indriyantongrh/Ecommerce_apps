package com.example.ryudigitalprinting.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ryudigitalprinting.Api.RequestInterface;
import com.example.ryudigitalprinting.Api.SuccessMessage;
import com.example.ryudigitalprinting.BuildConfig;
import com.example.ryudigitalprinting.R;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterUser extends AppCompatActivity {

    private String url = BuildConfig.BASE_URL;
    private String URL = "http://gudangandroid.universedeveloper.com/percetakan/";  //directory php ning server



    String id;
    CardView btndaftar;
    EditText txtnamalengkap, txtemail, txtnomortelepon, txtpassword;


    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        txtnamalengkap = findViewById(R.id.txtnamalengkap);
        txtemail = findViewById(R.id.txtemail);
        txtnomortelepon = findViewById(R.id.txtnomortelepon);
        txtpassword = findViewById(R.id.txtpassword);

        btndaftar = findViewById(R.id.btndaftar);
        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TambahUser();
            }
        });
    }

    public void TambahUser() {
        //membuat progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Proses tambah user ...");
        pDialog.show();

        //mengambil data dari edittext
        String namalengkap = txtnamalengkap.getText().toString();
        String email = txtemail.getText().toString();
        String nomortelepon = txtnomortelepon.getText().toString();
        String password = txtpassword.getText().toString();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();

        RequestInterface api = retrofit.create(RequestInterface.class);
        Call<SuccessMessage> call = api.registrasi(id, namalengkap, email, nomortelepon, password);
        call.enqueue(new Callback<SuccessMessage>() {
            @Override
            public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                String success = response.body().getSuccess();
                String message = response.body().getMessage();
                pDialog.dismiss();
                if (success.equals("1")) {
                    Toast.makeText(RegisterUser.this, message, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(DetailBookingTempat.this, Riwayat_booking.class);
//                    intent.putExtra("user_id",id_user);
//                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterUser.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessMessage> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(RegisterUser.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
