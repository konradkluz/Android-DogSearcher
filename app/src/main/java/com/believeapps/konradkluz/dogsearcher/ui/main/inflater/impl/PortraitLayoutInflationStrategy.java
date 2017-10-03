package com.believeapps.konradkluz.dogsearcher.ui.main.inflater.impl;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.TabAllFragment;
import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.TabFavouritesFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.LayoutInflationStrategy;

import butterknife.ButterKnife;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class PortraitLayoutInflationStrategy implements LayoutInflationStrategy{

    private static final String TAG = "PortraitLayoutInflation";
    private AppCompatActivity mActivity;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
//     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
//    @BindView(R.id.container)
    ViewPager mViewPager;

//    @BindView(R.id.tabs)
    TabLayout mTabLayout;

//    @BindView(R.id.fab)
    FloatingActionButton fab;

    public PortraitLayoutInflationStrategy(AppCompatActivity activity) {
        mActivity = activity;
        ButterKnife.bind(this, mActivity);
    }

    @Override
    public void inflate() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(mActivity.getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = mActivity.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout = mActivity.findViewById(R.id.tabs);
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab = mActivity.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TabAllFragment();
                case 1:
                    return new TabDogOfTheDayFragment();
                case 2:
                    return new TabFavouritesFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
