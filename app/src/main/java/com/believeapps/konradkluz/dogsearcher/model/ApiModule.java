package com.believeapps.konradkluz.dogsearcher.model;

import com.believeapps.konradkluz.dogsearcher.model.api.DogApiService;
import com.believeapps.konradkluz.dogsearcher.model.repository.CustomJsonConverterFactory;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogRepository;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by konradkluz on 01/10/2017.
 */
@Module
public class ApiModule {
    public final String BASE_URL = "https://dog.ceo";

//    @Provides
//    public OkHttpClient provideClient() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
//    }

    @Provides
    @Singleton
    public DogApiService provideDogApiService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new CustomJsonConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DogApiService.class);
    }

    @Provides
    @Singleton
    DogRepository provideDogRepository(DogRepositoryImpl dogRepository) {
        return dogRepository;
    }

}
