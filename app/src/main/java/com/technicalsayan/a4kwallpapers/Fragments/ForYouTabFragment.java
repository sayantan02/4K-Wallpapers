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


public class ForYouTabFragment extends Fragment {


    public ForYouTabFragment() {
        // Required empty public constructor
    }
    RecyclerView rcv;
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_for_you_tab, container, false);

        rcv = view.findViewById(R.id.rc_view_foryou);
//        String Ctime = String.valueOf(System.currentTimeMillis());
//        String last3 = Ctime.substring(Ctime.length() - 3);
//        Log.d(TAG, "onCreateView: "+Ctime);

        int min = 20;
        int max = 50;

        //Generate random int value from 10 to 99
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);


        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("image-data").orderByChild("time").equalTo(random_int), Model.class)
                        .build();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        rcv.setLayoutManager(gridLayoutManager);
        adapter = new MyAdapter(options,getContext());
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