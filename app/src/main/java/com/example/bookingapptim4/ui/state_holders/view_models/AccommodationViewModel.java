package com.example.bookingapptim4.ui.state_holders.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationViewModel extends ViewModel {
    private final MutableLiveData<String> searchText;
    public AccommodationViewModel(){
        searchText = new MutableLiveData<>();
        searchText.setValue("This is search help!");
    }
    public LiveData<String> getText(){
        return searchText;
    }
}
