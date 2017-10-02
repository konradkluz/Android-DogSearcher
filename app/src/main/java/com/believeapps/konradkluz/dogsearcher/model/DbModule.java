package com.believeapps.konradkluz.dogsearcher.model;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.believeapps.konradkluz.dogsearcher.model.db.AppDatabase;
import com.believeapps.konradkluz.dogsearcher.model.db.FavouriteDogsDao;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konradkluz on 02/10/2017.
 */
@Module
public class DbModule {

    @Provides
    @Singleton
    FavouriteDogsDao provideFavouriteDogsDao(AppDatabase appDatabase) {
        return appDatabase.favouriteDogsDao();
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "dog.db").build();
    }

    @Provides
    @Singleton
    DogLocalRepository provideDogLocalRepository(DogLocalRepositoryImpl dogLocalRepository) {
        return dogLocalRepository;
    }

}
