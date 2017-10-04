package com.believeapps.konradkluz.dogsearcher.viewmodel;

import com.believeapps.konradkluz.dogsearcher.model.entities.Breed;
import com.believeapps.konradkluz.dogsearcher.model.entities.BreedWithSubBreeds;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogLocalRepository;
import com.believeapps.konradkluz.dogsearcher.model.repository.DogRemoteRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by konradkluz on 03/10/2017.
 */

public class AllDogsFragmentViewModelTest {


    private DogLocalRepository mDogLocalRepository;
    private DogRemoteRepository mDogRemoteRepository;
    private AllDogsFragmentViewModel mAllDogsFragmentViewModel;

    @Before
    public void setup() throws Exception {
        mDogLocalRepository = mock(DogLocalRepository.class);
        mDogRemoteRepository = mock(DogRemoteRepository.class);
        mAllDogsFragmentViewModel
                = new AllDogsFragmentViewModel(mDogRemoteRepository, mDogLocalRepository);
    }

    @Test
    public void updateDogListShouldProperlyUpdateListIfRecordExistsInDb() throws Exception {
        //given
//        Flowable<List<BreedWithSubBreeds>> flowable = Flowable.fromCallable(this::prepareDbBreeds);
//        when(mDogLocalRepository.getAllFavourites()).thenReturn(flowable);
//        //and
//        List<BreedWithSubBreeds> breedsToUpdate = prepareApiBreeds();

//        //when
//        List<BreedWithSubBreeds> breedWithSubBreeds = mAllDogsFragmentViewModel.updateDogsFromApiWithFavourites(breedsToUpdate);
//        for (BreedWithSubBreeds breeds : breedWithSubBreeds) {
//            System.out.println(breeds.breed.getName() + " : " + breeds.breed.getId());
//        }
//
//        Thread.sleep(1000);
    }


    private List<BreedWithSubBreeds> prepareApiBreeds() {
        List<BreedWithSubBreeds> breedWithSubBreeds = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            BreedWithSubBreeds breedWithSub = new BreedWithSubBreeds();
            Breed breed = new Breed();
            breed.setName("breed" + i);
            breedWithSub.setBreed(breed);
            breedWithSubBreeds.add(breedWithSub);
        }
        return breedWithSubBreeds;
    }

    private List<BreedWithSubBreeds> prepareDbBreeds() {
        List<BreedWithSubBreeds> breedWithSubBreeds = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            BreedWithSubBreeds breedWithSub = new BreedWithSubBreeds();
            Breed breed = new Breed();
            breed.setName("breed" + i);
            breed.setId((long) (i + 1));
            breedWithSub.setBreed(breed);
            breedWithSubBreeds.add(breedWithSub);
        }
        return breedWithSubBreeds;
    }
}
