package com.technicalsayan.a4kwallpapers;

import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.technicalsayan.a4kwallpapers.Fragments.AboutAppFragment;
import com.technicalsayan.a4kwallpapers.Fragments.AboutMeFragment;
import com.technicalsayan.a4kwallpapers.Fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    int viewFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        viewFrag = getIntent().getIntExtra("diff",0);

        if (viewFrag == 0){
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();
            }
        }else if (viewFrag == 1){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings,new AboutMeFragment())
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings,new AboutAppFragment())
                    .commit();
        }
        getSupportActionBar().hide();
    }


}