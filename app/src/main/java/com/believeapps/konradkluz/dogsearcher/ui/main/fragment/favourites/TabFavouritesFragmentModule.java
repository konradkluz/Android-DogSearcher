package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites;

import android.support.v7.widget.RecyclerView;

import com.believeapps.konradkluz.dogsearcher.ui.main.MainActivity;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter.DogFavouritesViewHolder;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter.FavouritesRecyclerViewAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
public class TabFavouritesFragmentModule {

    @Provides
    RecyclerView.Adapter<DogFavouritesViewHolder> providesAdapter(FavouritesRecyclerViewAdapter adapter) {
        return adapter;
    }
}
