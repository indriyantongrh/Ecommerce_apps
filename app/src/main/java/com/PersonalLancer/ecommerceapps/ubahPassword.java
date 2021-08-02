package com.PersonalLancer.ecommerceapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.PersonalLancer.ecommerceapps.Api.RequestInterface;
import com.PersonalLancer.ecommerceapps.Api.SuccessMessage;
import com.PersonalLancer.ecommerceapps.LoginRegister.LoginUser;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ubahPassword extends AppCompatActivity {
    String id;
    SharedPreferences sharedpreferences;
    ProgressDialog pDialog;
    EditText txtPasswordbaru ;
    CardView btnupdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);
        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");

        Toast.makeText(ubahPassword.this, "id anda" + id, Toast.LENGTH_SHORT).show();



        txtPasswordbaru = findViewById(R.id.txtPasswordbaru);
//        txtComfirmpassword = findViewById(R.id.txtComfirmpassword);
        btnupdate = findViewById(R.id.btnupdate);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });




    }

    public void updatePassword() {
        //membuat progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Proses tambah user ...");
        pDialog.show();

        //mengambil data dari edittext

        String password = txtPasswordbaru.getText().toString();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();

        RequestInterface api = retrofit.create(RequestInterface.class);
        Call<SuccessMessage> call = api.updatePassword(id, password);
        call.enqueue(new Callback<SuccessMessage>() {
            @Override
            public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                String success = response.body().getSuccess();
                String message = response.body().getMessage();
                pDialog.dismiss();
                if (success.equals("1")) {
                    Toast.makeText(ubahPassword.this, message, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(DetailBookingTempat.this, Riwayat_booking.class);
//                    intent.putExtra("user_id",id_user);
//                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ubahPassword.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessMessage> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(ubahPassword.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}