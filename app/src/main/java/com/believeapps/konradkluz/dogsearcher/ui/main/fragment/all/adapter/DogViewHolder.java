package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.believeapps.konradkluz.dogsearcher.R;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface AllDogsItemClickListener {
        void onRowPositionClicked(int position);

        void onFavouritesButtonClicked(int position, boolean alreadyAdded);
    }

    private static final String TAG = "DogViewHolder";

    private WeakReference<AllDogsItemClickListener> listenerRef;

    @BindView(R.id.browse_item_dog_image_thumbnail)
    ImageView dogThumbnail;

    @BindView(R.id.browse_item_dog_breed_name)
    TextView breedName;

    @BindView(R.id.browse_item_sub_breeds)
    TextView subBreeds;

    @BindView(R.id.browse_item_add_to_favourites_button)
    ImageButton addToFavourites;

    public DogViewHolder(View itemView, AllDogsItemClickListener allDogsItemClickListener) {
        super(itemView);
        Log.d(TAG, "DogViewHolder: creating view holder");
        ButterKnife.bind(this, itemView);

        if (allDogsItemClickListener != null) {
            listenerRef = new WeakReference<>(allDogsItemClickListener);
        }

        itemView.setOnClickListener(this);
        addToFavourites.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.browse_item_add_to_favourites_button) {
            Log.d(TAG, "onClick: favourites btn clicked");
            boolean alreadyAdded = handleFavouritesBtnClicked();
            if (listenerRef != null) {
                listenerRef.get().onFavouritesButtonClicked(getAdapterPosition(), alreadyAdded);
            }
        } else {
            Log.d(TAG, "onClick: row clicked");
            if (listenerRef != null) {
                listenerRef.get().onRowPositionClicked(getAdapterPosition());
            }
        }
    }

    private boolean handleFavouritesBtnClicked() {
        if (android.R.drawable.btn_star_big_off == (Integer) addToFavourites.getTag()) {
            addToFavourites.setImageResource(android.R.drawable.btn_star_big_on);
            addToFavourites.setTag(android.R.drawable.btn_star_big_on);
            return false;
        } else {
            addToFavourites.setImageResource(android.R.drawable.btn_star_big_off);
            addToFavourites.setTag(android.R.drawable.btn_star_big_off);
            return true;
        }
    }
}