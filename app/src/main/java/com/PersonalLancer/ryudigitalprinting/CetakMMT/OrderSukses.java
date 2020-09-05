package com.PersonalLancer.ryudigitalprinting.CetakMMT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.PersonalLancer.ryudigitalprinting.Api.JSONResponse;
import com.PersonalLancer.ryudigitalprinting.Model.ModelTransaksiUser;
import com.PersonalLancer.ryudigitalprinting.Api.RequestInterface;
import com.PersonalLancer.ryudigitalprinting.LoginRegister.LoginUser;
import com.PersonalLancer.ryudigitalprinting.MainActivity;
import com.PersonalLancer.ryudigitalprinting.R;
import com.PersonalLancer.ryudigitalprinting.Utility.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderSukses extends AppCompatActivity {

    private ClipboardManager myClipboard;
    private ClipData myClip;

    private static final String url_ambil_id_transaksi = "http://api.pasarburung.id/percetakan/cektransaksiterakhir.php";
    public static final String BASE_URL = "http://api.pasarburung.id//percetakan/";

    TextView txt_nomorinvoice, txt_totalharga;
    String id, id_transaksi;
    SharedPreferences sharedpreferences;
    String String_id_transaksi;
    JSONArray string_json = null;
    JSONParser jsonParser = new JSONParser();
    private int mYear, mMonth, mDay;
    ProgressDialog pDialog;

    CardView btnkembaliberanda;

    private ArrayList<ModelTransaksiUser> mArrayListTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sukses);

        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
     ///   Toast.makeText(this, "ini id ke-" + id, Toast.LENGTH_SHORT).show();

       /// id_transaksi = sharedpreferences.getString("id_transaksi", "0");
        ///Toast.makeText(this, "ini id tansaksi ke-" + id_transaksi, Toast.LENGTH_SHORT).show();


        txt_nomorinvoice = findViewById(R.id.txt_nomorinvoice);
        txt_totalharga = findViewById(R.id.txt_totalharga);

        btnkembaliberanda = findViewById(R.id.btnkembaliberanda);
        btnkembaliberanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });


        ////Copied in clipnoard
        txt_nomorinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = txt_nomorinvoice.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Nomor Invoice di Copy",Toast.LENGTH_SHORT).show();
            }
        });

        ambilTransaksiUser();
        new AmbilIdTransaksiTerakhir().execute();

    }

 public void ambilTransaksiUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getTransaksi(id);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();

                mArrayListTransaksi = new ArrayList<>(Arrays.asList(jsonResponse.getTransaksi()));
                String nomor_invoice = mArrayListTransaksi.get(0).getNomor_invoice();
                String jumlah_harga = mArrayListTransaksi.get(0).getJumlah_harga();


                txt_nomorinvoice.setText(nomor_invoice);
                txt_totalharga.setText(jumlah_harga);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    class AmbilIdTransaksiTerakhir extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(OrderSukses.this);
            pDialog.setMessage("Membuat pesanan ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                JSONObject json = jsonParser.makeHttpRequest(url_ambil_id_transaksi, "GET", params1);
                string_json = json.getJSONArray("nomor");

                runOnUiThread(new Runnable() {
                    public void run() {


                        try {
                            // ambil objek member pertama dari JSON Array
                            JSONObject ar = string_json.getJSONObject(0);
                            String nomor_id_d = ar.getString("id");
                            Integer tambah = Integer.parseInt(nomor_id_d) + 1;
                            String fix = getDateId() + "-" + String.valueOf(tambah);
                            String_id_transaksi = getDateId() + "-" + String.valueOf(tambah);

                            if (String_id_transaksi != null) {

                            } else {

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            //simpanTransaksi();
            pDialog.dismiss();
        }
    }


    //untuk transaksi terakhir
    private String getDateId() {
        Date current = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("ddMMyy");
        String dateString = frmt.format(current);
        return dateString;
    }





}
