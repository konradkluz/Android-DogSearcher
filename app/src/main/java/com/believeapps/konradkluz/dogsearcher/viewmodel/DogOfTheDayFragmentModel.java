package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogRemoteRepository;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by konradkluz on 05/10/2017.
 */

public class DogOfTheDayFragmentModel extends ViewModel {

    private static final String TAG = "DogOfTheDayFragmentMode";
    private final MediatorLiveData<Response> mDbResponse;
    private CompositeDisposable compositeDisposable;
    private DogLocalRepository mDogLocalRepository;
    private DogRemoteRepository mDogRemoteRepository;
    private boolean requestSent = false;
    private boolean newDogOfTheDayLoaded = false;

    @Inject
    public DogOfTheDayFragmentModel(DogLocalRepository dogLocalRepository,
                                    DogRemoteRepository dogRemoteRepository) {
        compositeDisposable = new CompositeDisposable();
        mDogLocalRepository = dogLocalRepository;
        mDogRemoteRepository = dogRemoteRepository;
        mDbResponse = new MediatorLiveData<>();
    }

    @NonNull
    public MediatorLiveData<Response> getDbResponse() {
        return mDbResponse;
    }


    public LiveData<Response> loadOrDrawDogOfTheDay() {
        MutableLiveData<Response> liveData = new MutableLiveData<>();
        compositeDisposable.add(
                mDogLocalRepository.getDogOfTheDay()
                        .flatMap(dogOfTheDay -> {
                            if (isTimeToUpdate(dogOfTheDay.getExpirationDateMillis())) {
                                mDogLocalRepository.deleteDogOfTheDay(dogOfTheDay);
                                return Maybe.empty();
                            }
                            return Maybe.fromCallable(() -> dogOfTheDay);
                        })
                        .switchIfEmpty(mDogRemoteRepository
                                .loadRandomBreed()
                                .doAfterSuccess(dogOfTheDay -> {
                                    mDogLocalRepository.insertDogOfTheDay(dogOfTheDay);
                                    newDogOfTheDayLoaded = true;
                                }))
                        .flatMap(dogOfTheDay -> {
                            Log.d(TAG, "loadOrDrawDogOfTheDay: find favourites by name");
                            return mDogLocalRepository.findFavouriteByName(dogOfTheDay.getName())
                                    .switchIfEmpty(Maybe.fromCallable(() -> dogOfTheDay));
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                dogOfTheDay -> liveData.setValue(Response.success(dogOfTheDay)),
                                error -> liveData.setValue(Response.error(error)),
                                () -> Log.d(TAG, "loadOrDrawDogOfTheDay: completed"))
        );
        mDbResponse.addSource(liveData, mDbResponse::setValue);
        return mDbResponse;
    }

    public void deleteDogFromFavouritesByName(String breedName) {
        Log.d(TAG, "deleteDogFromFavourites: deleting from favourites: " + breedName);
        mDogLocalRepository.deleteDogFromFavouritesByName(breedName);
        Log.d(TAG, "deleteDogFromFavourites: dog deleted: " + breedName);
    }

    public void persistFavouriteDog(BreedWithSubBreeds favouriteDog) {
        Log.d(TAG, "persistFavouriteDog: persisting favourite dog: " + favouriteDog);
        mDogLocalRepository.insertFavouriteDog(favouriteDog);
        Log.d(TAG, "persistFavouriteDog: dog persisted: " + favouriteDog);
    }

    public boolean isRequestSent() {
        return requestSent;
    }

    public boolean isNewDogOfTheDayLoaded() {
        return newDogOfTheDayLoaded;
    }

    public void setNewDogOfTheDayLoaded(boolean newDogOfTheDayLoaded) {
        this.newDogOfTheDayLoaded = newDogOfTheDayLoaded;
    }

    private boolean isTimeToUpdate(Long expirationDate) {
        Calendar timeOfUpdate = Calendar.getInstance();
        timeOfUpdate.setTimeInMillis(expirationDate);

        Calendar current = Calendar.getInstance();
        return current.after(timeOfUpdate);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared: called");
        compositeDisposable.dispose();
    }
}
