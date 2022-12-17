package com.technicalsayan.a4kwallpapers.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.technicalsayan.a4kwallpapers.Adapters.MyAdapter;
import com.technicalsayan.a4kwallpapers.Models.Model;
import com.technicalsayan.a4kwallpapers.R;

public class ProPicsTabFragment extends Fragment {

    public ProPicsTabFragment() {
        // Required empty public constructor
    }

    RecyclerView rcv;
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pro_pics_tab, container, false);

        rcv = view.findViewById(R.id.rc_view_pro);

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("image-data").orderByChild("pro").equalTo("true"), Model.class)
                        .build();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rcv.setLayoutManager(gridLayoutManager);
        adapter = new MyAdapter(options, getContext());
        rcv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}