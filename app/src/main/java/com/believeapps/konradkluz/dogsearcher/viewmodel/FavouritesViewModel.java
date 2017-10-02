package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.believeapps.konradkluz.dogsearcher.model.db.AppDatabase;
import com.believeapps.konradkluz.dogsearcher.model.entities.FavouriteDog;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogRemoteRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by konradkluz on 02/10/2017.
 */

public class FavouritesViewModel extends ViewModel {

//    private final LiveData<List<FavouriteDog>> mFavouriteDogs;
//    private final MediatorLiveData<List<FavouriteDog>> mFavouriteDogs;

    private DogLocalRepository mDogLocalRepository;
    private CompositeDisposable compositeDisposable;

    @Inject
    public FavouritesViewModel(DogLocalRepository localRepository) {
        compositeDisposable = new CompositeDisposable();
        mDogLocalRepository = localRepository;
    }

    public Flowable<List<FavouriteDog>> getFavouriteDogs() {
        return mDogLocalRepository.getAllFavourites();
    }

    @Override
    public void onCleared(){
        //prevents memory leaks by disposing pending observable objects
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
