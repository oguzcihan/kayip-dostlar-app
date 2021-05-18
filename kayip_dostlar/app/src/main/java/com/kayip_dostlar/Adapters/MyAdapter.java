package com.kayip_dostlar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kayip_dostlar.Classes.Veri;
import com.kayip_dostlar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class MyAdapter extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public View view;
    public TextView baslik,isim;

    public MyAdapter(View itemView) {
        super(itemView);
        this.imageView = itemView.findViewById(R.id.img_search);
        this.baslik=itemView.findViewById(R.id.txt_baslik);
        this.isim=itemView.findViewById(R.id.txt_isim);
        view=itemView;
    }


}

