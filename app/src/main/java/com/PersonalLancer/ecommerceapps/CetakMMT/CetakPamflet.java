package com.PersonalLancer.ecommerceapps.CetakMMT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.PersonalLancer.ecommerceapps.Api.JSONResponse;
import com.PersonalLancer.ecommerceapps.BuildConfig;
import com.PersonalLancer.ecommerceapps.Model.ModelProfilUser;
import com.PersonalLancer.ecommerceapps.Api.RequestInterface;
import com.PersonalLancer.ecommerceapps.LoginRegister.LoginUser;
import com.PersonalLancer.ecommerceapps.R;
import com.PersonalLancer.ecommerceapps.Utility.AppController;
import com.PersonalLancer.ecommerceapps.Utility.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.PersonalLancer.ecommerceapps.BuildConfig.BASE_URL;

public class CetakPamflet extends AppCompatActivity implements View.OnClickListener {

    private String url = BuildConfig.BASE_URL+"transaksi.php";
    private static final String TAG = CetakMMT.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";
    JSONArray string_json = null;
    JSONParser jsonParser = new JSONParser();
    int success;

    String id;
    SharedPreferences sharedpreferences;

    TextView txthargapamflet,txthargakertas;
    EditText txtlebar, txtpanjang, txtjumlahorder, txttanggalpesan, txtnamalengkap, txtnomorhp;
    Spinner spinnerukurankertas;
    Double kertasa4, kertasa3;
    CardView btncekharga, btnuploadgambar, btnpesanpamflet;
    ImageView image;
    Bitmap bitmap, decoded;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    String id_transaksi, gambar;
    private ArrayList<ModelProfilUser> mArrayListUser;
    private int mYear, mMonth, mDay;
    ProgressDialog pDialog;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_pamflet);

        txtnamalengkap = findViewById(R.id.txtnamalengkap);
        txtnomorhp = findViewById(R.id.txtnomorhp);
        txtlebar = findViewById(R.id.txtlebar);
        txtpanjang = findViewById(R.id.txtpanjang);
        txtjumlahorder = findViewById(R.id.txtjumlahorder);
        txthargapamflet = findViewById(R.id.txthargapamflet);
        txthargakertas = findViewById(R.id.txthargakertas);
        txttanggalpesan = findViewById(R.id.txttanggalpesan);
        image = findViewById(R.id.image);

        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
       /// Toast.makeText(this, "ini id ke-"+ id, Toast.LENGTH_SHORT).show();

        ambilProfilUser();

        btnuploadgambar = findViewById(R.id.btnuploadgambar);
        btnuploadgambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        spinnerukurankertas = findViewById(R.id.spinnerukurankertas);

        /// spinner tagihan perbulan
        String [] values =
                {"-Ukuran Kertas-","Kertas A4","Kertas A3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerukurankertas.setAdapter(adapter);
        ///method spinner tagihan perbulan
        spinnerukurankertas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                if (spinnerukurankertas.getSelectedItem().equals("Kertas A4")) {
                    txtlebar.setText("21cm");
                    txtpanjang.setText("29cm");
                    txthargakertas.setText("1100");




                }else if (spinnerukurankertas.getSelectedItem().equals("Kertas A3")){
                    txtlebar.setText("29.7cm");
                    txtpanjang.setText("42cm");
                    txthargakertas.setText("1600");
                }else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btncekharga = findViewById(R.id.btncekharga);
        btncekharga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((txtlebar.getText().length()>0)  )

                {

                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);


                    double jumlahorder = Double.parseDouble(txtjumlahorder.getText().toString());
                    double harga = Double.parseDouble(txthargakertas.getText().toString());
                    double hitungan = jumlahorder*harga;

                    double result =  hitungan * jumlahorder;
                    ///jumlahharga.setText("Rp. " +Double.toString(result));
                    txthargapamflet.setText(formatRupiah.format(hitungan));


                }


                else {
                    Toast toast = Toast.makeText(CetakPamflet.this, "Masukan Lebar dan panjang ukuran MMT", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        btnpesanpamflet = findViewById(R.id.btnpesanpamflet);
        btnpesanpamflet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String namalengkap = txtnamalengkap.getText().toString();
                String nomortelepon = txtnomorhp.getText().toString();
                String tanggal_pesan = txttanggalpesan.getText().toString();
                String lebar = txtlebar.getText().toString();
                String panjang = txtpanjang.getText().toString();
                String jumlah_pesanan = txtjumlahorder.getText().toString();
                String jumlah_harga = txthargapamflet.getText().toString();



                /// if (conMgr.getActiveNetworkInfo() != null
                ///     && conMgr.getActiveNetworkInfo().isAvailable()
                ///   && conMgr.getActiveNetworkInfo().isConnected()) {
                PostDataCustomers(namalengkap, nomortelepon, tanggal_pesan, lebar, panjang,panjang, jumlah_pesanan, jumlah_harga,gambar);
                /// } else {
                ///     Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                //   nn }


            }
        });



        txttanggalpesan.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(LoginUser.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "0");
        Toast.makeText(this, "ini id ke-"+ id, Toast.LENGTH_SHORT).show();


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

    private void PostDataCustomers(final String namalengkap, final String nomortelepon, final String nomor_invoice, final String tanggal_pesan, final String lebar, final String panjang, final String jumlah_pesanan, final String gambar, final String jumlah_harga) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Silahkan Tunggu ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {

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

                        goToPayment();
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

                final int random = new Random().nextInt(1000) + 20; // [0, 60] + 20 => [20, 80]
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                params.put("id", id);
                params.put("namalengkap", namalengkap);
                params.put("nomortelepon", nomortelepon);
                params.put("jenis_cetak", "Pesanan Cetak Pamflet");
                params.put("nomor_invoice", "#INVCETAK"+currentDay+currentMonth+currentYear+random);
                params.put("tanggal_pesan", txttanggalpesan.getText().toString());
                params.put("lebar", txtlebar.getText().toString());
                params.put("panjang", txtpanjang.getText().toString());
                params.put("jumlah_pesanan", txtjumlahorder.getText().toString());
                params.put("gambar", getStringImage(decoded));
                params.put("jumlah_harga", txthargapamflet.getText().toString());
                params.put("status_pesanan", "Menunggu Pembayaran");
                params.put("status_transfer", "Belum Lunas");
                params.put("status_transaksi", "Belum Lunas");

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

    private void goToPayment(){

        intent = new Intent(CetakPamflet.this, OrderSukses.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txttanggalpesan:

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txttanggalpesan.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
        }
    }
}
