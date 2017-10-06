package com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.DogOfTheDay;
import com.believeapps.konradkluz.dogsearcher.model.entities.Status;
import com.believeapps.konradkluz.dogsearcher.viewmodel.AllDogsFragmentViewModel;
import com.believeapps.konradkluz.dogsearcher.viewmodel.DogOfTheDayFragmentModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class TabDogOfTheDayFragment extends Fragment implements TabDogOfTheDayView {

    private static final String TAG = "TabDogOfTheDayFragment";

    private DogOfTheDayFragmentModel mDogOfTheDayFragmentModel;

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

        mDogOfTheDayFragmentModel = ViewModelProviders.of(this, mFactory).get(DogOfTheDayFragmentModel.class);

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
                DogOfTheDay dogOfTheDay = (DogOfTheDay) response.data;
                setFields(dogOfTheDay);
            }
        });

        //TODO how to verify request sent
        if (!mDogOfTheDayFragmentModel.isRequestSent()) {
            Log.d(TAG, "onStart: loading dogs from api");
            mDogOfTheDayFragmentModel.loadOrDrawDogOfTheDay();
        }

    }

    private void loadImageFromUrl(String imageUrl) {
        Picasso.with(getActivity()).load(imageUrl)
                .error(R.drawable.ic_image_black_48dp)
                .placeholder(R.drawable.ic_image_black_48dp)
                .into(mDogImage);
    }
}
