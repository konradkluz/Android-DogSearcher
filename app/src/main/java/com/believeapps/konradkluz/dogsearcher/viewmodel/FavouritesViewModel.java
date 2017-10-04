package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by konradkluz on 02/10/2017.
 */

public class FavouritesViewModel extends ViewModel {

    private DogLocalRepository mDogLocalRepository;
    private CompositeDisposable compositeDisposable;

    @Inject
    public FavouritesViewModel(DogLocalRepository localRepository) {
        compositeDisposable = new CompositeDisposable();
        mDogLocalRepository = localRepository;
    }

    public void getFavouriteDogs(
            Consumer<List<BreedWithSubBreeds>> onNext,
            Consumer<Throwable> onError) {
        compositeDisposable.add(mDogLocalRepository.getFavouriteDogs(onNext, onError));
    }

    @Override
    public void onCleared() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
