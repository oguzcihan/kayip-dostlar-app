package com.kayip_dostlar.Pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kayip_dostlar.R;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class SignUp extends Activity {
    private Button uyeOl;
    private EditText ad, soyad, mail, kullaniciAdi, sifre,sehir;
    private FirebaseDatabase database;
    private DatabaseReference ref, yazdir_ad, yazdir_soyad, yazdir_mail, yazdir_sifre,yazdir_sehir, yazdir_tarih;
    String AES = "AES";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayitol);
        tanimla();
        Action();
        close();

    }

    public void tanimla() {
        ad = (EditText) findViewById(R.id.txtAd);
        soyad = (EditText) findViewById(R.id.txtSoyad);
        mail = (EditText) findViewById(R.id.txtmail);
        kullaniciAdi = (EditText) findViewById(R.id.edit_kullaniciAdi);
        sifre = (EditText) findViewById(R.id.txtSifre);
        sehir=(EditText)findViewById(R.id.txtsehir);
        uyeOl = (Button) findViewById(R.id.btn_kayitol);

    }

    /*Çarpı simgesi*/
    public void close() {

        ImageView view = (ImageView) findViewById(R.id.imageClose);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void Action() {

        uyeOl.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    if (!ad.getText().toString().equals("") && !soyad.getText().toString().equals("") && !mail.getText().toString().equals("") && !kullaniciAdi.getText().toString().equals("") && !sifre.getText().toString().equals("")&&!sehir.getText().toString().equals("")) {

                        SimpleDateFormat bicim=new SimpleDateFormat("dd/M/yyyy");
                        Date tarih=new Date();

                        String adi = ad.getText().toString();
                        String soyadi = soyad.getText().toString();
                        String maili = mail.getText().toString();
                        String kullanici = kullaniciAdi.getText().toString();
                        String sehiri=sehir.getText().toString();
                        String sifresi = encrypt(kullanici,sifre.getText().toString());

                        database = FirebaseDatabase.getInstance();
                        ref = database.getReference().child("kullaniciKayitlari").child(kullanici);
                        yazdir_ad = ref.child("ad");
                        yazdir_ad.setValue(adi);
                        //
                        yazdir_soyad = ref.child("soyad");
                        yazdir_soyad.setValue(soyadi);
                        //
                        yazdir_sehir = ref.child("sehir");
                        yazdir_sehir.setValue(sehiri);
                        //
                        yazdir_mail = ref.child("email");
                        yazdir_mail.setValue(maili);
                        //
                        yazdir_sifre = ref.child("sifre");
                        yazdir_sifre.setValue(sifresi);
                        //
                        yazdir_tarih = ref.child("tarih");
                        yazdir_tarih.setValue(bicim.format(tarih));

                        Toast.makeText(SignUp.this, "Kayıt başarılı :)", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUp.this, Login.class);
                        startActivity(i);


                    } else {
                        Toast.makeText(SignUp.this, "Lütfen boş alan bırakmayınız!", Toast.LENGTH_SHORT).show();

                    }

                } catch (
                        Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

//    private String decrypt(String kullanici, String password) throws Exception {
//        SecretKeySpec key = generateKey(kullanici);
//        Cipher c = Cipher.getInstance(AES);
//        c.init(Cipher.DECRYPT_MODE, key);
//        byte[] decodedValue = Base64.decode(password, Base64.DEFAULT);
//        byte[] decValue = c.doFinal(decodedValue);
//        String decryptedVal = new String(decValue);
//        return decryptedVal;
//    }

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
