package com.believeapps.konradkluz.dogsearcher.ui.main;

import android.support.v4.app.Fragment;

import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragmentComponent;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.TabAllFragment;
import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.TabAllFragmentComponent;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.TabFavouritesFragment;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.TabFavouritesFragmentComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
public abstract class FragmentBuilder {

    @Binds
    @IntoMap
    @FragmentKey(TabAllFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindTabAllFragment(TabAllFragmentComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(TabDogOfTheDayFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindTabDogOfTheDayFragment(TabDogOfTheDayFragmentComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(TabFavouritesFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindTabFavouritesFragment(TabFavouritesFragmentComponent.Builder builder);

//    @ContributesAndroidInjector(modules = TabAllFragmentModule.class)
//    abstract TabAllFragment bindTabAllFragment();
//
//    @ContributesAndroidInjector(modules = TabDogOfTheDayFragmentModule.class)
//    abstract TabDogOfTheDayFragment bindTabDogOfTheDayFragment();
//
//    @ContributesAndroidInjector(modules = TabFavouritesFragmentModule.class)
//    abstract TabFavouritesFragment bindTabFavouritesFragment();


}
