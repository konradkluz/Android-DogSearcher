package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter;


import android.widget.SearchView;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by konradkluz on 09/10/2017.
 */

public class RxSearch {

    public static Flowable<String> fromSearchView(@NonNull final SearchView searchView) {
        final BehaviorProcessor<String> subject = BehaviorProcessor.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subject.onNext(newText);
                return true;
            }
        });

        return subject;
    }
}