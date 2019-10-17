package com.example.ryudigitalprinting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ryudigitalprinting.Api.JSONResponse;
import com.example.ryudigitalprinting.Model.ModelProfilUser;
import com.example.ryudigitalprinting.Api.RequestInterface;
import com.example.ryudigitalprinting.LoginRegister.LoginUser;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.ryudigitalprinting.BuildConfig.BASE_URL;

public class EditProfile extends AppCompatActivity {

    String id;
    SharedPreferences sharedpreferences;

    EditText txtnamalengkap, txtnomorhp, txtemail, txtpassword;

    private ArrayList<ModelProfilUser> mArrayListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        txtnamalengkap = findViewById(R.id.txtnamalengkap);
        txtnomorhp = findViewById(R.id.txtnomorhp);
        txtemail = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);



        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
        Toast.makeText(this, "ini id ke-"+ id, Toast.LENGTH_SHORT).show();

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
                String password = mArrayListUser.get(0).getPassword();

                txtnamalengkap.setText(namalengkap);
                txtemail.setText(email);
                txtnomorhp.setText(nomortelepon);
                txtpassword.setText(password);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }
}
