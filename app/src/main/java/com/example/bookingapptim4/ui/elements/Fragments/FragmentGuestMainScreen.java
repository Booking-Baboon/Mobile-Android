package com.example.bookingapptim4.ui.elements.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.databinding.FragmentGuestMainScreenBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.ui.state_holders.view_models.AccommodationViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGuestMainScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGuestMainScreen extends Fragment{

    public static ArrayList<Accommodation> accommodations = new ArrayList<Accommodation>();
    private AccommodationViewModel accommodationViewModel;
    private FragmentGuestMainScreenBinding binding;

    public static FragmentGuestMainScreen newInstance() {
        return new FragmentGuestMainScreen();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        binding = FragmentGuestMainScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prepareAccommodationsList(accommodations);

        SearchView searchView = binding.searchText;
        accommodationViewModel.getText().observe(getViewLifecycleOwner(), searchView::setQueryHint);

        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), 0);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            Button applyFilterButton = dialogView.findViewById(R.id.apply_filter_button);
            applyFilterButton.setOnClickListener(a ->{
//                Apply filters
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.show();
        });



        Spinner spinner = binding.btnSort;
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sort_array));
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Sort implementation
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        FragmentTransition.to(FragmentAccommodationList.newInstance(accommodations), getActivity(), false, R.id.scroll_accommodations_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareAccommodationsList(ArrayList<Accommodation> accommodations){
        accommodations.add(new Accommodation("Sweet home", "Feels like a home", "Switzerland", 100));
        accommodations.add(new Accommodation("Sweet home", "Feels like a home", "Switzerland", 150));
        accommodations.add(new Accommodation("Sweet home", "Feels like a home", "Switzerland",30));
        accommodations.add(new Accommodation("Sweet home", "Feels like a home", "Switzerland", 80));
    }

}
