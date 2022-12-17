package com.technicalsayan.a4kwallpapers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.technicalsayan.a4kwallpapers.Adapters.MyAdapter;
import com.technicalsayan.a4kwallpapers.Adapters.SectionsPagerAdapter;
import com.technicalsayan.a4kwallpapers.Models.Model;
//import com.technicalsayan.a4kwallpapers.Utils.AppOpenManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    
    Dialog dialog;
    ArrayList<String> arrayList;
    RecyclerView rcv;
    MyAdapter adapter;
    SharedPreferences sp;
    private AdView mAdView,adView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        //// Initialization
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        /////////////// Banner Ads /////////////////

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        //////////////// ./Banner Ads ///////////////////



        startActivity(new Intent(this,SplashScreenActivity.class));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        String default_tab = sp.getString("set_tab", "");

        if (default_tab.equals("for_you")){
            viewPager.setCurrentItem(1, true);
        }else{
            viewPager.setCurrentItem(0, true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.search:
                manualsearch();
                break;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                break;
            case R.id.About:
                Intent it = new Intent();
                it.setClass(MainActivity.this,SettingsActivity.class);
                it.putExtra("diff",1);
                startActivity(it);
                break;
            case R.id.aboutApp:
                Intent it2 = new Intent();
                it2.setClass(MainActivity.this,SettingsActivity.class);
                it2.putExtra("diff",2);
                startActivity(it2);
                break;
            case R.id.instagram:
                Uri uri = Uri.parse("http://instagram.com/_u/magnetic_boy_sayantan");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/magnetic_boy_sayantan")));
                }
        }

        if (id == R.id.search){
            manualsearch();
        }else if (id == R.id.settings){

        }
        return false;
    }

    private void manualsearch(){

        arrayList = new ArrayList<>();

    dialog = new Dialog(this);
    dialog.setContentView(R.layout.custom_view_searchable);

        /////////////// Banner Ads /////////////////
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adView2 = dialog.findViewById(R.id.adView3);
        adView2.loadAd(adRequest2);

        adView2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                adView2.loadAd(adRequest2);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        //////////////// ./Banner Ads ///////////////////

    dialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.MATCH_PARENT);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

    ((ViewGroup)dialog.getWindow().getDecorView())
            .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left));
    dialog.show();

    EditText ed = dialog.findViewById(R.id.searchText);
        rcv = dialog.findViewById(R.id.searchrcv);
        rcv.setVisibility(View.GONE);

    ed.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            rcv.setVisibility(View.VISIBLE);
            FirebaseRecyclerOptions<Model> options =
                    new FirebaseRecyclerOptions.Builder<Model>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("image-data").orderByChild("tag").startAt(editable.toString()).endAt(editable.toString()+"\uf8ff"), Model.class)
                            .build();

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
            rcv.setLayoutManager(gridLayoutManager);
            adapter = new MyAdapter(options,getApplicationContext());
            adapter.startListening();
            rcv.setAdapter(adapter);
        }
    });

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Are you Sure want to exit?");
        builder1.setMessage("Your Un Applyed work will be gone");
        builder1.setIcon(R.drawable.ic_baseline_error_24);
        builder1.setCancelable(true);

        AlertDialog.Builder neg = builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.teal_700);
        alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.red);
    }
}