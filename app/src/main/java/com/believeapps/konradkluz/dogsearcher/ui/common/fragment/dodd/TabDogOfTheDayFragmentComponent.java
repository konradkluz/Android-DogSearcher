package com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Subcomponent(modules = TabDogOfTheDayFragmentModule.class)
public interface TabDogOfTheDayFragmentComponent extends AndroidInjector<TabDogOfTheDayFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TabDogOfTheDayFragment>{}
}
