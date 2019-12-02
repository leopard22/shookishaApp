package com.example.shookisha.ui.main;

import android.content.Context;
import android.location.Location;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shookisha.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_offre, R.string.tab_panier, R.string.tab_achat, R.string.tab_profil, R.string.tab_infos};

    private final Context mContext;
    private String data;
    private Location location;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String data) {
        super(fm);
        mContext = context;
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment = OffreFragment.newOffreFragment(data);
                break;

            case 1:
                fragment = PanierFragment.newPanierFragment(data);
                break;
            case 2:
                fragment = new AchatFragment();
                break;
            case 3:
                fragment = ProfilFragment.newInstance(data);
                break;
            case 4:
                fragment =  InfoFragment.newInstance(data);
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
        return TAB_TITLES.length;
    }
}