package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.Status;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter.DogFavouritesViewHolder;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter.FavouritesRecyclerViewAdapter;
import com.believeapps.konradkluz.dogsearcher.viewmodel.FavouritesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class TabFavouritesFragment extends Fragment implements TabFavouritesView, DogFavouritesViewHolder.FavouritesItemClickListener{

    private static final String TAG = "TabFavouritesFragment";

    FavouritesViewModel mFavouritesViewModel;

    @Inject
    ViewModelProvider.Factory mFactory;

    @Inject
    FavouritesRecyclerViewAdapter mFavouritesRecyclerViewAdapter;

    @BindView(R.id.favourites_dogs_list)
    RecyclerView mRecyclerView;

    @Inject
    public TabFavouritesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_favourites, container, false);
        ButterKnife.bind(this, rootView);

        mFavouritesViewModel = ViewModelProviders.of(this, mFactory).get(FavouritesViewModel.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFavouritesRecyclerViewAdapter);
        mFavouritesRecyclerViewAdapter.setFavouritesItemClickListener(this);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFavouritesViewModel.getDbResponse().observe(this, response -> {
            if (response.status == Status.ERROR) {
                mFavouritesRecyclerViewAdapter.swapSource(new ArrayList<>());
                Log.e(TAG, "onCreateView: error occurred", response.error);
            } else {
                List<BreedWithSubBreeds> breedWithSubBreeds = (List<BreedWithSubBreeds>) response.data;
                mFavouritesRecyclerViewAdapter.swapSource(breedWithSubBreeds);
                Log.d(TAG, "onCreateView: response succeed: " + response.data);
            }
        });

        if (!mFavouritesViewModel.isRequestSent()) {
            Log.d(TAG, "onStart: loading dogs from db");
            mFavouritesViewModel.getFavouriteDogs();
        }
    }

    @Override
    public void onFavouritesButtonClicked(int position, boolean alreadyAdded) {
        Log.d(TAG, "onFavouritesButtonClicked: clicked");
        BreedWithSubBreeds breedWithSubBreeds = mFavouritesRecyclerViewAdapter.getBreed(position);
        if (alreadyAdded) {
            mFavouritesViewModel.deleteDogFromFavourites(breedWithSubBreeds);
        } else {
            mFavouritesViewModel.persistFavouriteDog(breedWithSubBreeds);
        }
    }
}
