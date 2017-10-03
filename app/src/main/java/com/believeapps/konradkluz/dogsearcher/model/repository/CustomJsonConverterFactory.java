package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.SubBreed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CustomJsonConverterFactory extends Converter.Factory {

    private static final String TAG = "CustomJsonConverterFact";

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return JsonConverter.INSTANCE;
    }

    final static class JsonConverter implements Converter<ResponseBody, List<BreedWithSubBreeds>> {
        static final JsonConverter INSTANCE = new JsonConverter();

        @Override
        public List<BreedWithSubBreeds> convert(ResponseBody responseBody) throws IOException {
            try {
                Log.d(TAG, "convert: try to convert responseBody: " + responseBody);
                return getDogList(responseBody);
            } catch (JSONException e) {
                throw new IOException("Failed to parse JSON", e);
            }
        }

        @NonNull
        private List<BreedWithSubBreeds> getDogList(ResponseBody responseBody) throws JSONException, IOException {
            List<BreedWithSubBreeds> dogs = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(responseBody.string());
            JSONObject message = jsonObject.getJSONObject("message");
            Iterator<String> keys = message.keys();
            while (keys.hasNext()) {
                String breedName = keys.next();

                Breed breed = new Breed();
                breed.setName(breedName);

                JSONArray jsonArray = message.getJSONArray(breedName);
                List<SubBreed> subBreeds = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    SubBreed subBreed = new SubBreed();
                    subBreed.setName(jsonArray.getString(i));
                    subBreeds.add(subBreed);
                }
                BreedWithSubBreeds breedWithSubBreeds = new BreedWithSubBreeds();
                breedWithSubBreeds.setBreed(breed);
                breedWithSubBreeds.setSubBreeds(subBreeds);

                dogs.add(breedWithSubBreeds);
            }
            return dogs;
        }
    }
}