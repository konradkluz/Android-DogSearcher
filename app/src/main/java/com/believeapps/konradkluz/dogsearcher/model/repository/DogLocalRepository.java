package com.believeapps.konradkluz.dogsearcher.model.repository;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by konradkluz on 02/10/2017.
 */

public interface DogLocalRepository {
    Disposable getFavouriteDogs(Consumer<List<BreedWithSubBreeds>> onNext,
                                                        Consumer<Throwable> onError);
    Disposable getAllFavourites(List<BreedWithSubBreeds> breedWithSubBreeds,
                          Consumer<List<BreedWithSubBreeds>> onNext,
                          Consumer<Throwable> onError);
    Disposable insertFavouriteDog(BreedWithSubBreeds favouriteDog);

    Disposable deleteDogFromFavourites(BreedWithSubBreeds favouriteDog);
}
