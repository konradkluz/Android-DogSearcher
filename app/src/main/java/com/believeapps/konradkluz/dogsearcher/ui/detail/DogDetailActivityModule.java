package com.believeapps.konradkluz.dogsearcher.ui.detail;

import android.arch.lifecycle.ViewModel;

import com.believeapps.konradkluz.dogsearcher.viewmodel.DogDetailActivityModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
public class DogDetailActivityModule {

    @Provides
    DogDetailView provideDogDetailView(DogDetailActivity dogDetailActivity) {
        return dogDetailActivity;
    }

    @Provides
    ViewModel provideDogDetailViewModel(DogDetailActivityModel dogDetailActivityModel) {
        return dogDetailActivityModel;
    }
}
