package com.kayip_dostlar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.PreferenceManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kayip_dostlar.Pages.AyarlarActivity;
import com.kayip_dostlar.Pages.IlanVer;
import com.kayip_dostlar.Pages.Login;
import com.kayip_dostlar.Pages.Profile;
import com.kayip_dostlar.ui.HomeFragment;
import com.kayip_dostlar.ui.IlanlarimFragment;
import com.kayip_dostlar.ui.SearchFragment;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    public String kullaniciAdi;
    public String out;
    ImageView imageView, imageView2;
    SwitchCompat dark;
    FloatingActionButton fab;
    private static FragmentManager fragmentManager;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dark=findViewById(R.id.imgdark);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        kullaniciAdi =preferences.getString("deger","bos");
        fabmenu();
        profil();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;


            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    toolbar.setTitle("Anasayfa");
                    //return true;
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    toolbar.setTitle("Ara");
                    //işlemler
                    break;
//                case R.id.nav_notification:
//                    selectedFragment = new NotificationFragment();
//          maşnde değişilik yaptın mı hiç yoksa  vardır değişikilik tamam dur ozaman burda yeniden yapam
//                    if (kullaniciAdi.equals("")) {
//                        Intent intent1 = new Intent(MainActivity.this
//                                , Login.class);
//                        startActivity(intent1);
//                    }
//                    toolbar.setTitle("Bildirimler");
//                    //işlemler
//                    break;
                case R.id.nav_ilanlar:
                    selectedFragment = new IlanlarimFragment();

                    if (kullaniciAdi.equals("bos")) {
                        Intent intent1 = new Intent(MainActivity.this
                                , Login.class);
                        startActivity(intent1);

                    }
                    toolbar.setTitle("İlanlarım");
                    //işlemler
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

    public void fabmenu() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kullaniciAdi.equals("bos")) {
                    Intent intent1 = new Intent(MainActivity.this
                            , Login.class);
                    startActivity(intent1);
                } else {
                    Intent i = new Intent(MainActivity.this, IlanVer.class);
                    startActivity(i);
                }


            }
        });

    }

    public void profil(){
        imageView=(ImageView)toolbar.findViewById(R.id.imgprofil);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kullaniciAdi.equals("bos")) {
                    Intent intent1 = new Intent(MainActivity.this
                            , Login.class);
                    startActivity(intent1);
                }else{
                    Intent i=new Intent(MainActivity.this, Profile.class);
                    startActivity(i);
                }

            }
        });
        imageView2=(ImageView)toolbar.findViewById(R.id.imgsettings);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kullaniciAdi.equals("bos")) {
                    Intent intent1 = new Intent(MainActivity.this
                            , Login.class);
                    startActivity(intent1);
                }else{
                    Intent i=new Intent(MainActivity.this, AyarlarActivity.class);
                    startActivity(i);
                }
            }
        });

        dark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    dark.setChecked(true);
                    editor = preferences.edit();
                    editor.putBoolean("night_mode",true);
                    editor.commit();
                    toolbar.setBackgroundColor(Color.parseColor("#808080"));

                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    dark.setChecked(false);
                    editor=preferences.edit();
                    editor.putBoolean("light_mode",false);
                    editor.commit();
                }
            }
        });

    }

}