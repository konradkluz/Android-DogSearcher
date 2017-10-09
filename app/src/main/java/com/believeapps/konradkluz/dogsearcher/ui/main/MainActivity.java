package com.believeapps.konradkluz.dogsearcher.ui.main;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.entities.DogOfTheDay;
import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.DogOfTheDayChangedListener;
import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.TabAllFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.TabFavouritesFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.LayoutInflationStrategy;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.impl.LargeLandscapeLayoutInflationStrategy;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.impl.PortraitLayoutInflationStrategy;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


//TODO Bugs (TODOs)
//TODO Scheduled Job and notification
//TODO Remove favourite dialog
public class MainActivity extends AppCompatActivity implements MainView, DogOfTheDayChangedListener, HasSupportFragmentInjector {

    private static final String TAG = "MainActivity";

    LayoutInflationStrategy mLayoutInflationStrategy;

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    @Inject
    TabAllFragment mTabAllFragment;

    @Inject
    TabFavouritesFragment mTabFavouritesFragment;

    @Inject
    TabDogOfTheDayFragment mTabDogOfTheDayFragment;

    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
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
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }

    @Override
    public void dogOfTheDayChanged(DogOfTheDay dogOfTheDay) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "New Dog of the day loaded.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
