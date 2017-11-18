package com.example.android.newsapp.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.android.newsapp.R;
import com.example.android.newsapp.adapters.ViewPagerAdapter;


public class MainActivity extends AppCompatActivity {
    final static String LOG_TAG = MainActivity.class.getSimpleName();

    private ViewPagerAdapter adapter;


    static class ViewHolder {
        private TabLayout tabLayout;
        private ViewPager viewPager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewHolder.viewPager.setAdapter(adapter);
        viewHolder.viewPager.setOffscreenPageLimit(adapter.getCount());
        viewHolder.viewPager.setCurrentItem(0);

        viewHolder.tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewHolder.tabLayout.setupWithViewPager(viewHolder.viewPager);


    }


}
