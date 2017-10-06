package com.believeapps.konradkluz.dogsearcher.model.api;

import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.Response;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by konradkluz on 01/10/2017.
 */

public interface DogApiService {
    @GET("/api/breeds/list/all")
    Flowable<List<BreedWithSubBreeds>> getAllDogs();

    @GET("/api/breed/{breedName}/images/random")
    Single<ResponseBody> getImageUrlByBreedName(@Path("breedName") String breedName);

    @GET("/api/breeds/image/random")
    Maybe<ResponseBody> getRandomBreed();
}
