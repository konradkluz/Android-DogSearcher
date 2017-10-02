package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.db.FavouriteDogsDao;
import com.believeapps.konradkluz.dogsearcher.model.entities.FavouriteDog;

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
    public Flowable<List<FavouriteDog>> getAllFavourites() {
        return mFavouriteDogsDao.getAllFavouriteDogs();
    }

    @Override
    public void insertFavouriteDog(FavouriteDog favouriteDog) {
        Observable.fromCallable(() -> mFavouriteDogsDao.insert(favouriteDog))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> Log.d(TAG, "persistFavouriteDog: favourite inserted: " + id),
                        error -> Log.e(TAG, "persistFavouriteDog: error occurred: ", error));

    }
}
