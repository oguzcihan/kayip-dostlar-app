package com.kayip_dostlar.Pages;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kayip_dostlar.MainActivity;
import com.kayip_dostlar.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.SecureRandom;
import java.util.Random;


public class IlanVer extends Activity {
    ImageView mImageview;
    Button yukle;
    TextView textView, bulundu;
    EditText isim, yas, tel, ilanbaslik, adres, aciklama;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri imageUri;
    FirebaseDatabase database;
    DatabaseReference ref, yazdir_isim, yazdir_yas, yazdir_tel, yazdir_baslik, yazdir_adres, yazdir_detay, yazdir_image, yazdir_kullaniciadi, yazdir_bulundu;
    String kullaniciAdi;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilanver);
        close();
        foto_sec();
        ilanNO();
        tanimla();
        action();

    }

    public void tanimla() {
        bulundu = (TextView) findViewById(R.id.txt_bulundu);
        isim = (EditText) findViewById(R.id.edit_isim);
        yas = (EditText) findViewById(R.id.edit_yas);
        ilanbaslik = (EditText) findViewById(R.id.baslik);
        adres = (EditText) findViewById(R.id.adres);
        tel = (EditText) findViewById(R.id.edit_tel);
        aciklama = (EditText) findViewById(R.id.edit_aciklama);
        yukle = (Button) findViewById(R.id.btn_yukle);
    }

    public void action() {
        yukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
                editor = preferences.edit();
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(IlanVer.this);
                myBuilder.setTitle("İlan Onay");
                myBuilder.setMessage("İlanı onaylıyor musunuz?");
                myBuilder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            kullaniciAdi = preferences.getString("deger", "bos");

                            if (!isim.getText().toString().equals("") && !yas.getText().toString().equals("")
                                    && !ilanbaslik.getText().toString().equals("") && !adres.getText().toString().equals("")
                                    && !tel.getText().toString().equals("") && !aciklama.getText().toString().equals("")) {
                                String ilanNo = textView.getText().toString();
                                String i = isim.getText().toString();
                                String y = yas.getText().toString();
                                String baslik = ilanbaslik.getText().toString();
                                String adress = adres.getText().toString();
                                String telefon = tel.getText().toString();
                                String detay = aciklama.getText().toString();
                                String bulundumu = bulundu.getText().toString();
//                                String kullaniciAdii=ad.getText().toString();
                                /**/
                                database = FirebaseDatabase.getInstance();
                                ref = database.getReference().child("ilanlar").child(ilanNo);
                                yazdir_isim = ref.child("isim");
                                yazdir_isim.setValue(i);
                                /**/
                                yazdir_yas = ref.child("yas");
                                yazdir_yas.setValue(y);
                                /**/
                                yazdir_baslik = ref.child("baslik");
                                yazdir_baslik.setValue(baslik);
                                /**/
                                yazdir_adres = ref.child("adres");
                                yazdir_adres.setValue(adress);
                                /**/
                                yazdir_tel = ref.child("telefon");
                                yazdir_tel.setValue(telefon);
                                /**/
                                yazdir_detay = ref.child("detay");
                                yazdir_detay.setValue(detay);
                                /**/
                                yazdir_bulundu = ref.child("bulundumu");
                                yazdir_bulundu.setValue(bulundumu);
                                /**/
                                yazdir_kullaniciadi = ref.child("kullaniciAdi");
                                yazdir_kullaniciadi.setValue(kullaniciAdi);
                                /**/

                                if (imageUri != null) {

                                    uploadImage();
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri2 = uri.toString();
                                            yazdir_image = ref.child("imageurl");
                                            yazdir_image.setValue(uri2);

                                        }
                                    });
                                    Intent intent = new Intent(IlanVer.this, MainActivity.class);
                                    startActivity(intent);
//                                    Toast.makeText(IlanVer.this, "Yükleme başarılı.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(IlanVer.this, "Fotoğraf Seçiniz.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(IlanVer.this, "Lütfen boş alan bırakmayınız!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });
                myBuilder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                myBuilder.show();

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    StorageReference fileRef;
    String url;

    private void uploadImage() {

        fileRef = FirebaseStorage.getInstance().getReference().child("uploads/").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url = uri.toString();
                        //Log.d("DownloadUrl",url); //indirmek için kullan
                        /**/
                        yazdir_image = ref.child("imageurl");
                        yazdir_image.setValue(url);

                        Toast.makeText(IlanVer.this, "Yükleme başarılı.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(IlanVer.this, "Tekrar dene!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void ilanNO() {
        textView = findViewById(R.id.randomNumber);
        textView.setText(generateSessionKey(5));
    }

    public static String generateSessionKey(int length) {
        String alphabet =
                new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); // 9
        int n = alphabet.length(); // 10
        String result = new String();
        Random r = new Random(); // 11
        for (int i = 0; i < length; i++) // 12
            result = result + alphabet.charAt(r.nextInt(n)); //13
        return result;
    }

    public void close() {

        ImageView view = (ImageView) findViewById(R.id.close);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IlanVer.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void foto_sec() {
        mImageview = findViewById(R.id.img_foto);
        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "İstek reddedildi.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data.getData();
            mImageview.setImageURI(data.getData());

        }
    }
}
