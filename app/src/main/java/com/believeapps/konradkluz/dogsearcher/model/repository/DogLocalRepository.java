package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.arch.lifecycle.LiveData;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.DogOfTheDay;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.maybe.MaybeToFlowable;

/**
 * Created by konradkluz on 02/10/2017.
 */

public interface DogLocalRepository {
    LiveData<Response> getFavouriteDogs();
    Flowable<List<BreedWithSubBreeds>>  getAllFavourites(List<BreedWithSubBreeds> breedWithSubBreeds);

    Maybe<DogOfTheDay> getDogOfTheDay();
    void insertFavouriteDog(BreedWithSubBreeds favouriteDog);

    void insertDogOfTheDay(DogOfTheDay dogOfTheDay);

    void deleteDogOfTheDay(DogOfTheDay dogOfTheDay);

    void deleteDogFromFavourites(BreedWithSubBreeds favouriteDog);

    Maybe<DogOfTheDay> findFavouriteByName(String name);

    void deleteDogFromFavouritesByName(String breedName);
}
