package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.DogOfTheDay;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogRemoteRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Flowable;
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
                        .switchIfEmpty(mDogRemoteRepository
                                .loadRandomBreed()
                                .doAfterSuccess(dogOfTheDay -> mDogLocalRepository.insertDogOfTheDay(dogOfTheDay)))
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

    public boolean isRequestSent() {
        return requestSent;
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared: called");
        compositeDisposable.dispose();
    }
}
