package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.entities.Status;
import com.believeapps.konradkluz.dogsearcher.ui.detail.DogDetailActivity;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter.AllDogsRecyclerViewAdapter;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter.DogViewHolder;
import com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter.RxSearch;
import com.believeapps.konradkluz.dogsearcher.viewmodel.AllDogsFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class TabAllFragment extends Fragment implements TabAllView,
        DogViewHolder.AllDogsItemClickListener {

    private static final String TAG = "TabAllFragment";

    private AllDogsFragmentViewModel mAllDogsFragmentViewModel;

    private List<BreedWithSubBreeds> mBreedWithSubBreeds = new ArrayList<>();

    @Inject
    ViewModelProvider.Factory mFactory;

    @Inject
    AllDogsRecyclerViewAdapter mAllDogsRecyclerViewAdapter;

    @BindView(R.id.all_dogs_list)
    RecyclerView mAllDogs;

    private SearchView mSearchView;

    @Inject
    public TabAllFragment() {
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);
        mSearchView.setIconified(true);


        RxSearch.fromSearchView(mSearchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap(query -> {
                        return Flowable.fromCallable(() -> mBreedWithSubBreeds)
                                .flatMap(breeds -> {
                                    List<BreedWithSubBreeds> filtered = new ArrayList<>();
                                    for (BreedWithSubBreeds breed : breeds) {
                                        if (breed.getBreed().getName().startsWith(query)) {
                                            filtered.add(breed);
                                        }
                                    }
                                    return Flowable.fromCallable(() -> filtered);
                                });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> {
                    Log.d(TAG, "Search View: search text: " + query);
                    mAllDogsRecyclerViewAdapter.swapSource(query);
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        mAllDogsRecyclerViewAdapter.setViewModel(mAllDogsFragmentViewModel);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAllDogsFragmentViewModel.getApiResponse().observe(this, response -> {
            if (response.status == Status.ERROR) {
                mAllDogsRecyclerViewAdapter.swapSource(new ArrayList<>());
                Log.e(TAG, "onCreateView: error occurred", response.error);
            } else {
                List<BreedWithSubBreeds> breedWithSubBreeds = (List<BreedWithSubBreeds>) response.data;
                mAllDogsFragmentViewModel.updateDogsFromApiWithFavourites(
                        breedWithSubBreeds,
                        updatedBreeds -> {
                            Log.d(TAG, "onCreateView: updated breeds: " + updatedBreeds);
                            mBreedWithSubBreeds = updatedBreeds;
                            mAllDogsRecyclerViewAdapter.swapSource(updatedBreeds);
                        },
                        error -> Log.e(TAG, "onCreateView: error occurred: ", error));
                Log.d(TAG, "onCreateView: response succeed: " + response.data);
            }
        });

        if (!mAllDogsFragmentViewModel.isRequestSent()) {
            Log.d(TAG, "onStart: loading dogs from api");
            mAllDogsFragmentViewModel.loadDogs();
        }

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
