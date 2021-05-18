package com.kayip_dostlar.Pages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
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

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SifreDegistirActivity extends Activity {
    private ImageButton imgbtn;
    private EditText mevcutsifre, yeni_sifre, yeni_sifretekrar;
    private String AES = "AES";
    private Button kaydet;
    private String kulad;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("kullaniciKayitlari");
    private DatabaseReference ref, yazdirsifre;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifredegisikligi);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        mevcutsifre = (EditText) findViewById(R.id.edt_mevcutsifre);
        yeni_sifre = (EditText) findViewById(R.id.edt_yenisifre);
        yeni_sifretekrar = (EditText) findViewById(R.id.edt_yenisifretekrar);
        kaydet = (Button) findViewById(R.id.btn_sifrekaydet);

        buton();
        action();
    }

    public void buton() {
        imgbtn = (ImageButton) findViewById(R.id.imageButtonback);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SifreDegistirActivity.this, Profile.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void action() {
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oku.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        kulad = preferences.getString("deger", "bos");

                        try {
                            String decsifre = null;
                            if (snapshot.exists()) {

                                String data = snapshot.child(kulad).getKey();
                                String sifre = (String) snapshot.child(kulad).child("sifre").getValue();
                                decsifre = decrypt(data, sifre);
                                String msifre = mevcutsifre.getText().toString();
                                String ysifre = yeni_sifre.getText().toString();
                                String ytsifre = yeni_sifretekrar.getText().toString();

                                if (decsifre.equals(msifre)) {

                                    if (ysifre.equals(ytsifre)) {

                                        String encsifre = encrypt(data, ytsifre);
                                        database = FirebaseDatabase.getInstance();
                                        ref = database.getReference().child("kullaniciKayitlari").child(kulad);
                                        yazdirsifre = ref.child("sifre");
                                        yazdirsifre.setValue(encsifre);

                                        Toast.makeText(SifreDegistirActivity.this, "Şifreniz değiştirildi.", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(SifreDegistirActivity.this, Profile.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(SifreDegistirActivity.this, "Şifre tekrarı hatalı girildi!", Toast.LENGTH_SHORT).show();
                                    }
                                }
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
        });


    }

    private String decrypt(String kullanici, String password) throws Exception {
        SecretKeySpec key = generateKey(kullanici);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(password, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedVal = new String(decValue);
        return decryptedVal;
    }

    private String encrypt(String Data, String password) throws Exception {
        SecretKeySpec key = generateKey(Data);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(password.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String kullanici) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = kullanici.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;

    }
}
