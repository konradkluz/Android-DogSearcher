package com.believeapps.konradkluz.dogsearcher.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.believeapps.konradkluz.dogsearcher.model.entities.FavouriteDog;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by konradkluz on 02/10/2017.
 */
@Dao
public interface FavouriteDogsDao {

    @Query("select * from favourite_dogs")
    Flowable<List<FavouriteDog>> getAllFavouriteDogs();

    @Query("select * from favourite_dogs where breed like :breed limit 1")
    Single<FavouriteDog> findByBreed(String breed);

    @Insert(onConflict = REPLACE)
    long insert(FavouriteDog favouriteDog);

    @Update
    void update(FavouriteDog favouriteDog);

    @Delete
    void delete(FavouriteDog favouriteDog);

}
