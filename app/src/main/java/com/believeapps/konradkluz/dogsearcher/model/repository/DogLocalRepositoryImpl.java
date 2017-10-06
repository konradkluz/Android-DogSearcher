package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.db.FavouriteDogsDao;
import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.DogOfTheDay;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;
import com.believeapps.konradkluz.dogsearcher.model.entities.SubBreed;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.maybe.MaybeToFlowable;
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
    public LiveData<Response> getFavouriteDogs() {
        Log.d(TAG, "getFavouriteDogs: getting favourite dogs from db");
        final MutableLiveData<Response> liveData = new MutableLiveData<>();
        mFavouriteDogsDao.getFavouriteDogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> liveData.setValue(Response.success(response)),
                        error -> liveData.setValue(Response.error(error))
                );
        return liveData;
    }

    @Override
    public Flowable<List<BreedWithSubBreeds>> getAllFavourites(
            List<BreedWithSubBreeds> breedWithSubBreeds) {
        Flowable<List<BreedWithSubBreeds>> allFavourites = mFavouriteDogsDao.getFavouriteDogs();

        Flowable<List<BreedWithSubBreeds>> listFlowable = Flowable
                .fromCallable(() -> breedWithSubBreeds);

        return Flowable
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
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Maybe<DogOfTheDay> getDogOfTheDay() {
        Log.d(TAG, "getDogOfTheDay: try to load dog of the day from local db");
        return mFavouriteDogsDao.getDogOfTheDay();
    }

    @Override
    public void insertFavouriteDog(BreedWithSubBreeds favouriteDog) {
        Single.fromCallable(() -> {
            Breed breed = favouriteDog.getBreed();
            breed.setFavourite(true);
            return mFavouriteDogsDao.insert(breed);
        }).flatMapMaybe(breedId -> {
            for (SubBreed subBreed : favouriteDog.getSubBreeds()) {
                subBreed.setBreedId(breedId);
            }
            if (favouriteDog.getSubBreeds() != null && !favouriteDog.getSubBreeds().isEmpty()) {
                return Maybe.fromCallable(() -> mFavouriteDogsDao.insertAllSubBreeds(favouriteDog.getSubBreeds()));
            }
            return Maybe.empty();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (id) -> Log.d(TAG, "insertFavouriteDog: favourite dog inserted"),
                        (error) -> Log.e(TAG, "insertFavouriteDog: error occurred while inserting favourite dog.", error));
    }

    @Override
    public void insertDogOfTheDay(DogOfTheDay dogOfTheDay) {
        Single.fromCallable(() -> mFavouriteDogsDao.insert(dogOfTheDay))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (id) -> Log.d(TAG, "insertDogOfTheDay: dog of the day inserted with id: " + id),
                        (error) -> Log.e(TAG, "insertDogOfTheDay: error occurred while inserting dog of the day.", error));
    }

    @Override
    public void deleteDogFromFavourites(BreedWithSubBreeds favouriteDog) {
        Single.fromCallable(() -> mFavouriteDogsDao.delete(favouriteDog.getBreed()))
                .flatMapMaybe(numberOfRows -> {
                    if (favouriteDog.getSubBreeds() != null && !favouriteDog.getSubBreeds().isEmpty()) {
                        return Maybe.fromCallable(() -> mFavouriteDogsDao.deleteAllSubBreeds(favouriteDog.getSubBreeds()));
                    }
                    return Maybe.empty();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        numberOfRows -> Log.d(TAG, "deleted: " + numberOfRows + "sub breeds"),
                        error -> Log.e(TAG, "deleting sub breeds: error occurred: ", error),
                        () -> {
                            Log.d(TAG, "deleteDogFromFavourites: breed deleted: " + favouriteDog.getBreed());
                            favouriteDog.getBreed().setId(null);
                            favouriteDog.getBreed().setFavourite(false);
                        }
                );
    }

    @Override
    public Maybe<DogOfTheDay> findFavouriteByName(String name) {
        return mFavouriteDogsDao.findByBreed(name)
                .flatMap(breeds -> {
                    DogOfTheDay dogOfTheDay = new DogOfTheDay();
                    dogOfTheDay.setName(breeds.getBreed().getName());
                    dogOfTheDay.setImageUrl(breeds.getBreed().getImageUrl());
                    dogOfTheDay.setFavourite(breeds.getBreed().isFavourite());
                    return Maybe.fromCallable(() -> dogOfTheDay);
                });
    }
}
