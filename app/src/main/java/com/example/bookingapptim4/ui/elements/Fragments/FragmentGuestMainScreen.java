package com.example.bookingapptim4.ui.elements.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AmenityUtils;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationType;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.ui.state_holders.view_models.AccommodationViewModel;
import com.example.bookingapptim4.databinding.FragmentGuestMainScreenBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

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
    private ArrayList<Amenity> allAmenities = new ArrayList<>();
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

        //Loading...
        loadAmenities();
        loadAccommodations();

        accommodationListView = root.findViewById(R.id.scroll_accommodations_list);

        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);

            // Fill
            fillAccommodationTypeCheckboxes(dialogView);
            fillAmenitiesCheckboxes(dialogView);
            loadDateRangePicker(dialogView);

            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();

            // Find the close button and set an OnClickListener to dismiss the dialog
            ImageButton btnClose = dialogView.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(closeView -> {
                alertDialog.dismiss();
            });

            Button applySearchFormButton = dialogView.findViewById(R.id.applySearchFormButton);
            applySearchFormButton.setOnClickListener(a -> {
                alertDialog.dismiss();
            });

            Button clearSearchFormButton = dialogView.findViewById(R.id.clearSearchFormButton);
            clearSearchFormButton.setOnClickListener(a -> {
                clearFormFields(dialogView);
            });

            alertDialog.show();
        });



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

    private void loadAmenities(){
        /*
         * Poziv REST servisa se odvija u pozadini i mi ne moramo da vodimo racuna o tome
         * Samo je potrebno da registrujemo sta da se desi kada odgovor stigne od nas
         * Taj deo treba da implementiramo dodavajuci Callback<List<Event>> unutar enqueue metode
         *
         * Servis koji pozivamo izgleda:
         * http://<service_ip_adress>:<service_port>/api/product
         * */
        Call<ArrayList<Amenity>> call = AmenityUtils.amenityService.getAll();
        call.enqueue(new Callback<ArrayList<Amenity>>() {
            @Override
            public void onResponse(Call<ArrayList<Amenity>> call, Response<ArrayList<Amenity>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    allAmenities = response.body();

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Amenity>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    private void fillAccommodationTypeCheckboxes(View view){

        LinearLayout checkboxContainer = view.findViewById(R.id.accommodationTypeCheckboxes);

        // Get the values from the AccommodationType enum
        AccommodationType[] values = AccommodationType.values();

        // Dynamically add CheckBoxes to the LinearLayout
        for (AccommodationType type : values) {
            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setText(type.toString()); // Assuming toString() gives the text representation of the enum
            checkBox.setId(View.generateViewId());
            checkboxContainer.addView(checkBox);
        }
    }

    private void fillAmenitiesCheckboxes(View view){
        // Find the LinearLayout container for amenities checkboxes
        LinearLayout amenitiesCheckboxesContainer = view.findViewById(R.id.amenitiesCheckboxes);

        // Dynamically add CheckBoxes for each amenity
        for (Amenity amenity : allAmenities) {
            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setText(amenity.getName()); // Assuming 'getName()' returns the amenity name
            checkBox.setId(View.generateViewId());
            amenitiesCheckboxesContainer.addView(checkBox);
        }
    }

    private void loadDateRangePicker(View view){
        MaterialButton dateRangePickerButton = view.findViewById(R.id.dateRangePickerButton);
        TextView selectedDateRangeTextView = view.findViewById(R.id.selectedDateRangeTextView);

        MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker().build();

        dateRangePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                datePicker.show(fragmentManager, datePicker.toString());
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                // Handle the selected date range
                String selectedDateRange = datePicker.getHeaderText();
                selectedDateRangeTextView.setText(selectedDateRange);
            }
        });
    }

    // Add this method to clear the form fields
    private void clearFormFields(View dialogView) {
        // Clear text input fields
        TextInputEditText cityEditText = dialogView.findViewById(R.id.cityTextField).findViewById(R.id.cityInputTextField);
        TextInputEditText guestNumEditText = dialogView.findViewById(R.id.guestNumTextField).findViewById(R.id.guestNumInputTextField);
        TextInputEditText priceFromEditText = dialogView.findViewById(R.id.priceFromTextField).findViewById(R.id.priceFromInputTextField);
        TextInputEditText priceToEditText = dialogView.findViewById(R.id.priceToTextField).findViewById(R.id.priceToInputTextField);

        cityEditText.setText("");
        guestNumEditText.setText("");
        priceFromEditText.setText("");
        priceToEditText.setText("");

        // Clear date range
        MaterialButton dateRangePickerButton = dialogView.findViewById(R.id.dateRangePickerButton);
        TextView selectedDateRangeTextView = dialogView.findViewById(R.id.selectedDateRangeTextView);
        dateRangePickerButton.setText("Choose Date Range");
        selectedDateRangeTextView.setText("");

        // Clear checkboxes
        clearCheckboxes(dialogView.findViewById(R.id.accommodationTypeCheckboxes));
        clearCheckboxes(dialogView.findViewById(R.id.amenitiesCheckboxes));

    }


    private void clearCheckboxes(LinearLayout checkboxContainer) {
        int childCount = checkboxContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = checkboxContainer.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                checkBox.setChecked(false);
            }
        }
    }
}
