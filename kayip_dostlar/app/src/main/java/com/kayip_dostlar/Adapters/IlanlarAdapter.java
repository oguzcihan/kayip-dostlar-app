package com.kayip_dostlar.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;
import com.kayip_dostlar.Classes.Ilanlar;
import com.kayip_dostlar.Pages.IlanduzenleActivity;
import com.kayip_dostlar.R;
import com.kayip_dostlar.ui.IlanlarimFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class IlanlarAdapter extends RecyclerView.Adapter<IlanlarAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Ilanlar> ilanlar;
    FirebaseDatabase database;
    Task<Void> ref;
    DatabaseReference reff, getbulundu;
    private static int fragmentManager;
    Fragment fr = null;

    public IlanlarAdapter(Context ctx, List<Ilanlar> ilanlar) {
        this.inflater = LayoutInflater.from(ctx);
        this.ilanlar = ilanlar;

    }

    @NonNull
    @Override
    public IlanlarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_ilanlarim, parent, false);

        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView detay, ilanbaslik, ilanNo;
        ImageView imghayvan;
        ImageButton trash, update;
        Button btn;

        public ViewHolder(View ItemView) {
            super(ItemView);
            detay = ItemView.findViewById(R.id.txt_detay);
            ilanbaslik = ItemView.findViewById(R.id.txt_ilanbaslik);
            imghayvan = ItemView.findViewById(R.id.img_ilanlarim);
            ilanNo = ItemView.findViewById(R.id.txt_ilanno);
            trash = ItemView.findViewById(R.id.imgbtn_trash);
            btn = ItemView.findViewById(R.id.btn_isaretle);
            update = ItemView.findViewById(R.id.imgbtn_update);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.detay.setText(ilanlar.get(position).getIlan_detay());
        holder.ilanbaslik.setText(ilanlar.get(position).getIlan_baslik());
        holder.ilanNo.setText(ilanlar.get(position).getIlanNo());
        holder.btn.setText(ilanlar.get(position).getBulundu());

        Picasso.get()
                .load(ilanlar.get(position).getImageurl())
                .into(holder.imghayvan);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bulundu = "Bulundu";
                FirebaseDatabase.getInstance().getReference().child("ilanlar").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            database = FirebaseDatabase.getInstance();
                            reff = database.getReference().child("ilanlar").child(holder.ilanNo.getText().toString());
                            String butonisim = holder.btn.getText().toString();
                            if (butonisim.equals("Bulundu")) {
                                getbulundu = reff.child("bulundumu");
                                getbulundu.setValue("Bulundu Olarak İşaretle");
                            } else {
                                getbulundu = reff.child("bulundumu");
                                getbulundu.setValue(bulundu);
                            }

                            fr = new IlanlarimFragment();
                            fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager()
                                    .beginTransaction().replace(R.id.fragment_container, fr).commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(v.getContext());
                myBuilder.setTitle("Siliniyor");
                myBuilder.setMessage("İlanı silmek istiyor musunuz?");
                myBuilder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        database = FirebaseDatabase.getInstance();
                        ref = database.getReference().child("ilanlar").child(holder.ilanNo.getText().toString()).removeValue();
                        Toast.makeText(v.getContext(), "İlanınız silindi.", Toast.LENGTH_SHORT).show();
                        fr = new IlanlarimFragment();

                        fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                                .beginTransaction().replace(R.id.fragment_container, fr).commit();


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

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), IlanduzenleActivity.class);
                i.putExtra("ilanNo",holder.ilanNo.getText().toString());
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ilanlar.size();
    }

}
