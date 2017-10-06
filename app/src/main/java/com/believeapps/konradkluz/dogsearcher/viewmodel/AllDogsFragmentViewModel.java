package com.believeapps.konradkluz.dogsearcher.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogRemoteRepository;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by konradkluz on 30/09/2017.
 */

public class AllDogsFragmentViewModel extends ViewModel {

    private static final String TAG = "AllDogsFragmentViewMode";

    private final MediatorLiveData<Response> mApiResponse;
    private CompositeDisposable compositeDisposable;

    private DogRemoteRepository mDogRemoteRepository;
    private DogLocalRepository mDogLocalRepository;
    private boolean requestSent = false;

    @Inject
    public AllDogsFragmentViewModel(DogRemoteRepository remoteRepository,
                                    DogLocalRepository localRepository) {
        mApiResponse = new MediatorLiveData<>();
        compositeDisposable = new CompositeDisposable();
        mDogRemoteRepository = remoteRepository;
        mDogLocalRepository = localRepository;
    }

    @NonNull
    public MediatorLiveData<Response> getApiResponse() {
        return mApiResponse;
    }

    public LiveData<Response> loadDogs() {
        mApiResponse.addSource(
                mDogRemoteRepository.getAllDogs(),
                response -> mApiResponse.setValue(response));
        requestSent = true;
        return mApiResponse;
    }

    public void persistFavouriteDog(BreedWithSubBreeds favouriteDog) {
        Log.d(TAG, "persistFavouriteDog: persisting favourite dog: " + favouriteDog);

        mDogLocalRepository.insertFavouriteDog(favouriteDog);
    }

    public void deleteDogFromFavourites(BreedWithSubBreeds favouriteDog) {
        Log.d(TAG, "deleteDogFromFavourites: deleting from favourites: " + favouriteDog);
        mDogLocalRepository.deleteDogFromFavourites(favouriteDog);
    }

    public void updateDogsFromApiWithFavourites(List<BreedWithSubBreeds> breedWithSubBreeds,
                                                Consumer<List<BreedWithSubBreeds>> onNext,
                                                Consumer<Throwable> onError) {
        Log.d(TAG, "updateDogsFromApiWithFavourites: updating dogs");
        compositeDisposable.add(mDogLocalRepository.getAllFavourites(breedWithSubBreeds).subscribe(onNext, onError));
    }

    public void loadImageUrlByBreedName(String breedName,
                                        Consumer<String> onNext,
                                        Consumer<Throwable> onError) {
        compositeDisposable.add(mDogRemoteRepository.loadImageUrlByBreedName(breedName)
                .subscribe(onNext, onError)
        );
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }

    public boolean isRequestSent() {
        return requestSent;
    }
}
