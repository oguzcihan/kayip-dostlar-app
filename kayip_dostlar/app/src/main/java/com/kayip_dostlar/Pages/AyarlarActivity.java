package com.kayip_dostlar.Pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kayip_dostlar.MainActivity;
import com.kayip_dostlar.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AyarlarActivity extends Activity {
    ImageButton imgbtn;
    String kullaniciAdi;
    TextView isim,mail,konum;
    Button cıkıs,duzenle;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
        vericek();
        buton();
        cıkıss();


    }
    String ad,soyad,maill,konumm;
    public void vericek(){
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        kullaniciAdi =preferences.getString("deger","bos");

        isim=findViewById(R.id.edt_isim);
        mail=findViewById(R.id.edt_mail);
        konum=findViewById(R.id.edt_konum);
        DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("kullaniciKayitlari");
        oku.child(kullaniciAdi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    if(snapshot.exists()) {
                        ad = snapshot.child("ad").getValue().toString();
                        soyad=snapshot.child("soyad").getValue().toString();
                        isim.setText(ad+" "+soyad);
                        maill=snapshot.child("email").getValue().toString();
                        mail.setText(maill);
                        konumm=(snapshot.child("sehir").getValue().toString());
                        konum.setText(konumm);
                    }
                }
                catch (Exception e) {


                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void buton(){
        imgbtn=(ImageButton)findViewById(R.id.imageButtonayarlar);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AyarlarActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        duzenle=(Button)findViewById(R.id.btn_duzenle);
        duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(AyarlarActivity.this,ProfileDuzenle.class);
                i.putExtra("ad", ad);
                i.putExtra("soyad", soyad);
                i.putExtra("konum", konumm);
                i.putExtra("mail", maill);
                startActivity(i);
                finish();
            }
        });
    }
    public void cıkıss(){
        cıkıs=findViewById(R.id.buttoncikis);
        cıkıs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(AyarlarActivity.this);
                myBuilder.setTitle("Çıkış onay");
                myBuilder.setMessage("Çıkış yapmak istiyor musunuz?");
                myBuilder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
                        editor = preferences.edit();
                        Intent i = new Intent(AyarlarActivity.this, MainActivity.class);
                        preferences.edit().remove("deger").commit();
                        startActivity(i);
                    }
                });
                myBuilder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                myBuilder.show();
            }
        });
    }
}
