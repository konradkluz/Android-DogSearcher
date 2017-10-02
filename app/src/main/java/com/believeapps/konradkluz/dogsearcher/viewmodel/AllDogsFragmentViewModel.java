package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.believeapps.konradkluz.dogsearcher.model.entities.Response;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogRepository;

import javax.inject.Inject;

/**
 * Created by konradkluz on 30/09/2017.
 */

public class AllDogsFragmentViewModel extends ViewModel {

    private MediatorLiveData<Response> mApiResponse;
    private DogRepository mDogRepository;
    private boolean requestSent = false;

    @Inject
    public AllDogsFragmentViewModel(DogRepository repository) {
        mApiResponse = new MediatorLiveData<>();
        mDogRepository = repository;
    }

    @NonNull
    public MediatorLiveData<Response> getApiResponse() {
        return mApiResponse;
    }

    public LiveData<Response> loadDogs() {
        mApiResponse.addSource(
                mDogRepository.getAllDogs(),
                response -> mApiResponse.setValue(response));
        requestSent = true;
        return mApiResponse;
    }

    public boolean isRequestSent() {
        return requestSent;
    }
}
