package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;

import javax.inject.Inject;

/**
 * Created by konradkluz on 06/10/2017.
 */

public class DogDetailActivityModel extends ViewModel {

    private static final String TAG = "DogDetailActivityModel";

    private DogLocalRepository mDogLocalRepository;

    @Inject
    public DogDetailActivityModel(DogLocalRepository dogLocalRepository) {
        mDogLocalRepository = dogLocalRepository;
    }

    public void persistFavouriteDog(BreedWithSubBreeds favouriteDog) {
        Log.d(TAG, "persistFavouriteDog: persisting favourite dog: " + favouriteDog);
        mDogLocalRepository.insertFavouriteDog(favouriteDog);
        Log.d(TAG, "persistFavouriteDog: dog persisted: " + favouriteDog);
    }

    //TODO does not work when added to favourites in all view and removed in detail view
    public void deleteDogFromFavourites(BreedWithSubBreeds favouriteDog) {
        Log.d(TAG, "deleteDogFromFavourites: deleting from favourites: " + favouriteDog);
        mDogLocalRepository.deleteDogFromFavourites(favouriteDog);
        Log.d(TAG, "deleteDogFromFavourites: dog deleted: " + favouriteDog);
    }

}
