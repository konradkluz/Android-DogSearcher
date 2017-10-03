package com.believeapps.konradkluz.dogsearcher.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.SubBreed;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by konradkluz on 02/10/2017.
 */
@Dao
public interface FavouriteDogsDao {

    @Query("select * from breed_favourite")
    Flowable<List<BreedWithSubBreeds>> getAllFavouriteDogs();

    @Query("select * from breed_favourite where name like :name limit 1")
    Single<BreedWithSubBreeds> findByBreed(String name);

    @Insert(onConflict = REPLACE)
    Long insert(Breed favouriteDog);

    @Insert(onConflict = REPLACE)
    List<Long> insertAllSubBreeds(List<SubBreed> subBreeds);

    @Update
    void update(Breed favouriteDog);

    @Delete
    int delete(Breed favouriteDog);

    @Delete
    int deleteAllSubBreeds(List<SubBreed> subBreeds);

}
