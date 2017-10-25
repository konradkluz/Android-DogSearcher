package com.believeapps.konradkluz.dogsearcher.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.viewmodel.DogDetailActivityModel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class DogDetailActivity extends AppCompatActivity implements View.OnClickListener  {

    private static final String TAG = "DogDetailActivity";

    private DogDetailActivityModel mDogDetailActivityModel;

    @Inject
    ViewModelProvider.Factory mFactory;

    @BindView(R.id.dog_detail_image)
    ImageView mDogImage;

    @BindView(R.id.dog_detail_name)
    TextView mDogName;

    @BindView(R.id.dog_detail_add_to_favourites_button)
    ImageButton mAddToFavourites;

    private BreedWithSubBreeds mBreedWithSubBreeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_detail);
        ButterKnife.bind(this);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDogDetailActivityModel = ViewModelProviders.of(this, mFactory).get(DogDetailActivityModel.class);

        mBreedWithSubBreeds = (BreedWithSubBreeds) getIntent().getSerializableExtra("dog");
        Log.d(TAG, "onCreate: " + mBreedWithSubBreeds);
        if (mBreedWithSubBreeds != null) {
            Log.d(TAG, "onCreateView: dog found");
            Breed breed = mBreedWithSubBreeds.getBreed();
            setFields(breed);
        }
        mAddToFavourites.setOnClickListener(this);

    }


    private void setFields(Breed breed) {
        mDogName.setText(breed.getName());
        if (breed.isFavourite()) {
            mAddToFavourites.setTag(android.R.drawable.btn_star_big_on);
            mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            mAddToFavourites.setTag(android.R.drawable.btn_star_big_off);
            mAddToFavourites.setImageResource(android.R.drawable.btn_star_big_off);
        }

        loadImageFromUrl(breed.getImageUrl());
    }

    private void loadImageFromUrl(String imageUrl) {
        Picasso.with(this).load(imageUrl)
                .error(R.drawable.ic_image_black_48dp)
                .placeholder(R.drawable.ic_image_black_48dp)
                .into(mDogImage);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: Dog detail activity favourites btn clicked");
        if (view.getId() == R.id.dog_detail_add_to_favourites_button) {
            Log.d(TAG, "onClick: favourites btn clicked");
            boolean alreadyAdded = handleFavouritesBtnClicked();
            Log.d(TAG, "onFavouritesButtonClicked: clicked");
            if (alreadyAdded) {
                mDogDetailActivityModel.deleteDogFromFavourites(mBreedWithSubBreeds);
                Toast.makeText(this,
                        getString(R.string.favourite_removed, mBreedWithSubBreeds.getBreed().getName())
                        , Toast.LENGTH_LONG).show();
            } else {
                mDogDetailActivityModel.persistFavouriteDog(mBreedWithSubBreeds);
                Toast.makeText(this,
                        getString(R.string.favourite_added, mBreedWithSubBreeds.getBreed().getName())
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
