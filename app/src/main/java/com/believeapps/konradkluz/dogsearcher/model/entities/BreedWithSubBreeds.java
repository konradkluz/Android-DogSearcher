package com.believeapps.konradkluz.dogsearcher.model.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.io.Serializable;
import java.util.List;

/**
 * Created by konradkluz on 03/10/2017.
 */

public class BreedWithSubBreeds implements Serializable{

    @Embedded
    public Breed breed;

    @Relation(parentColumn = "id", entityColumn = "breedId", entity = SubBreed.class)
    public List<SubBreed> subBreeds;

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public List<SubBreed> getSubBreeds() {
        return subBreeds;
    }

    public void setSubBreeds(List<SubBreed> subBreeds) {
        this.subBreeds = subBreeds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BreedWithSubBreeds that = (BreedWithSubBreeds) o;

        return breed != null ? breed.equals(that.breed) : that.breed == null;
    }

    @Override
    public int hashCode() {
        return breed != null ? breed.hashCode() : 0;
    }

    @Override
    public String toString() {
        return breed.toString();
    }
}
