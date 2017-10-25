package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by konradkluz on 28/09/2017.
 */
public class TabAllFragment extends Fragment implements
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
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);
        mSearchView.setIconified(true);

        if (mAllDogsFragmentViewModel.getSearchQuery() != null && !mAllDogsFragmentViewModel.getSearchQuery().isEmpty()) {
            mSearchView.setQuery(mAllDogsFragmentViewModel.getSearchQuery(), false);
            mSearchView.setIconified(false);
        }

        RxSearch.fromSearchView(mSearchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap(query -> {
                    mAllDogsFragmentViewModel.setSearchQuery(query);
                    return Flowable.fromCallable(() -> mBreedWithSubBreeds)
                            .flatMap(breeds -> {
                                List<BreedWithSubBreeds> filtered = filter(query, breeds);
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

    @NonNull
    private List<BreedWithSubBreeds> filter(String query, List<BreedWithSubBreeds> breeds) {
        List<BreedWithSubBreeds> filtered = new ArrayList<>();
        for (BreedWithSubBreeds breed : breeds) {
            if (breed.getBreed().getName().startsWith(query)) {
                filtered.add(breed);
            }
        }
        return filtered;
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
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("gowno", mAllDogsFragmentViewModel.getSearchQuery());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_all, container, false);
        ButterKnife.bind(this, rootView);
        mAllDogsFragmentViewModel = ViewModelProviders.of(this, mFactory).get(AllDogsFragmentViewModel.class);
        if (savedInstanceState != null) {
            String query = savedInstanceState.getString("gowno");
            if (query != null) {
                Log.d(TAG, "onCreateView: ---------");
                mAllDogsFragmentViewModel.setSearchQuery(query);
            }
        }


        Log.d(TAG, "onCreateView: --------------");

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
                mBreedWithSubBreeds = new ArrayList<>();
                Log.e(TAG, "onCreateView: error occurred", response.error);
            } else {
                List<BreedWithSubBreeds> breedWithSubBreeds = (List<BreedWithSubBreeds>) response.data;
                mAllDogsFragmentViewModel.updateDogsFromApiWithFavourites(
                        breedWithSubBreeds,
                        updatedBreeds -> {
                            Log.d(TAG, "onCreateView: updated breeds: " + updatedBreeds);
                            mBreedWithSubBreeds = updatedBreeds;
                            String query = mAllDogsFragmentViewModel.getSearchQuery();
                            if (query != null && !query.isEmpty()) {
                                mAllDogsRecyclerViewAdapter.swapSource(filter(query, mBreedWithSubBreeds));
                            } else {
                                mAllDogsRecyclerViewAdapter.swapSource(updatedBreeds);
                            }
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
            Toast.makeText(getActivity(),
                    getString(R.string.favourite_removed, breedWithSubBreeds.getBreed().getName())
                    , Toast.LENGTH_LONG).show();
        } else {
            mAllDogsFragmentViewModel.persistFavouriteDog(breedWithSubBreeds);
            Toast.makeText(getActivity(),
                    getString(R.string.favourite_added, breedWithSubBreeds.getBreed().getName())
                    , Toast.LENGTH_LONG).show();
        }
    }
}
