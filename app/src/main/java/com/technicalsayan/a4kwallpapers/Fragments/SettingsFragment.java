package com.technicalsayan.a4kwallpapers.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.technicalsayan.a4kwallpapers.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

}
