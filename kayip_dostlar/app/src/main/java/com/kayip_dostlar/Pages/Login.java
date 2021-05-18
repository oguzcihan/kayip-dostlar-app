package com.kayip_dostlar.Pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kayip_dostlar.MainActivity;
import com.kayip_dostlar.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Login extends FragmentActivity {

    EditText kullaniciAdi, sifre;
    Button giris, uyeOl;
    private TextView txtsifreunuttum;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String AES = "AES";
    private static FragmentManager fragmentManager;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("kullaniciKayitlari");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tanimlama();
        LoginEventHandlers();
        close();
        kayit();
        fragmentManager = getSupportFragmentManager();
    }

//    public void sifreunuttum() {
//        txtsifreunuttum = findViewById(R.id.textVievSifremiUnuttun);
//        txtsifreunuttum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Login.this, SignUp.class);
//                startActivity(i);
//            }
//        });
//    }

    public void close() {

        ImageView view = (ImageView) findViewById(R.id.imageClose);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("deger", "");
                startActivity(intent);
                finish();
            }
        });
    }

    public void tanimlama() {
        kullaniciAdi = (EditText) findViewById(R.id.inputkullaniciadi);
        sifre = (EditText) findViewById(R.id.inputSifre);
        giris = (Button) findViewById(R.id.btnGirisYap);
        uyeOl = (Button) findViewById(R.id.loginuyeOl);
    }

    public void kayit() {
        uyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });

    }

    private void userLogin_onClick() {
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oku.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String ad = kullaniciAdi.getText().toString();
                        String sifree = sifre.getText().toString();
                        String sifresi = null;
                        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
                        editor = preferences.edit();

                        try {

                            if (snapshot.exists()) {
                                String data = snapshot.child(ad).getKey();
                                String encsifre = snapshot.child(ad).child("sifre").getValue().toString();
                                sifresi = decrypt(ad, encsifre);

                                if (data.equals(ad)) {
                                    if (sifresi.equals(sifree)) {

                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        editor.putString("deger", data);
                                        editor.commit();
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(Login.this, "Sifreyi Kontrol Ediniz", Toast.LENGTH_LONG).show();

                                    }
                                }
                            }

                        } catch (Exception e) {
                            Toast.makeText(Login.this, "Hatalı Giriş", Toast.LENGTH_LONG).show();

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

    private SecretKeySpec generateKey(String kullanici) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = kullanici.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;

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

    private void LoginEventHandlers() {
        userLogin_onClick();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean checkOnisim = checkEditText(kullaniciAdi);
                boolean checkOnsifre = checkEditText(sifre);
                if (checkOnisim && checkOnsifre) {
                    giris.setEnabled(true);

                } else {
                    giris.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        kullaniciAdi.addTextChangedListener(textWatcher);
        sifre.addTextChangedListener(textWatcher);
    }

    private boolean checkEditText(EditText editText) {
        String value = editText.getText().toString();
        if (value.length() < 3) { // kosul
            Drawable errorImage = ContextCompat.getDrawable(Login.this,
                    R.drawable.ic_baseline_error_24);
            int height = errorImage.getIntrinsicHeight();
            int width = errorImage.getIntrinsicWidth();
            errorImage.setBounds(0, 0, width, height);
            editText.setError("Bu alan en az 3 karakterden oluşmalıdır!", errorImage);
            return false;
        } else
            return true;

    }
}
