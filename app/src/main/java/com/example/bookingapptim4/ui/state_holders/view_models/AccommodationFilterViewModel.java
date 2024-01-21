package com.example.bookingapptim4.ui.state_holders.view_models;

import androidx.lifecycle.ViewModel;

import com.example.bookingapptim4.domain.models.accommodations.AccommodationType;

import java.util.ArrayList;

public class AccommodationFilterViewModel extends ViewModel {
    String city;
    String guestNum;
    String priceFrom;
    String priceTo;
    String selectedDateRange;
    ArrayList<String> selectedAccommodationTypes;
    ArrayList<String> selectedAmenities;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGuestNum() {
        return guestNum;
    }

    public void setGuestNum(String guestNum) {
        this.guestNum = guestNum;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }

    public String getSelectedDateRange() {
        return selectedDateRange;
    }

    public void setSelectedDateRange(String selectedDateRange) {
        this.selectedDateRange = selectedDateRange;
    }

    public ArrayList<String> getSelectedAccommodationTypes() {
        return selectedAccommodationTypes;
    }

    public void setSelectedAccommodationTypes(ArrayList<String> selectedAccommodationTypes) {
        this.selectedAccommodationTypes = selectedAccommodationTypes;
    }

    public ArrayList<String> getSelectedAmenities() {
        return selectedAmenities;
    }

    public void setSelectedAmenities(ArrayList<String> selectedAmenities) {
        this.selectedAmenities = selectedAmenities;
    }
}
