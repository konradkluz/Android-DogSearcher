package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all;

import android.support.v7.widget.RecyclerView;

import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter.AllDogsRecyclerViewAdapter;
import com.believeapps.konradkluz.dogsearcher.ui.main.MainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
public class TabAllFragmentModule {

    @Provides
    TabAllView provideTabAllView(TabAllFragment tabAllFragment) {
        return tabAllFragment;
    }

    @Provides
    RecyclerView.Adapter<AllDogsRecyclerViewAdapter.DogViewHolder> providesAdapter(AllDogsRecyclerViewAdapter viewAdapter) {
        return viewAdapter;
    }
}
