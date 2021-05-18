package com.kayip_dostlar.Pages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kayip_dostlar.MainActivity;
import com.kayip_dostlar.R;

public class ProfileDuzenle extends Activity {
    Button kaydet;
    String kulad;
    EditText ad,soyad,mail,konum;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static FragmentManager fragmentManager;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    ImageButton imgbtn;
    DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("kullaniciKayitlari");
    private DatabaseReference ref,yazdirad,yazdirsoyad,yazdirmail,yazdirkonum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilduzenle);

        kaydet=findViewById(R.id.profılguncel);
        ad=findViewById(R.id.edt_ad);
        soyad=findViewById(R.id.edt_soyad);
        mail=findViewById(R.id.edt_email);
        konum=findViewById(R.id.edt_konumm);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        degistir();
        buttons();

        Intent intent = getIntent();
        String isim = intent.getStringExtra("ad");
        String soyadd = intent.getStringExtra("soyad");
        String maill = intent.getStringExtra("mail");
        String konumm = intent.getStringExtra("konum");
        ad.setText(isim);
        soyad.setText(soyadd);
        mail.setText(maill);
        konum.setText(konumm);

    }
    public void buttons(){
        imgbtn=(ImageButton)findViewById(R.id.imageButtongeri);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ProfileDuzenle.this, AyarlarActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void degistir() {
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oku.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        kulad =preferences.getString("deger","bos");

                        try {

                            if (snapshot.exists()) {
                                String data = snapshot.child(kulad).getKey();


                                if (data.equals(kulad)) {
                                    String adi = ad.getText().toString();
                                    String soyadi = soyad.getText().toString();
                                    String maili = mail.getText().toString();
                                    String konumu=konum.getText().toString();

                                    database = FirebaseDatabase.getInstance();
                                    ref = database.getReference().child("kullaniciKayitlari").child(kulad);
                                    yazdirad=ref.child("ad");
                                    yazdirad.setValue(adi);
                                    yazdirsoyad=ref.child("soyad");
                                    yazdirsoyad.setValue(soyadi);
                                    yazdirkonum=ref.child("sehir");
                                    yazdirkonum.setValue(konumu);
                                    yazdirmail=ref.child("email");
                                    yazdirmail.setValue(maili);
                                    Toast.makeText(ProfileDuzenle.this, "Güncellendi. :)", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ProfileDuzenle.this, AyarlarActivity.class);
                                    startActivity(i);


                                } else {
                                    Toast.makeText(ProfileDuzenle.this, "Hatalı İşlem Yapıldı", Toast.LENGTH_SHORT).show();

                                }
                            }


                        } catch (Exception e) {
                            Toast.makeText(ProfileDuzenle.this, "Hata Oluştu", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                oku.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        kulad =preferences.getString("deger","bos");
//
//                        try {
//
//                            if (snapshot.exists()) {
//                                String data = snapshot.child(kulad).getKey();
//
//
//                                if (data.equals(kulad)) {
//                                    String adi = ad.getText().toString();
//                                    String soyadi = soyad.getText().toString();
//                                    String maili = mail.getText().toString();
//                                    String konumu=konum.getText().toString();
//
//                                    database = FirebaseDatabase.getInstance();
//                                    ref = database.getReference().child("kullaniciKayitlari").child(kulad);
//                                    yazdırad=ref.child("ad");
//                                    yazdırad.setValue(adi);
//                                    yazdırsoyad=ref.child("soyad");
//                                    yazdırsoyad.setValue(soyadi);
//                                    yazdırkonum=ref.child("sehir");
//                                    yazdırkonum.setValue(konumu);
//                                    yazdırmaıl=ref.child("email");
//                                    yazdırmaıl.setValue(maili);
//                                    Toast.makeText(ProfileDuzenle.this, "Güncellendi. :)", Toast.LENGTH_SHORT).show();
//                                    Intent i = new Intent(ProfileDuzenle.this, AyarlarActivity.class);
//                                    startActivity(i);
//
//
//                                } else {
//                                    Toast.makeText(ProfileDuzenle.this, "Hatalı İşlem Yapıldı", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//
//
//                        } catch (Exception e) {
//                            Toast.makeText(ProfileDuzenle.this, "Hata Oluştu", Toast.LENGTH_SHORT).show();
//
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }
        });


    }


}