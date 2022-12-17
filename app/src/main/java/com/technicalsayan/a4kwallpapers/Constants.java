package com.technicalsayan.a4kwallpapers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


public class Constants {

    public static RewardedAd rewardedAd;
    public static void loadRewardedAd(Context context){
        rewardedAd.load(context, "ca-app-pub-5615101169407494/2217708224", new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                loadRewardedAd(context);
            }
        });
    }
}
