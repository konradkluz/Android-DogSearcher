package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites;

import com.believeapps.konradkluz.dogsearcher.ui.main.MainActivity;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter.FavouritesRecyclerViewAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
public class TabFavouritesFragmentModule {

    @Provides
    TabFavouritesView provideTabFavouritesView(TabFavouritesFragment tabFavouritesFragment) {
        return tabFavouritesFragment;
    }

    @Provides
    FavouritesRecyclerViewAdapter providesAdapter(MainActivity mainActivity) {
        return new FavouritesRecyclerViewAdapter(mainActivity);
    }
}
