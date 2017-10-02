package com.believeapps.konradkluz.dogsearcher.ui.detail;

//import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragmentComponent;

import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragmentComponent;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
        (subcomponents = {TabDogOfTheDayFragmentComponent.class})
public class DogDetailActivityModule {


    @Provides
    DogDetailView provideDogDetailView(DogDetailActivity dogDetailActivity) {
        return dogDetailActivity;
    }

}
