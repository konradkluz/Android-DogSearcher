package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;

/**
 * Created by konradkluz on 01/10/2017.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private AllDogsFragmentViewModel mAllDogsFragmentViewModel;

    private FavouritesViewModel mFavouritesViewModel;

    @Inject
    public ViewModelFactory(AllDogsFragmentViewModel allDogsFragmentViewModel,
                            FavouritesViewModel favouritesViewModel) {
        mAllDogsFragmentViewModel = allDogsFragmentViewModel;
        mFavouritesViewModel = favouritesViewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AllDogsFragmentViewModel.class)) {
            return (T) mAllDogsFragmentViewModel;
        } else if (modelClass.isAssignableFrom(FavouritesViewModel.class)) {
            return (T) mFavouritesViewModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}