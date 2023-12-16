package com.example.bookingapptim4.ui.elements.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.ui.state_holders.view_models.AccommodationViewModel;
import com.example.bookingapptim4.databinding.FragmentGuestMainScreenBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGuestMainScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGuestMainScreen extends Fragment{

    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    ListView accommodationListView;
    private AccommodationViewModel accommodationViewModel;
    private FragmentGuestMainScreenBinding binding;

    public static FragmentGuestMainScreen newInstance() {
        return new FragmentGuestMainScreen();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        binding = FragmentGuestMainScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        accommodationListView = root.findViewById(R.id.scroll_accommodations_list);


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

        //Loading accommodations
        loadAccommodations();


//        FragmentTransition.to(FragmentAccommodationList.newInstance(), getActivity(), false, R.id.scroll_accommodations_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadAccommodations(){
        /*
         * Poziv REST servisa se odvija u pozadini i mi ne moramo da vodimo racuna o tome
         * Samo je potrebno da registrujemo sta da se desi kada odgovor stigne od nas
         * Taj deo treba da implementiramo dodavajuci Callback<List<Event>> unutar enqueue metode
         *
         * Servis koji pozivamo izgleda:
         * http://<service_ip_adress>:<service_port>/api/product
         * */
        Call<ArrayList<Accommodation>> call = AccommodationUtils.accommodationService.getAll();
        call.enqueue(new Callback<ArrayList<Accommodation>>() {
            @Override
            public void onResponse(Call<ArrayList<Accommodation>> call, Response<ArrayList<Accommodation>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    accommodations = response.body();

                    AccommodationListAdapter accommodationListAdapter = new AccommodationListAdapter(getActivity(), accommodations);
                    accommodationListView.setAdapter(accommodationListAdapter);
                    accommodationListAdapter.notifyDataSetChanged();

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Accommodation>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

}
