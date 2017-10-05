package com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd;

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
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class TabDogOfTheDayFragment extends Fragment implements TabDogOfTheDayView, View.OnClickListener {

    private static final String TAG = "TabDogOfTheDayFragment";

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

        View rootView = inflater.inflate(R.layout.fragment_tab_dog_of_the_day, container, false);
        ButterKnife.bind(this, rootView);

        Bundle arguments = getArguments();
        if (arguments != null) {
            BreedWithSubBreeds breedWithSubBreeds = (BreedWithSubBreeds) arguments.getSerializable("dog");
            if (breedWithSubBreeds != null) {
                Log.d(TAG, "onCreateView: dog found");
                Breed breed = breedWithSubBreeds.getBreed();
                mDogName.setText(breed.getName());
                if (breed.getId() != null) {
                    mAddToFavourites.setTag(android.R.drawable.btn_star_big_on);
                    mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_on);
                } else {
                    mAddToFavourites.setTag(android.R.drawable.btn_star_big_off);
                    mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_off);
                }

                loadImageFromUrl(breed.getImageUrl());
            }
        } else {
            Log.d(TAG, "onCreateView: dog not sent to fragment");
        }

        mAddToFavourites.setOnClickListener(this);

        return rootView;
    }

    private void loadImageFromUrl(String imageUrl) {
        Picasso.with(getActivity()).load(imageUrl)
                .error(R.drawable.ic_image_black_48dp)
                .placeholder(R.drawable.ic_image_black_48dp)
                .into(mDogImage);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_to_favourites_button) {
            Log.d(TAG, "onClick: favourites btn clicked");
            boolean alreadyAdded = handleFavouritesBtnClicked();
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
