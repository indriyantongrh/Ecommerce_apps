package com.PersonalLancer.ecommerceapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.PersonalLancer.ecommerceapps.Api.JSONResponse;
import com.PersonalLancer.ecommerceapps.Api.SuccessMessage;
import com.PersonalLancer.ecommerceapps.Model.ModelProfilUser;
import com.PersonalLancer.ecommerceapps.Api.RequestInterface;
import com.PersonalLancer.ecommerceapps.LoginRegister.LoginUser;
import com.google.gson.Gson;

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

public class EditProfile extends AppCompatActivity {

    String id;
    SharedPreferences sharedpreferences;
    CardView btnupdate;
    EditText txtnamalengkap, txtnomorhp, txtemail, txtpassword;
    ProgressDialog pDialog;
    private ArrayList<ModelProfilUser> mArrayListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        txtnamalengkap = findViewById(R.id.txtnamalengkap);
        txtnomorhp = findViewById(R.id.txtnomorhp);
        txtemail = findViewById(R.id.txtemail);

        btnupdate = findViewById(R.id.btnupdate);



        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
//        Toast.makeText(this, "ini id ke-"+ id, Toast.LENGTH_SHORT).show();
        btnupdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateUsers();
            }
        });
        ambilProfilUser();

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


                txtnamalengkap.setText(namalengkap);
                txtemail.setText(email);
                txtnomorhp.setText(nomortelepon);


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    public void updateUsers() {
        //membuat progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Proses tambah user ...");
        pDialog.show();

        //mengambil data dari edittext
        String namalengkap = txtnamalengkap.getText().toString();
        String email = txtemail.getText().toString();
        String nomortelepon = txtnomorhp.getText().toString();


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();

        RequestInterface api = retrofit.create(RequestInterface.class);
        Call<SuccessMessage> call = api.updateUsers(id, namalengkap, email, nomortelepon);
        call.enqueue(new Callback<SuccessMessage>() {
            @Override
            public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                String success = response.body().getSuccess();
                String message = response.body().getMessage();
                pDialog.dismiss();
                if (success.equals("1")) {
                    Toast.makeText(EditProfile.this, message, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(DetailBookingTempat.this, Riwayat_booking.class);
//                    intent.putExtra("user_id",id_user);
//                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditProfile.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessMessage> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(EditProfile.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
