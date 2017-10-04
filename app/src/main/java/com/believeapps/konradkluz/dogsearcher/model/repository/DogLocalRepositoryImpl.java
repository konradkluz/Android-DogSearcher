package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.db.FavouriteDogsDao;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.SubBreed;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
    public Flowable<List<BreedWithSubBreeds>> getFavouriteDogs() {
        return mFavouriteDogsDao.getAllFavouriteDogs();
    }

    @Override
    public void getAllFavourites(
            List<BreedWithSubBreeds> breedWithSubBreeds,
            Consumer<List<BreedWithSubBreeds>> onNext,
            Consumer<Throwable> onError) {
        Flowable<List<BreedWithSubBreeds>> allFavourites = mFavouriteDogsDao.getAllFavouriteDogs();

        Flowable<List<BreedWithSubBreeds>> listFlowable = Flowable
                .fromCallable(() -> breedWithSubBreeds);

        Flowable
                .zip(allFavourites, listFlowable,
                        (favourites, apiBreeds) -> {
                            for (BreedWithSubBreeds favourite : favourites) {
                                if (apiBreeds.contains(favourite)) {
                                    int index = apiBreeds.indexOf(favourite);
                                    apiBreeds.set(index, favourite);
                                }
                            }
                            return apiBreeds;
                        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    @Override
    public void insertFavouriteDog(BreedWithSubBreeds favouriteDog) {
        Flowable.fromCallable(() -> mFavouriteDogsDao.insert(favouriteDog.breed))
                .flatMap(breedId -> {
                    for (SubBreed subBreed : favouriteDog.subBreeds) {
                        subBreed.setBreedId(breedId);
                    }
                    if (favouriteDog.subBreeds != null && !favouriteDog.subBreeds.isEmpty()) {
                        return Flowable.fromCallable(() -> mFavouriteDogsDao.insertAllSubBreeds(favouriteDog.subBreeds));
                    }
                    return Flowable.empty();
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
        Flowable.fromCallable(() -> mFavouriteDogsDao.delete(favouriteDog.breed))
                .flatMap(numberOfRows -> {
                    if (favouriteDog.subBreeds != null && !favouriteDog.subBreeds.isEmpty()) {
                        return Flowable.fromCallable(() -> mFavouriteDogsDao.deleteAllSubBreeds(favouriteDog.subBreeds));
                    }
                    return Flowable.empty();
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
