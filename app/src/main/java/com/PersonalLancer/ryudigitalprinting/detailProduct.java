package com.PersonalLancer.ryudigitalprinting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class detailProduct extends AppCompatActivity {

    String id, berat, deskripsi, stock, harga, kondisi,namaProduct, merchant, gambar;
    TextView txt_name_product,txt_price_sale,txt_id, txt_weight_value,txt_condition,txt_stock,txt_name_merchant, txt_description;
    ImageView imageView;
    Button btnBelisekarang;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        txt_name_product = findViewById(R.id.txt_name_product);
        txt_price_sale = findViewById(R.id.txt_price_sale);
        txt_id = findViewById(R.id.txt_id);
        txt_condition = findViewById(R.id.txt_condition);
        txt_weight_value = findViewById(R.id.txt_weight_value);
        txt_stock = findViewById(R.id.txt_stock);
        txt_name_merchant = findViewById(R.id.txt_name_merchant);
        txt_description = findViewById(R.id.txt_description);
        btnBelisekarang = findViewById(R.id.btnBelisekarang);
        imageView = findViewById(R.id.imageView);

        id = getIntent().getStringExtra("id");
        deskripsi = getIntent().getStringExtra("deskripsi");
        berat   = getIntent().getStringExtra("berat");
        stock = getIntent().getStringExtra("stock");
        harga = getIntent().getStringExtra("harga");
        kondisi = getIntent().getStringExtra("kondisi");
        namaProduct = getIntent().getStringExtra("namaProduct");
        merchant = getIntent().getStringExtra("merchant");
        gambar = getIntent().getStringExtra("gambar");

        txt_name_product.setText(namaProduct);
        txt_price_sale.setText(harga);
        txt_condition.setText(kondisi);
        txt_description.setText(deskripsi);
        txt_weight_value.setText(berat+" Kg");
        txt_stock.setText(stock);
        txt_name_merchant.setText(merchant);

        Glide.with(detailProduct.this)
                .load(gambar)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(imageView);

        btnBelisekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(detailProduct.this, "Barang dimasukan keranjang belanja", Toast.LENGTH_LONG).show();
            }
        });

    }
}