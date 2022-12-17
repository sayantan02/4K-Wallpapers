package com.technicalsayan.a4kwallpapers.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.technicalsayan.a4kwallpapers.R;

public class AboutMeFragment extends Fragment {

    public AboutMeFragment() {
        // Required empty public constructor
    }
    ImageView aboutImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_me, container, false);
        aboutImage = view.findViewById(R.id.about_image);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/k-wallpapers-16bef.appspot.com/o/default-images%2FDSC_1834%20(1).jpg?alt=media&token=73b12643-aaa9-4f7f-9c2b-d14e3c4d31cb")
                .into(aboutImage);



        return view;
    }
}