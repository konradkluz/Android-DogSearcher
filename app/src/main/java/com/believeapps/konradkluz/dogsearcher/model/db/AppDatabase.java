package com.believeapps.konradkluz.dogsearcher.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.believeapps.konradkluz.dogsearcher.model.entities.FavouriteDog;

/**
 * Created by konradkluz on 02/10/2017.
 */
@Database(entities = {FavouriteDog.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavouriteDogsDao favouriteDogsDao();

}
