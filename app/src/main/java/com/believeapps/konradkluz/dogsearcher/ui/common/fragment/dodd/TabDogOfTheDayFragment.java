package com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.DogOfTheDay;
import com.believeapps.konradkluz.dogsearcher.model.entities.Status;
import com.believeapps.konradkluz.dogsearcher.ui.main.MainView;
import com.believeapps.konradkluz.dogsearcher.viewmodel.AllDogsFragmentViewModel;
import com.believeapps.konradkluz.dogsearcher.viewmodel.DogOfTheDayFragmentModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import static java.util.Collections.emptyList;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class TabDogOfTheDayFragment extends Fragment implements TabDogOfTheDayView, View.OnClickListener   {

    private static final String TAG = "TabDogOfTheDayFragment";

    private DogOfTheDayFragmentModel mDogOfTheDayFragmentModel;

    private DogOfTheDay mDogOfTheDay;

    private DogOfTheDayChangedListener mDogOfTheDayChangedListener;

    @Inject
    ViewModelProvider.Factory mFactory;

    @BindView(R.id.dog_day_image)
    ImageView mDogImage;

    @BindView(R.id.dog_day_name)
    TextView mDogName;

    @BindView(R.id.add_to_favourites_button)
    ImageButton mAddToFavourites;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Inject
    public TabDogOfTheDayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        View rootView = inflater.inflate(R.layout.fragment_tab_dog_of_the_day, container, false);
        ButterKnife.bind(this, rootView);

        mDogOfTheDayChangedListener = ((MainView) getActivity()).getDogOfTheDayChangedListener();
        mDogOfTheDayFragmentModel = ViewModelProviders.of(this, mFactory).get(DogOfTheDayFragmentModel.class);
        mAddToFavourites.setOnClickListener(this);

        return rootView;
    }

    private void setFields(DogOfTheDay dogOfTheDay) {
        mDogName.setText(dogOfTheDay.getName());
        if (dogOfTheDay.isFavourite()) {
            mAddToFavourites.setTag(android.R.drawable.btn_star_big_on);
            mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            mAddToFavourites.setTag(android.R.drawable.btn_star_big_off);
            mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_off);
        }

        loadImageFromUrl(dogOfTheDay.getImageUrl());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
        mDogOfTheDayFragmentModel.getDbResponse().observe(this, response -> {
            if (response.status == Status.ERROR) {
                Log.e(TAG, "onCreateView: error occurred", response.error);
            } else {
                mDogOfTheDay = (DogOfTheDay) response.data;
                setFields(mDogOfTheDay);
            }
        });

        if (!mDogOfTheDayFragmentModel.isRequestSent()) {
            Log.d(TAG, "onStart: loading dogs from api");
            mDogOfTheDayFragmentModel.loadOrDrawDogOfTheDay();
        }

        if (mDogOfTheDayFragmentModel.isNewDogOfTheDayLoaded()) {
            mDogOfTheDayChangedListener.dogOfTheDayChanged(mDogOfTheDay);
            mDogOfTheDayFragmentModel.setNewDogOfTheDayLoaded(false);
        }
    }

    private void loadImageFromUrl(String imageUrl) {
        Picasso.with(getActivity()).load(imageUrl)
                .error(R.drawable.ic_image_black_48dp)
                .placeholder(R.drawable.ic_image_black_48dp)
                .into(mDogImage);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: Dog of the day favourites btn clicked");
        if (view.getId() == R.id.add_to_favourites_button) {
            Log.d(TAG, "onClick: favourites btn clicked");
            boolean alreadyAdded = handleFavouritesBtnClicked();
            Log.d(TAG, "onFavouritesButtonClicked: clicked");

            Breed breed = new Breed();
            breed.setName(mDogOfTheDay.getName());
            breed.setImageUrl(mDogOfTheDay.getImageUrl());
            breed.setFavourite(mDogOfTheDay.isFavourite());

            BreedWithSubBreeds breedWithSubBreeds = new BreedWithSubBreeds();
            breedWithSubBreeds.setBreed(breed);
            breedWithSubBreeds.setSubBreeds(emptyList());

            if (alreadyAdded) {
                mDogOfTheDayFragmentModel.deleteDogFromFavouritesByName(breed.getName());
                Toast.makeText(getActivity(),
                        getString(R.string.favourite_removed, breedWithSubBreeds.getBreed().getName())
                        , Toast.LENGTH_LONG).show();
            } else {
                mDogOfTheDayFragmentModel.persistFavouriteDog(breedWithSubBreeds);
                Toast.makeText(getActivity(),
                        getString(R.string.favourite_added, breedWithSubBreeds.getBreed().getName())
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean handleFavouritesBtnClicked() {
        if (android.R.drawable.btn_star_big_off == (Integer) mAddToFavourites.getTag()) {
            mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_on);
            mAddToFavourites.setTag(android.R.drawable.btn_star_big_on);
            return false;
        } else {
            mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_off);
            mAddToFavourites.setTag(android.R.drawable.btn_star_big_off);
            return true;
        }
    }
}
