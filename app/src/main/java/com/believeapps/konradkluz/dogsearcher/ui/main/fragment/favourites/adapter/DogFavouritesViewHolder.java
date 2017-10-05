package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.believeapps.konradkluz.dogsearcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DogFavouritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "DogFavouritesViewHolder";

    @BindView(R.id.dog_day_image)
    ImageView mDogImage;

    @BindView(R.id.dog_day_name)
    TextView mBreedName;

    @BindView(R.id.dog_details_item_add_to_favourites_button)
    ImageButton mAddToFavourites;

    public DogFavouritesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mAddToFavourites.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dog_details_item_add_to_favourites_button) {
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