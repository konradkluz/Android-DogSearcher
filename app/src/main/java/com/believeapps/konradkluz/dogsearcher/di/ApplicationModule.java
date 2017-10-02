package com.believeapps.konradkluz.dogsearcher.di;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;


import com.believeapps.konradkluz.dogsearcher.ui.detail.DogDetailActivityComponent;
import com.believeapps.konradkluz.dogsearcher.ui.main.MainActivityComponent;
import com.believeapps.konradkluz.dogsearcher.viewmodel.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module(subcomponents = {
        MainActivityComponent.class,
        DogDetailActivityComponent.class})
public class ApplicationModule {

    @Provides
    ViewModelProvider.Factory provideDogListViewModelFactory(ViewModelFactory factory) {
        return factory;
    }

    @Provides
    @Singleton
    public Context provideContext(Application application) {
        return application;
    }
}
