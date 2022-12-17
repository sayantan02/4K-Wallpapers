package com.technicalsayan.a4kwallpapers.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.technicalsayan.a4kwallpapers.Fragments.ForYouTabFragment;
import com.technicalsayan.a4kwallpapers.Fragments.HomeTabFragment;
import com.technicalsayan.a4kwallpapers.Fragments.MostDownloadTabFragment;
import com.technicalsayan.a4kwallpapers.Fragments.PopularTabFragment;
import com.technicalsayan.a4kwallpapers.Fragments.ProPicsTabFragment;
import com.technicalsayan.a4kwallpapers.R;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3,R.string.tab_text_4,R.string.tab_text_5};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position)
        {

            case 0:
                fragment = new HomeTabFragment();
                break;

            case 1:
                fragment = new ForYouTabFragment();
                break;

            case 2:
                fragment = new ProPicsTabFragment();
                break;

            case 3:
                fragment = new PopularTabFragment();
                break;

            case 4:
                fragment = new MostDownloadTabFragment();
                break;

        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 5 total pages.
        return 5;
    }
}