package com.believeapps.konradkluz.dogsearcher.ui.detail;


import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Subcomponent(modules = {DogDetailActivityModule.class, FragmentBuilder.class})
public interface DogDetailActivityComponent extends AndroidInjector<DogDetailActivity>{

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DogDetailActivity>{}
}
