package com.believeapps.konradkluz.dogsearcher.model.repository;

import com.believeapps.konradkluz.dogsearcher.model.entities.FavouriteDog;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by konradkluz on 02/10/2017.
 */

public interface DogLocalRepository {
    Flowable<List<FavouriteDog>> getAllFavourites();
    void insertFavouriteDog(FavouriteDog favouriteDog);
}
