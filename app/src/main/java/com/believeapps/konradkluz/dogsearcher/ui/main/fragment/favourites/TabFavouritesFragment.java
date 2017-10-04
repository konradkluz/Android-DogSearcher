package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.favourites.adapter.FavouritesRecyclerViewAdapter;
import com.believeapps.konradkluz.dogsearcher.viewmodel.AllDogsFragmentViewModel;
import com.believeapps.konradkluz.dogsearcher.viewmodel.FavouritesViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static java.util.Arrays.asList;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class TabFavouritesFragment extends Fragment implements TabFavouritesView{

    private static final String TAG = "TabFavouritesFragment";

    FavouritesViewModel mFavouritesViewModel;

    @Inject
    ViewModelProvider.Factory mFactory;

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

        mFavouritesViewModel = ViewModelProviders.of(this, mFactory).get(FavouritesViewModel.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFavouritesRecyclerViewAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFavouritesViewModel.getFavouriteDogs(
                favouriteDogs -> mFavouritesRecyclerViewAdapter.swapSource(favouriteDogs),
                error -> Log.e(TAG, "onCreateView: error occurred", error)
        );
    }
}
