package com.technicalsayan.a4kwallpapers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.technicalsayan.a4kwallpapers.Adapters.MyAdapter;
import com.technicalsayan.a4kwallpapers.Models.Model;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static android.os.Build.VERSION.SDK_INT;
import static com.technicalsayan.a4kwallpapers.Constants.loadRewardedAd;

public class ViewImageActivity extends AppCompatActivity {
    ImageView fullimage, setwallpaper, info, download;
    boolean hidden = true;
    WallpaperManager wallpaperManager;
    BottomSheetBehavior bottomSheetBehavior;
    RecyclerView rcv;
    MyAdapter adapter;
    LinearLayout bottomlin;
    LinearLayoutCompat bottomsheetlayout;
    Intent it;
    SharedPreferences preferences;
    private boolean permissionsGranted;
    long downloadID;
    String imageName;
    ProgressDialog dialog;
    String child,tag,Source;
    int downloaded,popularity;
    ProgressBar progressBar;
    TextView banner_popup;
    SharedPreferences sp;
    private AdView mAdView;
    RewardedAd rewardedAd;
    public static String rewardedAdId = "ca-app-pub-5615101169407494/2217708224";
    boolean issetwallpaper = false;





    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                if (issetwallpaper){
                    dialog.dismiss();
                    File image;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        image = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/setHDWallpaper/"+imageName+".jpeg");
                    }else{
                        image = new File("/sdcard/.HDWallpaper/"+imageName+".jpeg");
                    }

