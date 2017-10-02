package com.believeapps.konradkluz.dogsearcher.model.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by konradkluz on 01/10/2017.
 */

public class Dog implements Serializable {

    private String breedName;
    private List<String> subBreedsNames;
    private String imageUrl;

    public Dog(String breedName, List<String> subBreedsNames) {
        this.breedName = breedName;
        this.subBreedsNames = subBreedsNames;
    }

    public String getBreedName() {
        return breedName;
    }

    public List<String> getSubBreedsNames() {
        return subBreedsNames;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "breedName='" + breedName + '\'' +
                ", subBreedsNames=" + subBreedsNames +
                '}';
    }
}
