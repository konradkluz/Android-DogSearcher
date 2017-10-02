package com.believeapps.konradkluz.dogsearcher.di;

import android.app.Activity;

import com.believeapps.konradkluz.dogsearcher.ui.detail.DogDetailActivity;
import com.believeapps.konradkluz.dogsearcher.ui.detail.DogDetailActivityComponent;
import com.believeapps.konradkluz.dogsearcher.ui.main.MainActivity;
import com.believeapps.konradkluz.dogsearcher.ui.main.MainActivityComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
public abstract class ActivityBuilder {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(DogDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindDogDetailActivity(DogDetailActivityComponent.Builder builder);

//    @ContributesAndroidInjector(modules = MainActivityModule.class)
//    abstract MainActivity bindMainActivity();
//
//    @ContributesAndroidInjector(modules = DogDetailActivityModule.class)
//    abstract DogDetailActivity bindDogDetailActivity();
}
