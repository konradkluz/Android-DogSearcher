package com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
public class TabDogOfTheDayFragmentModule {

    @Provides
    TabDogOfTheDayView provideTabDogOfTheDayView(TabDogOfTheDayFragment tabDogOfTheDayFragment) {
        return tabDogOfTheDayFragment;
    }
}
