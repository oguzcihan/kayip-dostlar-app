package com.kayip_dostlar.ui;

import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kayip_dostlar.Adapters.MyAdapter;
import com.kayip_dostlar.Classes.Arama;
import com.kayip_dostlar.Pages.DetailActivity;
import com.kayip_dostlar.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {
    private View view;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Arama> options;
    FirebaseRecyclerAdapter<Arama, MyAdapter>adapterr;
    DatabaseReference dataRef;
    String id;
    EditText editText;


    public SearchFragment(){

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        editText=view.findViewById(R.id.edt_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i== EditorInfo.IME_ACTION_SEARCH){

                    String searchTerm=editText.getText().toString();
                    if(searchTerm.length()>0){

                        recyclerView= (RecyclerView) view.findViewById(R.id.ilanlarList);
                        recyclerView.setHasFixedSize(true);
                        // Inflate the layout for this fragment
                        dataRef= FirebaseDatabase.getInstance().getReference().child("ilanlar");
                        final View rootView =(View) inflater.inflate(R.layout.fragment_search, container, false);
                        options= new FirebaseRecyclerOptions.Builder<Arama>()
                                .setQuery(dataRef.orderByChild("baslik").startAt(textView.getText().toString()).endAt(textView.getText().toString() + "\uf8ff"),Arama.class).build();
                        adapterr= new FirebaseRecyclerAdapter<Arama, MyAdapter>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull MyAdapter myViewHolder, final int position, @NonNull Arama veri) {
                                myViewHolder.baslik.setText(veri.getBaslik());
                                myViewHolder.isim.setText(veri.getIsim());
                                Picasso.get().load(veri.getImageurl()).into(myViewHolder.imageView);
                                myViewHolder.view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                                        intent.putExtra("key",getRef(position).getKey());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search,parent,false);

                                return new MyAdapter(view);
                            }

                        };


                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapterr.startListening();
                        recyclerView.setAdapter(adapterr);
                    }else if (searchTerm.length()==0){
                        Toast.makeText(getContext(), "Bir şeyler yazın...", Toast.LENGTH_SHORT).show();
                    }
                }


                return false;
            }
        });

        return view;
    }
}
