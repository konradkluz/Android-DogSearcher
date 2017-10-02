package com.believeapps.konradkluz.dogsearcher.ui.common.fragment.dodd;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.believeapps.konradkluz.dogsearcher.R;
import com.believeapps.konradkluz.dogsearcher.model.entities.Dog;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by konradkluz on 28/09/2017.
 */

public class TabDogOfTheDayFragment extends Fragment implements TabDogOfTheDayView{

    private static final String TAG = "TabDogOfTheDayFragment";

    @BindView(R.id.dog_day_image)
    ImageView mDogImage;

    @BindView(R.id.dog_day_name)
    TextView mDogName;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab_dog_of_the_day, container, false);
        ButterKnife.bind(this, rootView);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Dog dog = (Dog)arguments.getSerializable("dog");
            if (dog != null) {
                Log.d(TAG, "onCreateView: dog found");
                mDogName.setText(dog.getBreedName());

                Picasso.with(getActivity()).load(dog.getImageUrl())
                        .error(R.drawable.ic_image_black_48dp)
                        .placeholder(R.drawable.ic_image_black_48dp)
                        .into(mDogImage);
            }
        }else {
            Log.d(TAG, "onCreateView: dog not sent to fragment");
        }

        return rootView;
    }

}
