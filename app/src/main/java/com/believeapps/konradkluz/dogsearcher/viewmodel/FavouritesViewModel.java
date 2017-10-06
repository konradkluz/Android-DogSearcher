package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by konradkluz on 02/10/2017.
 */

public class FavouritesViewModel extends ViewModel {

    private static final String TAG = "FavouritesViewModel";

    private final MediatorLiveData<Response> mDbResponse;
    private DogLocalRepository mDogLocalRepository;
    private CompositeDisposable compositeDisposable;
    private boolean requestSent = false;

    @Inject
    public FavouritesViewModel(DogLocalRepository localRepository) {
        mDbResponse = new MediatorLiveData<>();
        compositeDisposable = new CompositeDisposable();
        mDogLocalRepository = localRepository;
    }

    @NonNull
    public MediatorLiveData<Response> getDbResponse() {
        return mDbResponse;
    }

    public LiveData<Response> getFavouriteDogs() {
        Log.d(TAG, "getFavouriteDogs: loading favourite dogs");

        mDbResponse.addSource(
                mDogLocalRepository.getFavouriteDogs(),
                mDbResponse::setValue);
        requestSent = true;
        return mDbResponse;
    }

    public void persistFavouriteDog(BreedWithSubBreeds favouriteDog) {
        Log.d(TAG, "persistFavouriteDog: persisting favourite dog: " + favouriteDog);
        mDogLocalRepository.insertFavouriteDog(favouriteDog);
        Log.d(TAG, "persistFavouriteDog: dog persisted: " + favouriteDog);
    }

    //TODO id deleted dog is dog of the day it does not change dog_of_the_day_table
    public void deleteDogFromFavourites(BreedWithSubBreeds favouriteDog) {
        Log.d(TAG, "deleteDogFromFavourites: deleting from favourites: " + favouriteDog);
        mDogLocalRepository.deleteDogFromFavourites(favouriteDog);
        Log.d(TAG, "deleteDogFromFavourites: dog deleted: " + favouriteDog);
    }

    @Override
    public void onCleared() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }

    public boolean isRequestSent() {
        return requestSent;
    }
}
