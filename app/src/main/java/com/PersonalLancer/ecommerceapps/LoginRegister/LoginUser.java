package com.PersonalLancer.ecommerceapps.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.PersonalLancer.ecommerceapps.Api.RequestInterface;
import com.PersonalLancer.ecommerceapps.Api.SharedPrefManager;
import com.PersonalLancer.ecommerceapps.Api.SuccessMessage;
import com.PersonalLancer.ecommerceapps.BuildConfig;
import com.PersonalLancer.ecommerceapps.MainActivity;
import com.PersonalLancer.ecommerceapps.R;
import com.PersonalLancer.ecommerceapps.Utility.AppController;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginUser extends AppCompatActivity {


    int success;
    ConnectivityManager conMgr;


    private String url = BuildConfig.BASE_URL+"login.php";

    private static final String TAG = LoginUser.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    SharedPrefManager sharedPrefManager;
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


        SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(this);


        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(LoginUser.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

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
               ///// pDialog = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                ///TambahUser();
                checkLogin();
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




    public void TambahUser() {
        //membuat progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Login ...");
        pDialog.show();

        //mengambil data dari edittext
        ///String namalengkap = txtnamalengkap.getText().toString();
        String nomortelepon = txtemail.getText().toString();
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
        Call<SuccessMessage> call = api.loginuser( id,nomortelepon,email,  password);
        call.enqueue(new Callback<SuccessMessage>() {
            @Override
            public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                String success = response.body().getSuccess();
                String message = response.body().getMessage();
                pDialog.dismiss();
                if (success.equals("1")) {

                    // menyimpan login ke session
                    sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
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


    private void checkLogin() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                pDialog.hide();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        id = jObj.getString(TAG_ID);
                        value_nama_lengkap = jObj.getString(TAG_NAMA_LENGKAP);
                        // value_token = jObj.getString(TAG_TOKEN);
                        id = jObj.getString(TAG_ID);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_NAMA_LENGKAP, value_nama_lengkap);
                        editor.putString(TAG_NOMORHP, value_nomorhp);
                        editor.putString(TAG_TOKEN, value_token);




                        editor.apply();

                        // Memanggil main activity
                        Intent intent = new Intent(LoginUser.this, MainActivity.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_NAMA_LENGKAP, value_nama_lengkap);
                        intent.putExtra(TAG_NOMORHP, value_nomorhp);
                        intent.putExtra(TAG_TOKEN, value_token);

                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                pDialog.hide();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", txtemail.getText().toString());
                /// params.put("username", txtusername.getText().toString());
                ////params.put("nomor_hp", txtusername.getText().toString());
                params.put("password", txtpassword.getText().toString());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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
