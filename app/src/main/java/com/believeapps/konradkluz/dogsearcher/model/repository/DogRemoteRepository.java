package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.arch.lifecycle.LiveData;

import com.believeapps.konradkluz.dogsearcher.model.entities.Response;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by konradkluz on 01/10/2017.
 */

public interface DogRemoteRepository {
    LiveData<Response> getAllDogs();

    Disposable loadImageUrlByBreedName(String breedName,
                                       Consumer<String> onNext,
                                       Consumer<Throwable> onError);
}
