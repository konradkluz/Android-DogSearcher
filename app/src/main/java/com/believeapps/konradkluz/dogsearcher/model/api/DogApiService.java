package com.believeapps.konradkluz.dogsearcher.model.api;

import com.believeapps.konradkluz.dogsearcher.model.entities.Dog;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by konradkluz on 01/10/2017.
 */

public interface DogApiService {
    @GET("/api/breeds/list/all")
    Observable<List<Dog>> getAllDogs();

    @GET("/api/breed/{breedName}/images/random")
    Observable<ResponseBody> getImageUrlByBreedName(@Path("breedName") String breedName);
}
