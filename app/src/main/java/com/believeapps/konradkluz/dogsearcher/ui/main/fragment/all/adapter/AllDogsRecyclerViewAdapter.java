package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.api.DogApiService;
import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.SubBreed;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class AllDogsRecyclerViewAdapter extends RecyclerView.Adapter<AllDogsRecyclerViewAdapter.DogViewHolder> {

    private static final String TAG = "AllDogsRecyclerViewAdap";

    private Context mContext;
    private DogViewHolder.AllDogsItemClickListener mAllDogsItemClickListener;
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
    public AllDogsRecyclerViewAdapter.DogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view registered");
        View view = LayoutInflater.from(mContext).inflate(R.layout.browse_item, parent, false);
        return new DogViewHolder(view, mAllDogsItemClickListener);
    }

    @Override
    public void onBindViewHolder(AllDogsRecyclerViewAdapter.DogViewHolder holder, int position) {
        if (mDogs == null || mDogs.isEmpty()) {
            Log.d(TAG, "onBindViewHolder: dog list empty");
            holder.dogThumbnail.setImageResource(R.drawable.ic_image_black_48dp);
            holder.breedName.setText(R.string.empty_query_search);
            holder.subBreeds.setText(R.string.empty);
            holder.addToFavourites.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "onBindViewHolder: dog list has elements");
            holder.dogThumbnail.setImageResource(R.drawable.ic_image_black_48dp);
            BreedWithSubBreeds breedWithSubBreeds = mDogs.get(position);

            Breed breed = breedWithSubBreeds.breed;
            List<SubBreed> subBreeds = breedWithSubBreeds.subBreeds;

            holder.breedName.setText(breed.getName());
            holder.subBreeds.setText(subBreeds.toString());
            holder.addToFavourites.setVisibility(View.VISIBLE);
            if (breed.getId() != null) {
                holder.addToFavourites.setTag(android.R.drawable.btn_star_big_on);
                holder.addToFavourites.setImageResource(android.R.drawable.btn_star_big_on);
            }else {
                holder.addToFavourites.setTag(android.R.drawable.btn_star_big_off);
                holder.addToFavourites.setImageResource(android.R.drawable.btn_star_big_off);
            }

            if (breed.getImageUrl() != null) {
                Picasso.with(mContext).load(breed.getImageUrl())
                        .error(R.drawable.ic_image_black_48dp)
                        .placeholder(R.drawable.ic_image_black_48dp)
                        .into(holder.dogThumbnail);
            } else {
                mDogApiService.getImageUrlByBreedName(breed.getName())
                        .map(responseBody -> {
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            return jsonObject.getString("message");
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(imageUrl -> {
                                    Log.d(TAG, "onBindViewHolder: subscribe: " + imageUrl);
                                    breed.setImageUrl(imageUrl);
                                    Picasso.with(mContext).load(imageUrl)
                                            .error(R.drawable.ic_image_black_48dp)
                                            .placeholder(R.drawable.ic_image_black_48dp)
                                            .into(holder.dogThumbnail);
                                }, error ->
                                        Log.e(TAG, "onBindViewHolder: error occurred: ", error)
                        );
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDogs != null && !mDogs.isEmpty() ? mDogs.size() : 1;
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

    public static class DogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
}
