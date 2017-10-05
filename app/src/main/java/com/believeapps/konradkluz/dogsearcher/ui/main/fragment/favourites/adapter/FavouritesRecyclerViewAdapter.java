package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by konradkluz on 29/09/2017.
 */

public class FavouritesRecyclerViewAdapter extends RecyclerView.Adapter<DogFavouritesViewHolder>{

    private static final String TAG = "FavouritesRecyclerViewA";

    @Inject
    Context mContext;

    private List<BreedWithSubBreeds> mFavouritesDogs;

    @Inject
    public FavouritesRecyclerViewAdapter() {
        mFavouritesDogs = new ArrayList<>();
    }

    @Override
    public DogFavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dog_details_item, parent, false);
        return new DogFavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DogFavouritesViewHolder holder, int position) {
        if (mFavouritesDogs != null || !mFavouritesDogs.isEmpty()) {
            BreedWithSubBreeds breedWithSubBreeds = mFavouritesDogs.get(position);
            Breed breed = breedWithSubBreeds.getBreed();
            fillViewHolder(holder, breed);
        }
    }

    private void fillViewHolder(DogFavouritesViewHolder holder, Breed breed) {
        holder.mDogImage.setImageResource(R.drawable.ic_image_black_48dp);
        holder.mBreedName.setText(breed.getName());
        if (breed.getId() != null) {
            holder.mAddToFavourites.setTag(android.R.drawable.btn_star_big_on);
            holder.mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.mAddToFavourites.setTag(android.R.drawable.btn_star_big_off);
            holder.mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_off);
        }
        loadImageFromUrl(holder, breed.getImageUrl());
    }

    private void loadImageFromUrl(DogFavouritesViewHolder holder, String imageUrl) {
        Picasso.with(mContext).load(imageUrl)
                .error(R.drawable.ic_image_black_48dp)
                .placeholder(R.drawable.ic_image_black_48dp)
                .into(holder.mDogImage);
    }

    @Override
    public int getItemCount() {
        return mFavouritesDogs != null && !mFavouritesDogs.isEmpty() ? mFavouritesDogs.size() : 0;
    }

    public void swapSource(List<BreedWithSubBreeds> dogs) {
        mFavouritesDogs = dogs;
        notifyDataSetChanged();
    }
}
