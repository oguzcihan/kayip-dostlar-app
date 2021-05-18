package com.kayip_dostlar.Pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kayip_dostlar.MainActivity;
import com.kayip_dostlar.R;
import com.kayip_dostlar.ui.IlanlarimFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Profile extends Activity {
    TextView isim, konum, tarih;
    Button sifredegistir, ilangoruntu, hesapsil;
    String kullaniciAdi;
    ImageButton imgbtn;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Task<Void> ref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        butonlar();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        kullaniciAdi = preferences.getString("deger", "bos");
        isim = findViewById(R.id.textViewisim);
        konum = findViewById(R.id.textViewkonum);
        tarih = findViewById(R.id.textViewtarihtenberiuye);
        DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("kullaniciKayitlari");
        oku.child(kullaniciAdi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {

                        String adsoyad=snapshot.child("ad").getValue().toString()+" "+snapshot.child("soyad").getValue().toString();
                        isim.setText(adsoyad);
                        konum.setText(snapshot.child("sehir").getValue().toString());
                        String date = snapshot.child("tarih").getValue().toString();

                        tarih.setText(date + " den beri üye");


                    }
                } catch (Exception e) {


                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void butonlar() {
        sifredegistir = (Button) findViewById(R.id.buttonsifremidegistir);
        sifredegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, SifreDegistirActivity.class);
                startActivity(i);
                finish();

            }
        });


        imgbtn = (ImageButton) findViewById(R.id.imageButtonprofile);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                intent.putExtra("deger", kullaniciAdi);
                startActivity(intent);
                finish();
            }
        });

        ilangoruntu = (Button) findViewById(R.id.buttonilanlarigoruntule);
        ilangoruntu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        hesapsil = (Button) findViewById(R.id.btn_hesabsil);
        hesapsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(Profile.this);
                myBuilder.setTitle("Siliniyor");
                myBuilder.setMessage("Hesabınız silinsin mi?");
                myBuilder.setIcon(R.drawable.person);
                myBuilder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        database = FirebaseDatabase.getInstance();
                        ref = database.getReference().child("kullaniciKayitlari").child(kullaniciAdi).removeValue();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query sQuery = ref.child("ilanlar").orderByChild("kullaniciAdi").equalTo(kullaniciAdi);

                        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                                    Snapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
                        editor = preferences.edit();
                        Toast.makeText(Profile.this, "Hesabınız silindi.", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(Profile.this, Login.class);
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
