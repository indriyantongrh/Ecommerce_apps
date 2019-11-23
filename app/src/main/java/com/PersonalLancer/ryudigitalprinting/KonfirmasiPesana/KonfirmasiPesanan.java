package com.PersonalLancer.ryudigitalprinting.KonfirmasiPesana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.PersonalLancer.ryudigitalprinting.Api.JSONResponse;
import com.PersonalLancer.ryudigitalprinting.Api.RequestInterface;
import com.PersonalLancer.ryudigitalprinting.BuildConfig;
import com.PersonalLancer.ryudigitalprinting.Model.ModelProfilUser;
import com.PersonalLancer.ryudigitalprinting.R;
import com.PersonalLancer.ryudigitalprinting.Utility.AppController;
import com.PersonalLancer.ryudigitalprinting.Utility.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.PersonalLancer.ryudigitalprinting.BuildConfig.BASE_URL;
import static com.PersonalLancer.ryudigitalprinting.LoginRegister.LoginUser.my_shared_preferences;
import static com.PersonalLancer.ryudigitalprinting.LoginRegister.LoginUser.session_status;

public class KonfirmasiPesanan extends AppCompatActivity {

    private String url = BuildConfig.BASE_URL+"ceknomorinvoice.php";
    private String urlpost = BuildConfig.BASE_URL+"transaksi_sukses.php";
    private static final String url_cari_invoice = "http://gudangandroid.universedeveloper.com/percetakan/cari_invoice.php";
    ProgressDialog pDialog;
    Boolean session = false;
    String tag_json_obj = "json_obj_req";
    int success;
    private static final String TAG = KonfirmasiPesanan.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_ID = "id";
    public final static String TAG_ID_TRANSAKSI = "id_transaksi";
    public final static String TAG_NAMA_LENGKAP = "namalengkap";
    public final static String TAG_NOMORHP = "nomortelepon";
    public final static String TAG_TOKEN = "token";

    TextView tviduser, tvidtransaksi;
    EditText txtnamalengkap, txtnomorhp, txtnomorinvoice,txtnamabank,txtjumlahtf,txtcariinvoice;
    CardView btnkonfirmasi,btnuploadgambar, btncari;
    SharedPreferences sharedpreferences;
    ImageView image;
    Bitmap bitmap, decoded;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    String  bukti_transfer;
    private ArrayList<ModelProfilUser> mArrayListUser;
    String id_transaksi, id;

