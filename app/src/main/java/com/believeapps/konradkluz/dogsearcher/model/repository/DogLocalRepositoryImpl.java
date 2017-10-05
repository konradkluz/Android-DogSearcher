package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.db.FavouriteDogsDao;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;
import com.believeapps.konradkluz.dogsearcher.model.entities.SubBreed;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
    public Disposable getAllFavourites(
            List<BreedWithSubBreeds> breedWithSubBreeds,
            Consumer<List<BreedWithSubBreeds>> onNext,
            Consumer<Throwable> onError) {
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    @Override
    public Disposable insertFavouriteDog(BreedWithSubBreeds favouriteDog) {
        return Flowable.fromCallable(() -> mFavouriteDogsDao.insert(favouriteDog.getBreed()))
                .flatMap(breedId -> {
                    for (SubBreed subBreed : favouriteDog.getSubBreeds()) {
                        subBreed.setBreedId(breedId);
                    }
                    if (favouriteDog.getSubBreeds() != null && !favouriteDog.getSubBreeds().isEmpty()) {
                        return Flowable.fromCallable(() -> mFavouriteDogsDao.insertAllSubBreeds(favouriteDog.getSubBreeds()));
                    }
                    return Flowable.empty();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        id -> Log.d(TAG, "persistFavouriteDog: favourite inserted: " + id),
                        error -> Log.e(TAG, "persistFavouriteDog: error occurred: ", error),
                        () -> Log.d(TAG, "persistFavouriteDog: breed persisted: " + favouriteDog.getBreed())
                );
    }

    @Override
    public Disposable deleteDogFromFavourites(BreedWithSubBreeds favouriteDog) {
        return Flowable.fromCallable(() -> mFavouriteDogsDao.delete(favouriteDog.getBreed()))
                .flatMap(numberOfRows -> {
                    if (favouriteDog.getSubBreeds() != null && !favouriteDog.getSubBreeds().isEmpty()) {
                        return Flowable.fromCallable(() -> mFavouriteDogsDao.deleteAllSubBreeds(favouriteDog.getSubBreeds()));
                    }
                    return Flowable.empty();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        numberOfRows -> Log.d(TAG, "deleted: " + numberOfRows + "sub breeds"),
                        error -> Log.e(TAG, "deleting sub breeds: error occurred: ", error),
                        () -> {
                            Log.d(TAG, "deleteDogFromFavourites: breed deleted: " + favouriteDog.getBreed());
                            favouriteDog.getBreed().setId(null);
                        }
                );
    }
}
