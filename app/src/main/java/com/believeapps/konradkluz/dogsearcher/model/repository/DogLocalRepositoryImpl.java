package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.db.FavouriteDogsDao;
import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.SubBreed;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by konradkluz on 02/10/2017.
 */

public class DogLocalRepositoryImpl implements DogLocalRepository {

    private static final String TAG = "DogLocalRepositoryImpl";

    @Inject
    FavouriteDogsDao mFavouriteDogsDao;

    @Inject
    public DogLocalRepositoryImpl() {
    }

    @Override
    public Flowable<List<BreedWithSubBreeds>> getAllFavourites() {
        return mFavouriteDogsDao.getAllFavouriteDogs();
    }

    @Override
    public void insertFavouriteDog(BreedWithSubBreeds favouriteDog) {
        Observable.fromCallable(() -> mFavouriteDogsDao.insert(favouriteDog.breed))
                .flatMap(breedId -> {
                    for (SubBreed subBreed : favouriteDog.subBreeds) {
                        subBreed.setBreedId(breedId);
                    }
                    if (favouriteDog.subBreeds != null && !favouriteDog.subBreeds.isEmpty()) {
                        return Observable.fromCallable(() -> mFavouriteDogsDao.insertAllSubBreeds(favouriteDog.subBreeds));
                    }
                    return Observable.empty();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        id -> Log.d(TAG, "persistFavouriteDog: favourite inserted: " + id),
                        error -> Log.e(TAG, "persistFavouriteDog: error occurred: ", error),
                        () -> Log.d(TAG, "persistFavouriteDog: breed persisted: " + favouriteDog.breed)
                );
    }

    @Override
    public void deleteDogFromFavourites(BreedWithSubBreeds favouriteDog) {
        Observable.fromCallable(() -> mFavouriteDogsDao.delete(favouriteDog.breed))
                .flatMap(numberOfRows -> {
                    if (favouriteDog.subBreeds != null && !favouriteDog.subBreeds.isEmpty()) {
                        return Observable.fromCallable(() -> mFavouriteDogsDao.deleteAllSubBreeds(favouriteDog.subBreeds));
                    }
                    return Observable.empty();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        numberOfRows -> Log.d(TAG, "deleted: " + numberOfRows + "sub breeds"),
                        error -> Log.e(TAG, "deleting sub breeds: error occurred: ", error),
                        () -> {
                            Log.d(TAG, "deleteDogFromFavourites: breed deleted: " + favouriteDog.breed);
                            favouriteDog.breed.setId(null);
                        }
                );
    }
}