    JSONArray string_json = null;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pesanan);


        txtcariinvoice = findViewById(R.id.txtcariinvoice);
        txtcariinvoice.requestFocus();
        tviduser = findViewById(R.id.tviduser);
        tvidtransaksi = findViewById(R.id.tvidtransaksi);

        txtnamalengkap = findViewById(R.id.txtnamalengkap);
        txtnomorhp = findViewById(R.id.txtnomorhp);
        txtnomorinvoice = findViewById(R.id.txtnomorinvoice);
        txtnamabank = findViewById(R.id.txtnamabank);
        txtjumlahtf = findViewById(R.id.txtjumlahtf);
        btnkonfirmasi = findViewById(R.id.btnkonfirmasi);
        image = findViewById(R.id.image);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
        Toast.makeText(this, "ini id ke-"+ id, Toast.LENGTH_SHORT).show();

        btncari = findViewById(R.id.btncari);
        btncari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CariInvoice().execute();

                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);
                id = sharedpreferences.getString(TAG_ID, null);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(session_status, true);
                editor.putString(TAG_ID, id);
            }
        });


        btnkonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                checkinvoice();
                // TODO Auto-generated method stub
                String namalengkap = txtnamalengkap.getText().toString();
                String nomortelepon = txtnomorhp.getText().toString();
                String nomor_invoice = txtnomorinvoice.getText().toString();
                String nama_bank = txtnamabank.getText().toString();
                String jumlah_bayar = txtjumlahtf.getText().toString();




                /// if (conMgr.getActiveNetworkInfo() != null
                ///     && conMgr.getActiveNetworkInfo().isAvailable()
                ///   && conMgr.getActiveNetworkInfo().isConnected()) {
                Konfirmasi(namalengkap, nomortelepon, nomor_invoice, nama_bank, jumlah_bayar,bukti_transfer);
                /// } else {
                ///     Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                //   nn }
            }
        });

        btnuploadgambar = findViewById(R.id.btnuploadgambar);
        btnuploadgambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        ///ambilProfilUser();
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
                txtnomorhp.setText(nomortelepon);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }


    class CariInvoice extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KonfirmasiPesanan.this);
            pDialog.setMessage("Mencari ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        protected String doInBackground(String... params) {

            try {

                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add (new BasicNameValuePair("id_transaksi",id_transaksi));
                params1.add (new BasicNameValuePair("nomor_invoice", txtcariinvoice.getText().toString()));
                ///params1.add (new BasicNameValuePair("nomor_hp", txtemail.getText().toString()));
                //params1.add(new BasicNameValuePair("username",username));

                JSONObject json = jsonParser.makeHttpRequest(url_cari_invoice, "GET", params1);

                string_json = json.getJSONArray("getinvoice");


                runOnUiThread(new Runnable() {
                    public void run() {
                        final TextView id_transaksi = (TextView) findViewById(R.id.tvidtransaksi);
                        final TextView id = (TextView) findViewById(R.id.tviduser);
                        final EditText namalengkap =  findViewById(R.id.txtnamalengkap);
                        final EditText nomortelepon =  findViewById(R.id.txtnomorhp);
                        final EditText nomor_invoice =  findViewById(R.id.txtnomorinvoice);


                        try {
                            // ambil objek member pertama dari JSON Array
                            JSONObject ar = string_json.getJSONObject(0);
                            final String id_transaksi_d = ar.getString("id_transaksi");
                            final String id_d = ar.getString("id");
                            final String nama_d = ar.getString("nama_lengkap");
                            final String nomorhp_d = ar.getString("nomortelepon");
                            final String nomorinvoice_d = ar.getString("nomor_invoice");


                            id_transaksi.setText(id_transaksi_d);
                            id.setText(id_d);
                            namalengkap.setText(nama_d);
                            nomortelepon.setText(nomorhp_d);
                            nomor_invoice.setText(nomorinvoice_d);


                            txtcariinvoice.setText("");

                            if (nomor_invoice.equals("")){
                                //username_tidakada.setVisibility(View.VISIBLE);
                                ///Toast.makeText(LupaPassword.this, "Nama atau Nomor HP tidak ditemukan", Toast.LENGTH_SHORT).show();

                                Toast.makeText(KonfirmasiPesanan.this, "Nomor Invoice tidak ditemukan", Toast.LENGTH_SHORT).show();
                            }else {


                                ///layout_tanya_jawab.setVisibility(View.VISIBLE);
                                nomor_invoice.requestFocus();

                                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                                session = sharedpreferences.getBoolean(session_status, false);
                               /// id_transaksi = sharedpreferences.getString(TAG_ID_TRANSAKSI, null);
                                ///id = sharedpreferences.getString(TAG_ID, null);



                               //// tvlogin.setVisibility(View.VISIBLE);

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

        protected void onPostExecute(String result) {
            pDialog.dismiss();
        }
    }

    private void checkinvoice() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Cek invoice...");
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
                       //// value_nama_lengkap = jObj.getString(TAG_NAMA_LENGKAP);
                        // value_token = jObj.getString(TAG_TOKEN);
                        id = jObj.getString(TAG_ID);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
/*
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
                        startActivity(intent);*/
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
                params.put("id", "id");
                params.put("id_transaksi", "id_transaksi");
                /// params.put("username", txtusername.getText().toString());
                ////params.put("nomor_hp", txtusername.getText().toString());
                params.put("nomor_invoice", txtnomorinvoice.getText().toString());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    private void Konfirmasi(final String namalengkap, final String nomortelepon, final String nomor_invoice, final String tanggal_pesan, final String lebar, final String panjang) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Silahkan Tunggu ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, urlpost, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Registrasi berhasil!", jObj.toString());

                       /// goToPayment();
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


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

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", id);
                params.put("namalengkap", namalengkap);
                params.put("nomortelepon", nomortelepon);
                ///params.put("id_transaksi", id_transaksi);
                params.put("nomor_invoice", txtnomorinvoice.getText().toString());
                params.put("nama_bank", txtnamabank.getText().toString());
                params.put("jumlah_bayar", txtjumlahtf.getText().toString());
                params.put("bukti_transfer", getStringImage(decoded));
                params.put("status_transfer", "Menunggu Konfirmasi");

                return params;
            }

        };

        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
        ///AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    //untuk upload image, compress .JPEG ke bitmap
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //untuk memilih gambar
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*Menampilkan gambar*/
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        image.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
