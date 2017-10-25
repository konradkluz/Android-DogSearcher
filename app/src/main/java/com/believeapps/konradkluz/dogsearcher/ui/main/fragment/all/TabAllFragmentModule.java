package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all;

import android.support.v7.widget.RecyclerView;

import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter.AllDogsRecyclerViewAdapter;
import com.believeapps.konradkluz.dogsearcher.ui.main.MainActivity;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter.DogViewHolder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
public class TabAllFragmentModule {

    @Provides
    RecyclerView.Adapter<DogViewHolder> providesAdapter(AllDogsRecyclerViewAdapter viewAdapter) {
        return viewAdapter;
    }
}
