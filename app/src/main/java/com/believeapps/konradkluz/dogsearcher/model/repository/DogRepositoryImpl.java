package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.api.DogApiService;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by konradkluz on 01/10/2017.
 */

public class DogRepositoryImpl implements DogRepository {

    private static final String TAG = "DogRepositoryImpl";

    @Inject
    DogApiService mDogApiService;

    @Inject
    public DogRepositoryImpl() {
    }

    @Override
    public LiveData<Response> getAllDogs() {
        final MutableLiveData<Response> liveData = new MutableLiveData<>();
        Log.d(TAG, "getAllDogs: request sent");
        mDogApiService.getAllDogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> liveData.setValue(Response.success(response)),
                        error -> liveData.setValue(Response.error(error))
                );
        Log.d(TAG, "getAllDogs: request finished");
        return liveData;
    }
}
