package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.api.DogApiService;
import com.believeapps.konradkluz.dogsearcher.model.entities.DogOfTheDay;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.maybe.MaybeToFlowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by konradkluz on 01/10/2017.
 */

public class DogRemoteRepositoryImpl implements DogRemoteRepository {

    private static final String TAG = "DogRemoteRepositoryImpl";

    @Inject
    DogApiService mDogApiService;

    @Inject
    public DogRemoteRepositoryImpl() {
    }

    @Override
    public LiveData<Response> getAllDogs() {
        final MutableLiveData<Response> liveData = new MutableLiveData<>();
        Log.d(TAG, "getAllDogs: request sent");
        mDogApiService.getAllDogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> liveData.setValue(Response.success(response)),
                        error -> liveData.setValue(Response.error(error))
                );
        Log.d(TAG, "getAllDogs: request finished");
        return liveData;
    }

    @Override
    public Single<String> loadImageUrlByBreedName(String breedName) {
        return mDogApiService.getImageUrlByBreedName(breedName)
                .map(responseBody -> {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    return jsonObject.getString("message");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Maybe<DogOfTheDay> loadRandomBreed() {
        Log.d(TAG, "loadRandomBreed: try to load random breed from remote repository");
        return mDogApiService.getRandomBreed()
                .map(responseBody -> {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    String message = jsonObject.getString("message");
                    Log.d(TAG, "loadRandomBreed: received random breed with url: " + message);
                    return message;
                }).flatMap(dogOfTheDayUrl -> {
                    Log.d(TAG, "loadOrDrawDogOfTheDay: url from remote repo received: " + dogOfTheDayUrl);

                    return Maybe.fromCallable(() -> createDogOfTheDay(dogOfTheDayUrl));
                });
    }

    private DogOfTheDay createDogOfTheDay(String message) {
        Log.d(TAG, "createDogOfTheDay: creating dog from url: " + message);
        Pattern pattern = Pattern.compile("(?<=img\\/).*?(?=\\s*\\/)");
        Matcher matcher = pattern.matcher(message);
        DogOfTheDay dogOfTheDay = new DogOfTheDay();
        if (matcher.find()) {
            String breedName = matcher.group(0);
            String[] split = breedName.split("-");
            dogOfTheDay.setName(split[0]);
            dogOfTheDay.setImageUrl(message);
        }
        return dogOfTheDay;
    }
}
