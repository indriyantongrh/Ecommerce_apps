package com.example.ryudigitalprinting.CetakMMT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryudigitalprinting.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class CetakMMT extends AppCompatActivity implements View.OnClickListener {

    EditText txtlebar, txtpanjang, txtjumlahorder, txttanggalpesan;
    TextView txthargammt;
    CardView btncekharga, btnuploadgambar;
    ImageView  image;
    Bitmap bitmap, decoded;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100

    private int mYear, mMonth, mDay;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_mmt);

        txtlebar = findViewById(R.id.txtlebar);
        txtpanjang = findViewById(R.id.txtpanjang);
        txtjumlahorder = findViewById(R.id.txtjumlahorder);
        txthargammt = findViewById(R.id.txthargammt);
        txttanggalpesan = findViewById(R.id.txttanggalpesan);

        image = findViewById(R.id.image);
        btnuploadgambar = findViewById(R.id.btnuploadgambar);
        btnuploadgambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
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

                    double lebar = Double.parseDouble(txtlebar.getText().toString());
                    double panjang = Double.parseDouble(txtpanjang.getText().toString());
                    double jumlahorder = Double.parseDouble(txtjumlahorder.getText().toString());
                    double hitungan = panjang*lebar *25000;

                    double result =  hitungan * jumlahorder;
                    ///jumlahharga.setText("Rp. " +Double.toString(result));
                    txthargammt.setText(formatRupiah.format(result));


                }


                else {
                    Toast toast = Toast.makeText(CetakMMT.this, "Masukan Lebar dan panjang ukuran MMT", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });



        txttanggalpesan.setOnClickListener(this);
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
