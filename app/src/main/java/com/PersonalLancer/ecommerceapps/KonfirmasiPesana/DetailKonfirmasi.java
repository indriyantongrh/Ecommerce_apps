package com.PersonalLancer.ecommerceapps.KonfirmasiPesana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.PersonalLancer.ecommerceapps.BuildConfig;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.abhinay.input.CurrencyEditText;


public class DetailKonfirmasi extends AppCompatActivity {

    TextView  tvnomorinvoice,txt_namalengkap, txt_jeniscetak,txt_lebar, txt_panjang,txt_jumlahcetak, txt_totalbayar;
    String id, id_transaksi,namalengkap,nomor_invoice,jenis_cetak,lebar,panjang,jumlah_pesanan,jumlah_harga, jumlah_bayar;

    CardView btncekharga, btnuploadgambar, btnkonfirmasi;
    ImageView image;
    Bitmap bitmap, decoded;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    SharedPreferences sharedpreferences;
    EditText txtnamabank,txtjumlahtf;

    JSONArray string_json = null;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    int success;    private static final String url_konfirmasi_pembayaran = BuildConfig.BASE_URL+"konfirmasipembayaran.php";

    private static final String TAG = DetailKonfirmasi.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    CurrencyEditText etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_konfirmasi);

        tvnomorinvoice = findViewById(R.id.tvnomorinvoice);
        txt_namalengkap = findViewById(R.id.txt_namalengkap);
        txt_jeniscetak = findViewById(R.id.txt_jeniscetak);
        txt_lebar = findViewById(R.id.txt_lebar);
        txt_panjang = findViewById(R.id.txt_panjang);
        txt_jumlahcetak = findViewById(R.id.txt_jumlahcetak);
        txt_totalbayar = findViewById(R.id.txt_totalbayar);
        image= findViewById(R.id.image);

        txtnamabank = findViewById(R.id.txtnamabank);
        txtjumlahtf = findViewById(R.id.txtjumlahtf);

      //  Locale localeID = new Locale("in", "ID");
       // NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);



 //       id = getIntent().getStringExtra("id");
   ///     Toast.makeText(this, "id user "+ id, Toast.LENGTH_SHORT).show();

        id_transaksi = getIntent().getStringExtra("id_transaksi");
        Toast.makeText(this, "id Transaksi "+ id_transaksi, Toast.LENGTH_SHORT).show();


        ////////////////////////////////////// data lowongan
        id_transaksi = getIntent().getStringExtra("id_transaksi");
        namalengkap = getIntent().getStringExtra("namalengkap");
        nomor_invoice = getIntent().getStringExtra("nomor_invoice");
        jenis_cetak = getIntent().getStringExtra("jenis_cetak");
        lebar = getIntent().getStringExtra("lebar");
        panjang = getIntent().getStringExtra("panjang");
        jumlah_pesanan = getIntent().getStringExtra("jumlah_pesanan");
        jumlah_harga = getIntent().getStringExtra("jumlah_harga");

        ////Toast.makeText(this, ""+gaji, Toast.LENGTH_SHORT).show();
        //////////////////////////////////

        tvnomorinvoice.setText(nomor_invoice);
        txt_namalengkap.setText(namalengkap);
        txt_jeniscetak.setText(jenis_cetak);
        txt_lebar.setText(lebar);
        txt_panjang.setText(panjang);
        txt_jumlahcetak.setText(jumlah_pesanan);
        txt_totalbayar.setText(jumlah_harga);


        btnuploadgambar = findViewById(R.id.btnuploadgambar);
        btnuploadgambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnkonfirmasi = findViewById(R.id.btnkonfirmasi);
        btnkonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KonfirmasiPembayaran();
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


    private void KonfirmasiPembayaran() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Silahkan Tunggu ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, url_konfirmasi_pembayaran, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Berhasil", jObj.toString());

                        goToLogin();
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        txtnamabank.setText("");
                        txtjumlahtf.setText("");



                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Upload Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                  Locale localeID = new Locale("in", "ID");
                  NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                 /// txtjumlahtf.setText(formatRupiah.format(jumlahbayar));

                params.put("id_transaksi", id_transaksi);
                params.put("status_pesanan", "Menunggu Konfirmasi");
                params.put("nama_bank", txtnamabank.getText().toString());
                double jumlahbayar = Double.parseDouble(txtjumlahtf.getText().toString());
                params.put("jumlah_bayar", formatRupiah.format(jumlahbayar));
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

    private void goToLogin(){

        Intent intent = new Intent(DetailKonfirmasi.this, KonfirmasiPembayaran.class);
        finish();
        startActivity(intent);
    }

}
