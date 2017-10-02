package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.arch.lifecycle.LiveData;

import com.believeapps.konradkluz.dogsearcher.model.entities.Response;

/**
 * Created by konradkluz on 01/10/2017.
 */

public interface DogRepository {
    LiveData<Response> getAllDogs();
}
