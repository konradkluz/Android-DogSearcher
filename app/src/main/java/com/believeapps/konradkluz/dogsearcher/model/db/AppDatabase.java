package com.believeapps.konradkluz.dogsearcher.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.DogOfTheDay;
import com.believeapps.konradkluz.dogsearcher.model.entities.SubBreed;

/**
 * Created by konradkluz on 02/10/2017.
 */
@Database(entities = {Breed.class, SubBreed.class, DogOfTheDay.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavouriteDogsDao favouriteDogsDao();

}
