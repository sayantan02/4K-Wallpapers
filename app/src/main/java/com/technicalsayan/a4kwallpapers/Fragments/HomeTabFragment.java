package com.technicalsayan.a4kwallpapers.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.technicalsayan.a4kwallpapers.Adapters.MyAdapter;
import com.technicalsayan.a4kwallpapers.Models.Model;
import com.technicalsayan.a4kwallpapers.R;



public class HomeTabFragment extends Fragment {



    public HomeTabFragment() {
        // Required empty public constructor
    }

    RecyclerView rcv;
    MyAdapter adapter;
    boolean APP_HAS_UPDATE = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_tab, container, false);

        Query query = FirebaseDatabase.getInstance().getReference().child("app-update");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                /// This @string/app_version should be changed like below ....
                if (!snapshot.child("version").getValue().equals(R.string.app_version)){
                    APP_HAS_UPDATE = true;
                } else {
                    APP_HAS_UPDATE = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                if (APP_HAS_UPDATE == true){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle("A new Version is Available");
                    builder1.setMessage("Stay Updated for latest patches.");
                    builder1.setIcon(R.drawable.ic_baseline_new_releases_24);
                    builder1.setCancelable(true);



                    AlertDialog.Builder neg = builder1.setPositiveButton(
                            "Update Now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.technicalsayan.a4kwallpapers");
                                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                                    likeIng.setPackage("com.android.vending");

                                    try {
                                        startActivity(likeIng);
                                    } catch (ActivityNotFoundException e) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.technicalsayan.a4kwallpapers")));
                                    }
                                }
                            });

                    builder1.setNegativeButton(
                            "ignore",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.red);
                    alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.green);
                }
            }
        },2000);

        rcv = view.findViewById(R.id.rc_view_home);



        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("image-data"), Model.class)
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