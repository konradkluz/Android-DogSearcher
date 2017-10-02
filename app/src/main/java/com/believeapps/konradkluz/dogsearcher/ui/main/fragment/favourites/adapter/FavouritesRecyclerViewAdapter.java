package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.believeapps.konradkluz.dogsearcher.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by konradkluz on 29/09/2017.
 */

public class FavouritesRecyclerViewAdapter extends RecyclerView.Adapter<FavouritesRecyclerViewAdapter.DogFavouritesViewHolder>{

    private static final String TAG = "FavouritesRecyclerViewA";

    private Context mContext;
    private List<String> mFavouritesDogs;

    public FavouritesRecyclerViewAdapter(Context context) {
        mContext = context;
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
            holder.dogImage.setImageResource(R.drawable.ic_image_black_48dp);
            holder.breedName.setText(mFavouritesDogs.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mFavouritesDogs != null && !mFavouritesDogs.isEmpty() ? mFavouritesDogs.size() : 0;
    }

    public void swapSource(List<String> dogs) {
        mFavouritesDogs = dogs;
        notifyDataSetChanged();
    }

    static class DogFavouritesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dog_day_image)
        ImageView dogImage;

        @BindView(R.id.dog_day_name)
        TextView breedName;

        public DogFavouritesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
