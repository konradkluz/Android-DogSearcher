package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Subcomponent(modules = TabFavouritesFragmentModule.class)
public interface TabFavouritesFragmentComponent extends AndroidInjector<TabFavouritesFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TabFavouritesFragment>{}
}
