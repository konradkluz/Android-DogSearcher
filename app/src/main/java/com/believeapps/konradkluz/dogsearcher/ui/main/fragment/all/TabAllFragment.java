package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.believeapps.konradkluz.dogsearcher.ui.detail.DogDetailActivity;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter.AllDogsRecyclerViewAdapter;
import com.believeapps.konradkluz.dogsearcher.viewmodel.AllDogsFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class TabAllFragment extends Fragment implements TabAllView,
        AllDogsRecyclerViewAdapter.DogViewHolder.AllDogsItemClickListener {

    private static final String TAG = "TabAllFragment";

    AllDogsFragmentViewModel mAllDogsFragmentViewModel;

    @Inject
    ViewModelProvider.Factory mFactory;

    @Inject
    AllDogsRecyclerViewAdapter mAllDogsRecyclerViewAdapter;

    @BindView(R.id.all_dogs_list)
    RecyclerView mAllDogs;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_all, container, false);
        ButterKnife.bind(this, rootView);

        mAllDogsFragmentViewModel = ViewModelProviders.of(this, mFactory).get(AllDogsFragmentViewModel.class);

        mAllDogs.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAllDogs.setAdapter(mAllDogsRecyclerViewAdapter);
        mAllDogsRecyclerViewAdapter.setAllDogsItemClickListener(this);

        mAllDogsFragmentViewModel.getApiResponse().observe(this, response -> {
            if (response.status == Status.ERROR) {
                mAllDogsRecyclerViewAdapter.swapSource(new ArrayList<>());
                Log.e(TAG, "onCreateView: error occurred", response.error);
            } else {
                List<BreedWithSubBreeds> breedWithSubBreeds = (List<BreedWithSubBreeds>) response.data;
                mAllDogsFragmentViewModel.updateDogs(breedWithSubBreeds)
                        .subscribe(
                                updatedBreeds -> {
                                    Log.d(TAG, "onCreateView: updated breeds: " + updatedBreeds);
                                    mAllDogsRecyclerViewAdapter.swapSource(updatedBreeds);
                                },
                                error -> Log.e(TAG, "onCreateView: error occurred: ", error));
                Log.d(TAG, "onCreateView: response succeed: " + response.data);
            }
        });

        //TODO how to verify request sent
        if (!mAllDogsFragmentViewModel.isRequestSent()) {
            mAllDogsFragmentViewModel.loadDogs();
        }

        return rootView;
    }

    @Override
    public void onRowPositionClicked(int position) {
        Log.d(TAG, "onItemClick: " + position);
        Log.d(TAG, "Portrait onRecyclerViewItemClicked: clicked");
        BreedWithSubBreeds breedWithSubBreeds = mAllDogsRecyclerViewAdapter.getBreed(position);
        if (breedWithSubBreeds != null) {
            Intent intent = new Intent(getActivity(), DogDetailActivity.class);
            intent.putExtra("dog", breedWithSubBreeds);
            getActivity().startActivity(intent);
            Log.d(TAG, "onRowPositionClicked: dog will be displayed");
        } else {
            Log.d(TAG, "onRowPositionClicked: dog list is empty");
        }
    }

    @Override
    public void onFavouritesButtonClicked(int position, boolean alreadyAdded) {
        Log.d(TAG, "onFavouritesButtonClicked: clicked");
        BreedWithSubBreeds breedWithSubBreeds = mAllDogsRecyclerViewAdapter.getBreed(position);
        if (alreadyAdded) {
            mAllDogsFragmentViewModel.deleteDogFromFavourites(breedWithSubBreeds);
        } else {
            mAllDogsFragmentViewModel.persistFavouriteDog(breedWithSubBreeds);
        }
    }
}
