package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Subcomponent(modules = TabAllFragmentModule.class)
public interface TabAllFragmentComponent  extends AndroidInjector<TabAllFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TabAllFragment>{}

}
