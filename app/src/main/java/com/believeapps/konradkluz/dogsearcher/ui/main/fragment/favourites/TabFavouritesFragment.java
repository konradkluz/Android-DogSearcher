package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter.FavouritesRecyclerViewAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import static java.util.Arrays.asList;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class TabFavouritesFragment extends Fragment implements TabFavouritesView{

    private static final String TAG = "TabFavouritesFragment";

    @Inject
    FavouritesRecyclerViewAdapter mFavouritesRecyclerViewAdapter;

    @BindView(R.id.favourites_dogs_list)
    RecyclerView mRecyclerView;

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFavouritesRecyclerViewAdapter.swapSource(asList("Husky", "Spaniel", "Labrador"));

        mRecyclerView.setAdapter(mFavouritesRecyclerViewAdapter);

        return rootView;
    }
}
