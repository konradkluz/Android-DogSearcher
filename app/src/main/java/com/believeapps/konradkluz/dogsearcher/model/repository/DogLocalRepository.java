package com.believeapps.konradkluz.dogsearcher.model.repository;

import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by konradkluz on 02/10/2017.
 */

public interface DogLocalRepository {
    Flowable<List<BreedWithSubBreeds>> getAllFavourites();
    void insertFavouriteDog(BreedWithSubBreeds favouriteDog);

    void deleteDogFromFavourites(BreedWithSubBreeds favouriteDog);
}
