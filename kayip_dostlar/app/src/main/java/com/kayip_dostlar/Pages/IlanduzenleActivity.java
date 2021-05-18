package com.kayip_dostlar.Pages;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kayip_dostlar.MainActivity;
import com.kayip_dostlar.R;
import com.kayip_dostlar.ui.IlanlarimFragment;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class IlanduzenleActivity extends Activity {
    DatabaseReference reference;
    ImageView img;

    TextView ilannoo;
    EditText baslik, aciklama, isim, yas, telefon, adres;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    Button btn;
    String key;
    private Uri imageUri;

    ImageButton imgbtn;
    private static int fragmentManager;
    Fragment fr = null;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("ilanlar");
    DatabaseReference ref, yazdir_isim, yazdir_yas, yazdir_tel, yazdir_baslik, yazdir_adres, yazdir_detay, yazdir_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilanduzenle);
        foto_sec();

        img = findViewById(R.id.img_ilanduzenle);
        baslik = findViewById(R.id.txt_baslik_duzenle);
        aciklama = findViewById(R.id.txt_aciklama_duzenle);
        isim = findViewById(R.id.txt_isim_duzenle);
        yas = findViewById(R.id.txt_yas_duzenle);
        telefon = findViewById(R.id.txt_telefon_duzenle);
        adres = findViewById(R.id.txt_adres_duzenle);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        Intent intent = getIntent();
        String ilanNo = intent.getStringExtra("ilanNo");
        ilannoo = (TextView) findViewById(R.id.txt_gelenilanno);
        ilannoo.setText(ilanNo);

        getData();
        duzenle();
        geri();


    }

    public void getData() {
        key = ilannoo.getText().toString();
        reference = FirebaseDatabase.getInstance().getReference().child("ilanlar");
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                baslik.setText(snapshot.child("baslik").getValue().toString());
                String image = snapshot.child("imageurl").getValue().toString();
                Picasso.get().load(image).into(img);
                aciklama.setText(snapshot.child("detay").getValue().toString());
                isim.setText(snapshot.child("isim").getValue().toString());
                yas.setText(snapshot.child("yas").getValue().toString());
                telefon.setText(snapshot.child("telefon").getValue().toString());
                adres.setText(snapshot.child("adres").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void duzenle() {
        btn = (Button) findViewById(R.id.btn_ilanduzenle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oku.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try {

                            if (snapshot.exists()) {
                                String data = snapshot.child(key).getKey();
                                if (data.equals(key)) {
                                    String baslikk = baslik.getText().toString();
                                    String aciklamaa = aciklama.getText().toString();
                                    String isimm = isim.getText().toString();
                                    String yass = yas.getText().toString();
                                    String telefonn = telefon.getText().toString();
                                    String adress = adres.getText().toString();

                                    database = FirebaseDatabase.getInstance();
                                    ref = database.getReference().child("ilanlar").child(key);
                                    yazdir_baslik = ref.child("baslik");
                                    yazdir_baslik.setValue(baslikk);
                                    yazdir_adres = ref.child("adres");
                                    yazdir_adres.setValue(adress);
                                    yazdir_detay = ref.child("detay");
                                    yazdir_detay.setValue(aciklamaa);
                                    yazdir_yas = ref.child("yas");
                                    yazdir_yas.setValue(yass);
                                    yazdir_tel = ref.child("telefon");
                                    yazdir_tel.setValue(telefonn);
                                    yazdir_isim = ref.child("isim");
                                    yazdir_isim.setValue(isimm);
                                    uploadImage();

                                    Intent i = new Intent(IlanduzenleActivity.this, MainActivity.class);
                                    startActivity(i);
                                    Toast.makeText(IlanduzenleActivity.this, "Güncellendi. :)", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(IlanduzenleActivity.this, "Hatalı İşlem Yapıldı", Toast.LENGTH_SHORT).show();

                                }
                            }


                        } catch (Exception e) {
                            Toast.makeText(IlanduzenleActivity.this, "Hata Oluştu" + e.toString(), Toast.LENGTH_SHORT).show();

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

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {

        if (imageUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads/").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            //Log.d("DownloadUrl",url); //indirmek için kullan
                            /**/
                            yazdir_image = ref.child("imageurl");
                            yazdir_image.setValue(url);
                            Toast.makeText(IlanduzenleActivity.this, "Yükleme başarılı.", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }
    }

    public void foto_sec() {
        img = findViewById(R.id.img_ilanduzenle);
        img.setOnClickListener(new View.OnClickListener() {
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
            img.setImageURI(data.getData());

        }
    }

    public void geri() {
        imgbtn = (ImageButton) findViewById(R.id.imageBtn_back);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IlanduzenleActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
