package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.api.DogApiService;
import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.SubBreed;
import com.believeapps.konradkluz.dogsearcher.viewmodel.AllDogsFragmentViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class AllDogsRecyclerViewAdapter extends RecyclerView.Adapter<DogViewHolder> {

    private static final String TAG = "AllDogsRecyclerViewAdap";

    private Context mContext;
    private DogViewHolder.AllDogsItemClickListener mAllDogsItemClickListener;
    private AllDogsFragmentViewModel mAllDogsFragmentViewModel;
    private List<BreedWithSubBreeds> mDogs;

    @Inject
    DogApiService mDogApiService;

    @Inject
    public AllDogsRecyclerViewAdapter(Context context) {
        Log.d(TAG, "AllDogsRecyclerViewAdapter: initializing view adapter.");
        mContext = context;
        mDogs = new ArrayList<>();
    }

    @Override
    public DogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view registered");
        View view = LayoutInflater.from(mContext).inflate(R.layout.browse_item, parent, false);
        return new DogViewHolder(view, mAllDogsItemClickListener);
    }

    @Override
    public void onBindViewHolder(DogViewHolder holder, int position) {
        if (mDogs == null || mDogs.isEmpty()) {
            fillEmptyViewHolder(holder);
        } else {
            BreedWithSubBreeds breedWithSubBreeds = mDogs.get(position);
            Breed breed = breedWithSubBreeds.getBreed();
            List<SubBreed> subBreeds = breedWithSubBreeds.getSubBreeds();
            fillViewHolder(holder, breed, subBreeds);
        }
    }

    @Override
    public int getItemCount() {
        return mDogs != null && !mDogs.isEmpty() ? mDogs.size() : 1;
    }

    private void fillViewHolder(DogViewHolder holder, Breed breed, List<SubBreed> subBreeds) {
        Log.d(TAG, "onBindViewHolder: dog list has elements");
        holder.dogThumbnail.setImageResource(R.drawable.ic_image_black_48dp);
        holder.breedName.setText(breed.getName());

        holder.subBreeds.setText(TextUtils.join(",", subBreeds));
        holder.addToFavourites.setVisibility(View.VISIBLE);
        if (breed.isFavourite()) {
            holder.addToFavourites.setTag(android.R.drawable.btn_star_big_on);
            holder.addToFavourites.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.addToFavourites.setTag(android.R.drawable.btn_star_big_off);
            holder.addToFavourites.setImageResource(android.R.drawable.btn_star_big_off);
        }

        loadImageFromUrl(holder, breed);
    }

    private void fillEmptyViewHolder(DogViewHolder holder) {
        Log.d(TAG, "onBindViewHolder: dog list empty");
        holder.dogThumbnail.setImageResource(R.drawable.ic_image_black_48dp);
        holder.breedName.setText(R.string.empty_query_search);
        holder.subBreeds.setText(R.string.empty);
        holder.addToFavourites.setVisibility(View.GONE);
    }

    private void loadImageFromUrl(DogViewHolder holder, Breed breed) {
        if (breed.getImageUrl() != null) {
            loadImageFromUrl(holder, breed.getImageUrl());
        } else {
            mAllDogsFragmentViewModel.loadImageUrlByBreedName(
                    breed.getName(),
                    imageUrl -> {
                        Log.d(TAG, "onBindViewHolder: subscribe: " + imageUrl);
                        breed.setImageUrl(imageUrl);
                        loadImageFromUrl(holder, imageUrl);
                    },
                    error -> Log.e(TAG, "onBindViewHolder: error occurred: ", error));
        }
    }

    private void loadImageFromUrl(DogViewHolder holder, String imageUrl) {
        Picasso.with(mContext).load(imageUrl)
                .error(R.drawable.ic_image_black_48dp)
                .placeholder(R.drawable.ic_image_black_48dp)
                .into(holder.dogThumbnail);
    }

    public void swapSource(List<BreedWithSubBreeds> dogs) {
        mDogs = dogs;
        notifyDataSetChanged();
    }

    public BreedWithSubBreeds getBreed(int position) {
        return mDogs != null && !mDogs.isEmpty() ? mDogs.get(position) : null;
    }

    public void setAllDogsItemClickListener(DogViewHolder.AllDogsItemClickListener allDogsItemClickListener) {
        mAllDogsItemClickListener = allDogsItemClickListener;
    }

    public void setViewModel(AllDogsFragmentViewModel viewModel) {
        mAllDogsFragmentViewModel = viewModel;
    }
}
