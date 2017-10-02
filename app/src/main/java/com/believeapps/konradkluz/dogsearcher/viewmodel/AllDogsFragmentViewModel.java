package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.believeapps.konradkluz.dogsearcher.model.entities.FavouriteDog;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogRemoteRepository;

import javax.inject.Inject;

/**
 * Created by konradkluz on 30/09/2017.
 */

public class AllDogsFragmentViewModel extends ViewModel {

    private final MediatorLiveData<Response> mApiResponse;

    private DogRemoteRepository mDogRemoteRepository;
    private DogLocalRepository mDogLocalRepository;
    private boolean requestSent = false;

    @Inject
    public AllDogsFragmentViewModel(DogRemoteRepository remoteRepository,
                                    DogLocalRepository localRepository) {
        mApiResponse = new MediatorLiveData<>();
        mDogRemoteRepository = remoteRepository;
        mDogLocalRepository = localRepository;
    }

    @NonNull
    public MediatorLiveData<Response> getApiResponse() {
        return mApiResponse;
    }

    public LiveData<Response> loadDogs() {
        mApiResponse.addSource(
                mDogRemoteRepository.getAllDogs(),
                response -> mApiResponse.setValue(response));
        requestSent = true;
        return mApiResponse;
    }

    public void persistFavouriteDog(FavouriteDog favouriteDog) {
        mDogLocalRepository.insertFavouriteDog(favouriteDog);
    }

    public boolean isRequestSent() {
        return requestSent;
    }
}
