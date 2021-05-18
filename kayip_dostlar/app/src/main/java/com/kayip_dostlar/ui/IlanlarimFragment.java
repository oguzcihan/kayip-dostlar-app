package com.kayip_dostlar.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kayip_dostlar.Adapters.IlanlarAdapter;
import com.kayip_dostlar.Classes.Ilanlar;
import com.kayip_dostlar.MainActivity;
import com.kayip_dostlar.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IlanlarimFragment extends Fragment {
    private View view;
    TextView textView;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    private List<Ilanlar> ilanlars;
    private IlanlarAdapter adapter;
    FirebaseDatabase database;
    String ad, baslik, detay, imageurl, ilanno, bulundu;
    DatabaseReference ref;

    public IlanlarimFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ilanlarim, container, false);
        ilanlars = new ArrayList<>();
        setUpAction(view);
        return view;
    }

    private void setUpAction(View view) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());//preferences objesi
        editor = preferences.edit();
        String kullaniciAdi = preferences.getString("deger", "bos");

        try {
            ref = database.getInstance().getReference().child("ilanlar");
            ref.orderByChild("kullaniciAdi").startAt(kullaniciAdi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    ad = snapshot.child("kullaniciAdi").getValue().toString();
                    if (ad.equals(kullaniciAdi)) {
                        baslik = snapshot.child("baslik").getValue().toString();
                        detay = snapshot.child("detay").getValue().toString();
                        imageurl = snapshot.child("imageurl").getValue().toString();
                        bulundu = snapshot.child("bulundumu").getValue().toString();
                        ilanno = snapshot.getKey().toString();
                        Ilanlar ilanlar = new Ilanlar();

                        if (detay.length() > 50) {
                            ilanlar.setIlan_detay( detay.substring(0, 30));
                            ilanlar.setIlan_baslik(baslik);
                            ilanlar.setImageurl(imageurl);
                            ilanlar.setIlanNo(ilanno);
                            ilanlar.setBulundu(bulundu);
                            ilanlars.add(ilanlar);
                        }else{
                            ilanlar.setIlan_detay(detay);
                            ilanlar.setIlan_baslik(baslik);
                            ilanlar.setImageurl(imageurl);
                            ilanlar.setIlanNo(ilanno);
                            ilanlar.setBulundu(bulundu);
                            ilanlars.add(ilanlar);
                        }

                    }
                    recyclerView = view.findViewById(R.id.ilanlarList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new IlanlarAdapter(getContext(), ilanlars);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Hata Tekrar Deneyiniz!" + ex.toString(), Toast.LENGTH_SHORT).show();
        }


    }
}
