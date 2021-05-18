package com.kayip_dostlar.Pages;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kayip_dostlar.MainActivity;
import com.kayip_dostlar.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends Activity {
    private ImageView imageView;
    DatabaseReference reference;
    TextView baslık, acıklama, isim, yas, telefon, adres;
    ImageButton geridon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imgview_view);
        baslık = findViewById(R.id.txt_baslik_detay);
        acıklama = findViewById(R.id.txt_aciklama_detay);
        isim = findViewById(R.id.txt_isim_detay);
        yas = findViewById(R.id.txt_yas_detay);
        telefon = findViewById(R.id.txt_telefon_detay);
        adres = findViewById(R.id.txt_adres_detay);
        buttons();

        reference = FirebaseDatabase.getInstance().getReference().child("ilanlar");
        String key = getIntent().getStringExtra("key");
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    baslık.setText(snapshot.child("baslik").getValue().toString());
                    String image = snapshot.child("imageurl").getValue().toString();
                    Picasso.get().load(image).resize(249,187).into(imageView);
                    acıklama.setText(snapshot.child("detay").getValue().toString());
                    isim.setText(snapshot.child("isim").getValue().toString());
                    yas.setText(snapshot.child("yas").getValue().toString());
                    telefon.setText(snapshot.child("telefon").getValue().toString());
                    adres.setText(snapshot.child("adres").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void buttons() {
        geridon = (ImageButton) findViewById(R.id.imgbtn_back);
        geridon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}