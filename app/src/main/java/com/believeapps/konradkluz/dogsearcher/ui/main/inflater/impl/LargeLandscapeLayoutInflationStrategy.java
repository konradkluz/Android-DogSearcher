package com.believeapps.konradkluz.dogsearcher.ui.main.inflater.impl;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.ui.detail.DogDetailActivity;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.TabAllFragment;
import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.TabFavouritesFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.LayoutInflationStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


/**
 * Created by konradkluz on 28/09/2017.
 */

public class LargeLandscapeLayoutInflationStrategy implements LayoutInflationStrategy{

    private static final String TAG = "LargeLandscapeViewIniti";

    private AppCompatActivity mActivity;

    @BindView(R.id.landscape_tab_view)
    ListView mLandscapeTabView;

    public LargeLandscapeLayoutInflationStrategy(AppCompatActivity activity) {
        mActivity = activity;
        ButterKnife.bind(this, mActivity);
        ButterKnife.setDebug(true);
    }

    @Override
    public void inflate() {
        initializeLandscapeTabBar();
        initializeLandscapeContent(0);
    }

    //TODO think about it
    private void initializeLandscapeContent(int viewPosition) {
        Log.d(TAG, "initializeLandscapeContent: initializing landscape content");
        Fragment fragment;
        switch (viewPosition) {
            case 0:
                fragment = new TabAllFragment();
                break;
            case 1:
                fragment = new TabDogOfTheDayFragment();
                break;
            case 2:
                fragment = new TabFavouritesFragment();
                break;
            default:
                throw new IllegalArgumentException("View on position: " + viewPosition + "does not exist." );
        }
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.landscape_content_container, fragment);
        fragmentTransaction.commit();
    }

    private void initializeLandscapeTabBar() {
        Log.d(TAG, "initializeLandscapeTabBar: initializing landscape side tab bar");
        ArrayAdapter<String> stringArrayAdapter =
                new ArrayAdapter<>(
                        mActivity,
                        R.layout.landscape_tab_item,
                        R.id.tab_item,
                        new String[]{"ALL", "DOG OF THE DAY", "FAVOURITES"});
        mLandscapeTabView.setAdapter(stringArrayAdapter);
    }

    @OnItemClick(R.id.landscape_tab_view)
    void onListItemClicked(AdapterView<?> parent, int position) {
        Log.d(TAG, "onListItemClicked: clicked: " + position);
        initializeLandscapeContent(position);
    }
}
