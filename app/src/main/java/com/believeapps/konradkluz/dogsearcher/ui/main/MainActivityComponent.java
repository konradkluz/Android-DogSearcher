package com.believeapps.konradkluz.dogsearcher.ui.main;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Subcomponent(modules = {MainActivityModule.class, FragmentBuilder.class})
public interface MainActivityComponent extends AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity>{}

}
