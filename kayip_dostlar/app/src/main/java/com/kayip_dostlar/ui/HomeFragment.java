package com.kayip_dostlar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.kayip_dostlar.Adapters.MyViewHolder;
import com.kayip_dostlar.Classes.Veri;
import com.kayip_dostlar.Pages.DetailActivity;
import com.kayip_dostlar.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Veri> options;
    FirebaseRecyclerAdapter<Veri, MyViewHolder>adapterr,adapterrr;
    DatabaseReference dataRef;
    String id;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView= (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        // Inflate the layout for this fragment
        dataRef= FirebaseDatabase.getInstance().getReference().child("ilanlar");
        final View rootView =(View) inflater.inflate(R.layout.fragment_home, container, false);
        options= new FirebaseRecyclerOptions.Builder<Veri>()
                .setQuery(dataRef.orderByChild("bulundumu").startAt("Bulundu Olarak İşaretle").endAt("Bulundu Olarak İşaretle" + "\uf8ff"),Veri.class).build();

        adapterr= new FirebaseRecyclerAdapter<Veri, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position, @NonNull Veri veri) {



                Picasso.get().load(veri.getImageurl()).resize(680,680).into(myViewHolder.imageView);
                myViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("key", getRef(position).getKey());
                        startActivity(intent);
                    }

                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cardveiw_item_book,parent,false);

                return new MyViewHolder(view);
            }

        };


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterr.startListening();
        recyclerView.setAdapter(adapterr);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapterr!=null)
        {
            adapterr.startListening();
        }
    }

    @Override
    public void onStop() {
        if (adapterr!=null){
            adapterr.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapterr!=null){
            adapterr.startListening();
        }
    }
}
