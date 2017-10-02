package com.believeapps.konradkluz.dogsearcher.ui.main;

import android.arch.lifecycle.ViewModel;

import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragmentComponent;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.TabAllFragmentComponent;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.TabFavouritesFragmentComponent;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.LayoutInflationStrategy;
import com.believeapps.konradkluz.dogsearcher.ui.main.inflater.impl.PortraitLayoutInflationStrategy;
import com.believeapps.konradkluz.dogsearcher.viewmodel.AllDogsFragmentViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module(subcomponents = {
        TabAllFragmentComponent.class,
        TabDogOfTheDayFragmentComponent.class,
        TabFavouritesFragmentComponent.class
})
public class MainActivityModule {

    @Provides
    MainView provideMainView(MainActivity mainActivity) {
        return mainActivity;
    }

    @Provides
    LayoutInflationStrategy provideLayoutInflationStrategy(MainActivity activity) {
        return new PortraitLayoutInflationStrategy(activity);
    }

    @Provides
    ViewModel provideDogListViewModel(AllDogsFragmentViewModel allDogsFragmentViewModel) {
        return allDogsFragmentViewModel;
    }

}
