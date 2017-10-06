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

    private DogOfTheDayFragmentModel mDogOfTheDayFragmentModel;

    private DogDetailActivityModel mDogDetailActivityModel;

    @Inject
    public ViewModelFactory(AllDogsFragmentViewModel allDogsFragmentViewModel,
                            FavouritesViewModel favouritesViewModel,
                            DogOfTheDayFragmentModel dogOfTheDayFragmentModel,
                            DogDetailActivityModel dogDetailActivityModel) {
        mAllDogsFragmentViewModel = allDogsFragmentViewModel;
        mFavouritesViewModel = favouritesViewModel;
        mDogOfTheDayFragmentModel = dogOfTheDayFragmentModel;
        mDogDetailActivityModel = dogDetailActivityModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AllDogsFragmentViewModel.class)) {
            return (T) mAllDogsFragmentViewModel;
        } else if (modelClass.isAssignableFrom(FavouritesViewModel.class)) {
            return (T) mFavouritesViewModel;
        }else if (modelClass.isAssignableFrom(DogOfTheDayFragmentModel.class)) {
            return (T) mDogOfTheDayFragmentModel;
        }else if (modelClass.isAssignableFrom(DogDetailActivityModel.class)) {
            return (T) mDogDetailActivityModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
