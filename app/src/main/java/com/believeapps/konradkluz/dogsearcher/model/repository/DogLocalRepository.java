package com.believeapps.konradkluz.dogsearcher.model.repository;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by konradkluz on 02/10/2017.
 */

public interface DogLocalRepository {
    Flowable<List<BreedWithSubBreeds>> getFavouriteDogs();
    void getAllFavourites(List<BreedWithSubBreeds> breedWithSubBreeds,
                          Consumer<List<BreedWithSubBreeds>> onNext,
                          Consumer<Throwable> onError);
    void insertFavouriteDog(BreedWithSubBreeds favouriteDog);

    void deleteDogFromFavourites(BreedWithSubBreeds favouriteDog);
}
