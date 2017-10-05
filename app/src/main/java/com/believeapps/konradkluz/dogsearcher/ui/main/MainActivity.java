package com.believeapps.konradkluz.dogsearcher.ui.main;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.TabAllFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter.listener.AllDogsRecyclerItemClickListener;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.TabFavouritesFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.LayoutInflationStrategy;
import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.impl.LargeLandscapeLayoutInflationStrategy;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.impl.PortraitLayoutInflationStrategy;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements MainView, HasSupportFragmentInjector {

    private static final String TAG = "MainActivity";

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    LayoutInflationStrategy mLayoutInflationStrategy;

    @Inject
    TabAllFragment mTabAllFragment;

    @Inject
    TabFavouritesFragment mTabFavouritesFragment;

    @Inject
    TabDogOfTheDayFragment mTabDogOfTheDayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.landscape_tab_view) != null) {
            mLayoutInflationStrategy = new LargeLandscapeLayoutInflationStrategy(this,
                    mTabAllFragment,
                    mTabFavouritesFragment,
                    mTabDogOfTheDayFragment);
        } else {
            mLayoutInflationStrategy = new PortraitLayoutInflationStrategy(this,
                    mTabAllFragment,
                    mTabFavouritesFragment,
                    mTabDogOfTheDayFragment);
        }
        mLayoutInflationStrategy.inflate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }
}
