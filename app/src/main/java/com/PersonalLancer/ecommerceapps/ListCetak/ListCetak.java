package com.PersonalLancer.ecommerceapps.ListCetak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.PersonalLancer.ecommerceapps.CetakMMT.CetakKartuNama;
import com.PersonalLancer.ecommerceapps.CetakMMT.CetakMMT;
import com.PersonalLancer.ecommerceapps.CetakMMT.CetakPamflet;
import com.PersonalLancer.ecommerceapps.CetakMMT.CetakUndanganPernikahan;
import com.PersonalLancer.ecommerceapps.R;

public class ListCetak extends AppCompatActivity {

    CardView btnkartunama, btncetakmmt,btnpamflet, btnundanganpernikahan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cetak);

        btnkartunama = findViewById(R.id.btnkartunama);
        btnkartunama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListCetak.this, CetakKartuNama.class);
                startActivity(intent);
            }
        });

        btncetakmmt = findViewById(R.id.btncetakmmt);
        btncetakmmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListCetak.this, CetakMMT.class);
                startActivity(intent);
            }
        });

        btnpamflet = findViewById(R.id.btnpamflet);
        btnpamflet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListCetak.this, CetakPamflet.class);
                startActivity(intent);
            }
        });

        btnundanganpernikahan = findViewById(R.id.btnundanganpernikahan);
        btnundanganpernikahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListCetak.this, CetakUndanganPernikahan.class);
                startActivity(intent);
            }
        });
    }
}
