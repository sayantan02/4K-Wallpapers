package com.technicalsayan.a4kwallpapers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.technicalsayan.a4kwallpapers.Models.Model;
import com.technicalsayan.a4kwallpapers.R;
import com.technicalsayan.a4kwallpapers.ViewImageActivity;


public class MyAdapter extends FirebaseRecyclerAdapter<Model,MyAdapter.ViewHolder> {

    Context context;
    Intent it;
    private InterstitialAd mInterstitialAd;

    public MyAdapter(@NonNull FirebaseRecyclerOptions<Model> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull Model model) {
        Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);
        holder.imageurl.setText(model.getImage());
        holder.tag.setText(model.getTag());
        holder.downloaded.setText(String.valueOf(model.getDownloaded()));
        holder.key.setText(model.getKey());
        holder.popularity.setText(String.valueOf(model.getPopularity()));
        holder.source.setText(model.getSource());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                it = new Intent(context, ViewImageActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("image",holder.imageurl.getText());
                it.putExtra("node", holder.key.getText());
                it.putExtra("tag",holder.tag.getText());
                it.putExtra("downloaded",Integer.parseInt((String) holder.downloaded.getText()));
                it.putExtra("popularity",Integer.parseInt((String) holder.popularity.getText()));
                it.putExtra("source",holder.source.getText());
                context.startActivity(it);
                                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context,"ca-app-pub-5615101169407494/3130855713", adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                        if (mInterstitialAd != null) {
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    Log.i("TAG", "The ad was dismissed.");
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    InterstitialAd.load(context, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
                                        @Override
                                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                            super.onAdLoaded(interstitialAd);
                                        }
                                    });
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    mInterstitialAd = null;
                                    Log.i("TAG", "The ad was shown.");
                                }
                            });
                            mInterstitialAd.show((Activity) context);
                        } else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.");
                        }

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
            }
        });



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        return new ViewHolder(view);
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView imageurl,downloaded,key,tag,popularity,source;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tag = itemView.findViewById(R.id.tag);
            imageurl = itemView.findViewById(R.id.imageurl);
            downloaded = itemView.findViewById(R.id.downloaded);
            key = itemView.findViewById(R.id.getkey);
            popularity = itemView.findViewById(R.id.popularity);
            source = itemView.findViewById(R.id.source);
        }
    }
}
