package com.believeapps.konradkluz.dogsearcher.model.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by konradkluz on 02/10/2017.
 */
@Entity(tableName = "favourite_dogs")
public class FavouriteDog {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "breed")
    private String breed;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
