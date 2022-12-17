package com.technicalsayan.a4kwallpapers;


import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashScreenActivity extends AppCompatActivity {
ImageView splashimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        splashimage = findViewById(R.id.splashImage);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Glide.with(SplashScreenActivity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/k-wallpapers-16bef.appspot.com/o/default-images%2Fmalte-schmidt-enGr5YbjQKQ-unsplash.jpg?alt=media&token=d8da6845-6e9d-4b8a-9b29-a901f7446014")
                .into(splashimage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2*1000);

    }
}