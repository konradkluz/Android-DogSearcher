package com.believeapps.konradkluz.dogsearcher.ui.main.inflater.impl;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.entities.DogOfTheDay;
import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.MainActivity;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.TabAllFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.TabFavouritesFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.LayoutInflationStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class PortraitLayoutInflationStrategy implements LayoutInflationStrategy{

    private static final String TAG = "PortraitLayoutInflation";

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.portrait_main_content)
    CoordinatorLayout coordinatorLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabAllFragment mTabAllFragment;
    private TabFavouritesFragment mTabFavouritesFragment;
    private TabDogOfTheDayFragment mTabDogOfTheDayFragment;
    private MainActivity mActivity;

    public PortraitLayoutInflationStrategy(MainActivity activity) {
        mTabAllFragment = new TabAllFragment();
        mTabFavouritesFragment = new TabFavouritesFragment();
        mTabDogOfTheDayFragment = new TabDogOfTheDayFragment();
        mActivity = activity;
        ButterKnife.bind(this, mActivity);
    }

    @Override
    public void inflate() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(mActivity.getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void dogOfTheDayChanged(DogOfTheDay dogOfTheDay) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "New Dog of the day loaded.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mTabAllFragment;
                case 1:
                    return mTabDogOfTheDayFragment;
                case 2:
                    return mTabFavouritesFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
