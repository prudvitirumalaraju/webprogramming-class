package com.example.android.newsapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.newsapp.R;
import com.example.android.newsapp.activities.SportFragment;
import com.example.android.newsapp.activities.TechFragment;
import com.example.android.newsapp.activities.USFragment;
import com.example.android.newsapp.activities.WorldFragment;

/**
 * Created by jennifernghinguyen on 1/17/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    String[] pageTitle;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        pageTitle = context.getResources().getStringArray(R.array.sections);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new USFragment();
        } else if (position == 1) {
            return new WorldFragment();
        } else if (position == 2) {
            return new TechFragment();
        } else {
            return new SportFragment();
        }
    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return pageTitle[0];
        } else if (position == 1) {
            return pageTitle[1];
        } else if (position == 2) {
            return pageTitle[2];
        } else {
            return pageTitle[3];
        }

    }


}