                    if (image.exists()) {
                        Dialog dialog1;
                        dialog1 = new Dialog(context);
                        dialog1.setContentView(R.layout.custom_wallpaper_set);
                        dialog1.getWindow().setLayout(Toolbar.LayoutParams.WRAP_CONTENT,Toolbar.LayoutParams.WRAP_CONTENT);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ((ViewGroup)dialog1.getWindow().getDecorView())
                                .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left));
                        dialog1.show();

                        Button sethome = dialog1.findViewById(R.id.sethome);
                        Button setlock = dialog1.findViewById(R.id.setlock);
                        Button setboth = dialog1.findViewById(R.id.setboth);
                        banner_popup = dialog1.findViewById(R.id.banner_popup);
                        progressBar = dialog1.findViewById(R.id.progressBar);

                        sethome.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                banner_popup.setText("Setting Wallpaper");
                                progressBar.setVisibility(View.VISIBLE);
                                sethome.setVisibility(View.GONE);
                                setlock.setVisibility(View.GONE);
                                setboth.setVisibility(View.GONE);
                                Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();

                                ////////// Load rewarded Ad ////////////
                                rewardedAd.load(ViewImageActivity.this, rewardedAdId, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                                    @Override
                                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                        super.onAdLoaded(rewardedAd);
                                        rewardedAd.show(ViewImageActivity.this, new OnUserEarnedRewardListener() {
                                            @Override
                                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        super.onAdFailedToLoad(loadAdError);
                                        loadRewardedAd(ViewImageActivity.this);
                                    }
                                });
                                ////////// Load rewarded Ad ////////////

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap imageBitmap;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                            imageBitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/setHDWallpaper/" + imageName + ".jpeg");
                                        }else{
                                            imageBitmap = BitmapFactory.decodeFile("/sdcard/.HDWallpaper/" + imageName + ".jpeg");
                                        }

                                        try {
                                            if (SDK_INT >= Build.VERSION_CODES.N) {
                                                wallpaperManager.setBitmap(imageBitmap,null,true,WallpaperManager.FLAG_SYSTEM);
                                            }else{
                                                Toast.makeText(context, "Your Android version is low to set Wallpaper on lock screen", Toast.LENGTH_SHORT).show();
                                                wallpaperManager.setBitmap(imageBitmap);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(ViewImageActivity.this, "Wallpaper Set Successfully", Toast.LENGTH_SHORT).show();
                                        issetwallpaper = false;
                                        dialog1.dismiss();
                                    }
                                },1000);
                            }
                        });
                        setlock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                banner_popup.setText("Setting Wallpaper");
                                progressBar.setVisibility(View.VISIBLE);
                                sethome.setVisibility(View.GONE);
                                setlock.setVisibility(View.GONE);
                                setboth.setVisibility(View.GONE);
                                Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();

                                ////////// Load rewarded Ad ////////////
                                rewardedAd.load(ViewImageActivity.this, rewardedAdId, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                                    @Override
                                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                        super.onAdLoaded(rewardedAd);
                                        rewardedAd.show(ViewImageActivity.this, new OnUserEarnedRewardListener() {
                                            @Override
                                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        super.onAdFailedToLoad(loadAdError);
                                        loadRewardedAd(ViewImageActivity.this);
                                    }
                                });
                                ////////// Load rewarded Ad ////////////

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap imageBitmap;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                            imageBitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/setHDWallpaper/" + imageName + ".jpeg");
                                        }else{
                                            imageBitmap = BitmapFactory.decodeFile("/sdcard/.HDWallpaper/" + imageName + ".jpeg");
                                        }
                                        try {
                                            if (SDK_INT >= Build.VERSION_CODES.N) {
                                                wallpaperManager.setBitmap(imageBitmap,null,false,WallpaperManager.FLAG_LOCK);
                                            }else{
                                                Toast.makeText(context, "Your Android version is low to set on lock screen", Toast.LENGTH_SHORT).show();
                                                wallpaperManager.setBitmap(imageBitmap);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(ViewImageActivity.this, "Wallpaper Set Successfully", Toast.LENGTH_SHORT).show();
                                        issetwallpaper = false;
                                        dialog1.dismiss();
                                    }
                                },1000);
                            }
                        });
                        setboth.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                banner_popup.setText("Setting Wallpaper");
                                progressBar.setVisibility(View.VISIBLE);
                                sethome.setVisibility(View.GONE);
                                setlock.setVisibility(View.GONE);
                                setboth.setVisibility(View.GONE);
                                Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();

                                ////////// Load rewarded Ad ////////////
                                rewardedAd.load(ViewImageActivity.this, rewardedAdId, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                                    @Override
                                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                        super.onAdLoaded(rewardedAd);
                                        rewardedAd.show(ViewImageActivity.this, new OnUserEarnedRewardListener() {
                                            @Override
                                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        super.onAdFailedToLoad(loadAdError);
                                        loadRewardedAd(ViewImageActivity.this);
                                    }
                                });
                                ////////// Load rewarded Ad ////////////

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap imageBitmap;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                            imageBitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/setHDWallpaper/" + imageName + ".jpeg");
                                        }else{
                                            imageBitmap = BitmapFactory.decodeFile("/sdcard/.HDWallpaper/" + imageName + ".jpeg");
                                        }
                                        try {
                                            if (SDK_INT >= Build.VERSION_CODES.N) {
                                                wallpaperManager.setBitmap(imageBitmap,null,true,WallpaperManager.FLAG_SYSTEM);
                                                wallpaperManager.setBitmap(imageBitmap,null,false,WallpaperManager.FLAG_LOCK);
                                            }else{
                                                Toast.makeText(context, "Your Android version is low to set Wallpaper on lock screen", Toast.LENGTH_SHORT).show();
                                                wallpaperManager.setBitmap(imageBitmap);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(ViewImageActivity.this, "Wallpaper Set Successfully", Toast.LENGTH_SHORT).show();
                                        issetwallpaper = false;
                                        dialog1.dismiss();
                                    }
                                },1000);
                            }
                        });
                    }else{
                        Toast.makeText(ViewImageActivity.this, "something Went Wrong", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        FileOutputStream fos;
                        try {
                            File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/HDWallpaper/",imageName + ".jpeg");
                            Bitmap bitmapImage = BitmapFactory.decodeStream(new FileInputStream(file));
                            try {
                                ContentResolver resolver = getContentResolver();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,generateRandomString()+".jpeg");
                                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
                                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES+File.separator+"4kHdWallpapers");
                                Uri imageUrl = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                                fos = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageUrl));
                                bitmapImage.compress(Bitmap.CompressFormat.JPEG,100,fos);
                                Objects.requireNonNull(fos);
                                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){ Toast.makeText(context, "Not Downloaded", Toast.LENGTH_SHORT).show(); }

                        } catch (FileNotFoundException e) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        /// Initialize
        fullimage = findViewById(R.id.fullimage);
        setwallpaper = findViewById(R.id.setwallpaper);
        bottomsheetlayout = findViewById(R.id.bottomheetbehavior);
        info = findViewById(R.id.info);
        download = findViewById(R.id.download);
        rcv = findViewById(R.id.rc_view2);
        bottomlin = findViewById(R.id.linbottom);

        /// Basic Functionalities
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        sp = PreferenceManager.getDefaultSharedPreferences(ViewImageActivity.this);

        /// Handling Bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheetlayout);
        bottomSheetBehavior.setPeekHeight(400);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == bottomSheetBehavior.STATE_EXPANDED) {
                    bottomlin.setVisibility(View.GONE);
                } else {
                    bottomlin.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        /////////////// Banner Ads /////////////////

        mAdView = findViewById(R.id.adView2);
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


        /// Setting required data to the Screen
        it = getIntent();
        child = it.getStringExtra("node");
        tag = it.getStringExtra("tag");
        popularity = it.getIntExtra("popularity",1);
        downloaded = it.getIntExtra("downloaded",1);
        Source = it.getStringExtra("source");


        Glide.with(this).load(it.getStringExtra("image")).into(fullimage);
        fullimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hidden) {
                    bottomSheetBehavior.setPeekHeight(400,true);
                    bottomSheetBehavior.setHideable(false);
                    hidden = true;
                } else {
                    bottomSheetBehavior.setPeekHeight(0,true);
                    hidden = false;
                }
            }
        });

        // Fetch Firebase data for bottomsheet
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("image-data").orderByChild("tag").equalTo(tag), Model.class)
                        .build();

        /// Making GridLayout for bottomsheet
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        rcv.setLayoutManager(gridLayoutManager);
        adapter = new MyAdapter(options, ViewImageActivity.this);
        rcv.setAdapter(adapter);


            /// Getting runtime Permissions for storage
            Dexter.withContext(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();




//        loadRewardedAd(this);
        /// Setting Wallpaper when clicked on download button
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        setwallpaper.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(ViewImageActivity.this, 0);
                dialog.setTitle("Setting Wallpaper");
                dialog.setMessage("please Wait while Setting Up");
                dialog.show();
                issetwallpaper = true;
                try {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("image-data")
                            .child(child)
                            .child("popularity")
                            .setValue(popularity+1);

                    // set the wallpaper by calling the setResource function and
                    // passing the drawable file
                    imageName = generateRandomString();
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                    boolean onlywifi = sp.getBoolean("over_wifi",false);

                    if (!onlywifi){
                        if (SDK_INT >= Build.VERSION_CODES.Q){
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(it.getStringExtra("image")))
                                    .setTitle("Image Downloading")
                                    .setDescription("The Image is downloading")
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI)
                                    .setDestinationUri(Uri.fromFile(new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/setHDWallpaper/",imageName+".jpeg")));
                            downloadID = downloadManager.enqueue(request);
                            Toast.makeText(ViewImageActivity.this, "File downloading Started.", Toast.LENGTH_SHORT).show();

                        }else{
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(it.getStringExtra("image")))
                                    .setTitle("Image Downloading")
                                    .setDescription("The Image is downloading")
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI)
                                    .setDestinationInExternalPublicDir("/.HDWallpaper",imageName+".jpeg");
                            downloadID = downloadManager.enqueue(request);
                            Toast.makeText(ViewImageActivity.this, "File downloading Started.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        for (Network network : connMgr.getAllNetworks()) {
                            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                                if (SDK_INT >= Build.VERSION_CODES.Q){
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(it.getStringExtra("image")))
                                            .setTitle("Image Downloading")
                                            .setDescription("The Image is downloading")
                                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                                            .setDestinationUri(Uri.fromFile(new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/setHDWallpaper/",imageName+".jpeg")));
                                    downloadID = downloadManager.enqueue(request);
                                    Toast.makeText(ViewImageActivity.this, "File downloading Started.", Toast.LENGTH_SHORT).show();
                                    break;
                                }else{
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(it.getStringExtra("image")))
                                            .setTitle("Image Downloading")
                                            .setDescription("The Image is downloading")
                                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                                            .setDestinationInExternalPublicDir("/.HDWallpaper",imageName+".jpeg");
                                    downloadID = downloadManager.enqueue(request);
                                    Toast.makeText(ViewImageActivity.this, "File downloading Started.", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                dialog.dismiss();
                                Toast.makeText(ViewImageActivity.this, "Could not Download, WiFi is not Connected", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                    }
                    
                } catch (Exception e) {
                    // here the errors can be logged instead of printStackTrace
                    e.printStackTrace();
                }
            }
        });

        /// handle when clicked on info button
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog d = new Dialog(ViewImageActivity.this);
                d.setContentView(R.layout.custom_info_dialog);
                d.getWindow().setLayout(Toolbar.LayoutParams.WRAP_CONTENT,Toolbar.LayoutParams.WRAP_CONTENT);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView download = d.findViewById(R.id.alldownloads);
                TextView source = d.findViewById(R.id.mainsource);
                Button close = d.findViewById(R.id.closebtn);
                download.setText(String.valueOf(downloaded));
                source.setText(Source);

                ((ViewGroup)d.getWindow().getDecorView())
                        .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(ViewImageActivity.this,android.R.anim.fade_in));
                d.show();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
            }
        });

        /// Download File when clicked on download button
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ////////// Load rewarded Ad ////////////
                rewardedAd.load(ViewImageActivity.this, rewardedAdId, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        rewardedAd.show(ViewImageActivity.this, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        loadRewardedAd(ViewImageActivity.this);
                    }
                });
                ////////// Load rewarded Ad ////////////

                imageName = generateRandomString();

                /// Checking if permissions are granted
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    // When All permissions are Granted
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("image-data")
                            .child(child)
                            .child("downloaded")
                            .setValue(downloaded+1);

                    boolean onlywifi = sp.getBoolean("over_wifi",false);
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                    if (!onlywifi) {

                        if (SDK_INT >= Build.VERSION_CODES.Q){
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(it.getStringExtra("image")))
                                    .setTitle("Image Downloading")
                                    .setDescription("The Image is downloading")
                                    .setAllowedOverRoaming(false)
                                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI)
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                    .setDestinationUri(Uri.fromFile(new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/HDWallpaper/",imageName + ".jpeg")));

                            downloadID = downloadManager.enqueue(request);
                            Toast.makeText(ViewImageActivity.this, "File downloading Started.", Toast.LENGTH_SHORT).show();
                        }else{
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(it.getStringExtra("image")))
                                    .setTitle("Image Downloading")
                                    .setDescription("The Image is downloading")
                                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI)
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                    .setDestinationInExternalPublicDir("/HDWallpaper", generateRandomString() + ".jpeg");

                            downloadID = downloadManager.enqueue(request);
                            Toast.makeText(ViewImageActivity.this, "File downloading Started.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        ConnectivityManager connMgr2 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        for (Network network : connMgr2.getAllNetworks()) {
                            NetworkInfo networkInfo2 = connMgr2.getNetworkInfo(network);
                            if (networkInfo2.getType() == ConnectivityManager.TYPE_WIFI) {

                                if (SDK_INT >= Build.VERSION_CODES.Q){
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(it.getStringExtra("image")))
                                            .setTitle("Image Downloading")
                                            .setDescription("The Image is downloading")
                                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                            .setDestinationUri(Uri.fromFile(new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/HDWallpaper/",imageName + ".jpeg")));
                                    downloadID = downloadManager.enqueue(request);
                                    Toast.makeText(ViewImageActivity.this, "File downloading Started.", Toast.LENGTH_SHORT).show();
                                    break;
                                }else{
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(it.getStringExtra("image")))
                                            .setTitle("Image Downloading")
                                            .setDescription("The Image is downloading")
                                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                            .setDestinationInExternalPublicDir("/HDWallpaper", generateRandomString() + ".jpeg");
                                    downloadID = downloadManager.enqueue(request);
                                    Toast.makeText(ViewImageActivity.this, "File downloading Started.", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            else if (networkInfo2.getType() == ConnectivityManager.TYPE_MOBILE) {
                                Toast.makeText(ViewImageActivity.this, "Could not Download, WiFi is not Connected", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                }else{
                    // When Permission is not granted
                    Toast.makeText(ViewImageActivity.this, "Don't have Storage Permission", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    


    /// Generate Random String for file name
    private String generateRandomString() {
        // Generateing Random String

        // create a string of uppercase and lowercase characters and numbers
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        // combine all strings
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
        // create random string builder
        StringBuilder sb = new StringBuilder();
        // create an object of Random class
        Random random = new Random();
        // specify length of random string
        int length = 10;
        for(int i = 0; i < length; i++) {
            // generate random index number
            int index = random.nextInt(alphaNumeric.length());
            // get character specified by index
            // from the string
            char randomChar = alphaNumeric.charAt(index);
            // append the character to string builder
            sb.append(randomChar);
        }
        return sb.toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}