package com.believeapps.konradkluz.dogsearcher.model.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.believeapps.konradkluz.dogsearcher.model.entities.Dog;

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

    final static class JsonConverter implements Converter<ResponseBody, List<Dog>> {
        static final JsonConverter INSTANCE = new JsonConverter();

        @Override
        public List<Dog> convert(ResponseBody responseBody) throws IOException {
            try {
                Log.d(TAG, "convert: try to convert responseBody: " + responseBody);
                return getDogList(responseBody);
            } catch (JSONException e) {
                throw new IOException("Failed to parse JSON", e);
            }
        }

        @NonNull
        private List<Dog> getDogList(ResponseBody responseBody) throws JSONException, IOException {
            List<Dog> dogs = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(responseBody.string());
            JSONObject message = jsonObject.getJSONObject("message");
            Iterator<String> keys = message.keys();
            while (keys.hasNext()) {
                String breed = keys.next();
                JSONArray jsonArray = message.getJSONArray(breed);
                List<String> subBreeds = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    subBreeds.add(jsonArray.getString(i));
                }
                dogs.add(new Dog(breed, subBreeds));
            }
            return dogs;
        }
    }
}