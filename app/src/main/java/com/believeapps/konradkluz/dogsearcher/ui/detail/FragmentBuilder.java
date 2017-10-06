package com.believeapps.konradkluz.dogsearcher.ui.detail;

import android.support.v4.app.Fragment;

import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragment;
import com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd.TabDogOfTheDayFragmentComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * Created by konradkluz on 30/09/2017.
 */
@Module
public abstract class FragmentBuilder {

    @Binds
    @IntoMap
    @FragmentKey(TabDogOfTheDayFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindTabDogOfTheDayFragment(TabDogOfTheDayFragmentComponent.Builder builder);

}
