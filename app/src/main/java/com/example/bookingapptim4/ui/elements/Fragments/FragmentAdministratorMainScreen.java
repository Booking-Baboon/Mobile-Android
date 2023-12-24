package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AmenityUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentGuestMainScreenBinding;
import com.example.bookingapptim4.databinding.FragmentHostMainScreenBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationFilter;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationType;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;
import com.example.bookingapptim4.domain.models.users.Host;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.example.bookingapptim4.ui.state_holders.view_models.AccommodationFilterViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHostMainScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdministratorMainScreen extends Fragment {

    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    private ArrayList<Amenity> allAmenities = new ArrayList<>();
    ListView accommodationListView;
    private AccommodationFilterViewModel accommodationFilterViewModel;
    private FragmentHostMainScreenBinding binding;
    private Bundle dialogInstanceState;

    public static FragmentHostMainScreen newInstance() {
        return new FragmentHostMainScreen();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accommodationFilterViewModel = new ViewModelProvider(this).get(AccommodationFilterViewModel.class);

        binding = FragmentHostMainScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        accommodationListView = root.findViewById(R.id.host_scroll_accommodations_list);

        //Loading...
        loadAmenities();
        searchAccommodations();

        //Define filter dialog
        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);

            // Fill filter dialog
            fillAccommodationTypeCheckboxes(dialogView);
            fillAmenitiesCheckboxes(dialogView);
            loadDateRangePicker(dialogView);

            //Restore
            restoreState(dialogView, dialogInstanceState);

            if(dialogInstanceState == null){
                dialogInstanceState = new Bundle();
            }

            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();

            //Add border radius
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.border_radius_background);
            }


            ImageButton btnClose = dialogView.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(closeView -> {
                updateAccommodationFilterViewModel(dialogView);
                saveState(dialogInstanceState);
                alertDialog.dismiss();
            });

            Button applySearchFormButton = dialogView.findViewById(R.id.applySearchFormButton);
            applySearchFormButton.setOnClickListener(a -> {
                updateAccommodationFilterViewModel(dialogView);
                saveState(dialogInstanceState);
                searchAccommodations();
                alertDialog.dismiss();
            });

            Button clearSearchFormButton = dialogView.findViewById(R.id.clearSearchFormButton);
            clearSearchFormButton.setOnClickListener(a -> {
                updateAccommodationFilterViewModel(dialogView);
                saveState(dialogInstanceState);
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

    private void searchAccommodations(AccommodationFilter filter){

        Call<ArrayList<Accommodation>> call = AccommodationUtils.accommodationService.search( filter.getCity(),
                filter.getCheckin(),
                filter.getCheckout(),
                (filter.getGuestNum() != null) ? filter.getGuestNum() : null,
                (filter.getMinPrice() != null) ? filter.getMinPrice() : null,
                (filter.getMaxPrice() != null) ? filter.getMaxPrice() : null,
                (filter.getAmenities() != null && !filter.getAmenities().isEmpty()) ? TextUtils.join(",", filter.getAmenities()) : null,
                (filter.getTypes() != null && !filter.getTypes().isEmpty()) ? TextUtils.join(",", filter.getTypes()) : null,
                (filter.getMinRating() != null) ? filter.getMinRating() : null);
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

        AccommodationType[] values = AccommodationType.values();

        for (AccommodationType type : values) {
            MaterialCheckBox checkBox = new MaterialCheckBox(requireContext());
            checkBox.setText(type.toString());
            checkBox.setId(View.generateViewId());
            checkboxContainer.addView(checkBox);
        }
    }

    private void fillAmenitiesCheckboxes(View view){
        LinearLayout amenitiesCheckboxesContainer = view.findViewById(R.id.amenitiesCheckboxes);

        for (Amenity amenity : allAmenities) {
            MaterialCheckBox checkBox = new MaterialCheckBox(requireContext());
            checkBox.setText(amenity.getName());
            checkBox.setId(View.generateViewId());
            amenitiesCheckboxesContainer.addView(checkBox);
        }
    }

    private void loadDateRangePicker(View view) {
        MaterialButton dateRangePickerButton = view.findViewById(R.id.dateRangePickerButton);
        TextView selectedDateRangeTextView = view.findViewById(R.id.selectedDateRangeTextView);

        MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker().build();

        dateRangePickerButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            datePicker.show(fragmentManager, datePicker.toString());
        });

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            long startDateMillis = selection.first;
            long endDateMillis = selection.second;

            String startDate = dateFormat.format(new Date(startDateMillis));
            String endDate = dateFormat.format(new Date(endDateMillis));

            String selectedDateRange = startDate + " to " + endDate;

            dateRangePickerButton.setText("Change Date Range");
            selectedDateRangeTextView.setText(selectedDateRange);
        });
    }

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

    private void searchAccommodations() {
        String selectedDateRange = accommodationFilterViewModel.getSelectedDateRange();
        String startDate = null;
        String endDate = null;
        if(selectedDateRange != null){
            String[] dateParts = selectedDateRange.split(" to ");
            if (dateParts.length == 2) {
                startDate = dateParts[0];
                endDate = dateParts[1];
            }
        }

        performSearch(accommodationFilterViewModel.getCity(),
                accommodationFilterViewModel.getGuestNum(),
                accommodationFilterViewModel.getPriceFrom(),
                accommodationFilterViewModel.getPriceTo(),
                startDate,
                endDate,
                convertToAccommodationTypeList(accommodationFilterViewModel.getSelectedAccommodationTypes()),
                accommodationFilterViewModel.getSelectedAmenities());
    }


    private void performSearch(String city, String guestNum, String priceFrom, String priceTo, String startDate, String endDate,
                               ArrayList<AccommodationType> accommodationTypes, ArrayList<String> amenities) {

        AccommodationFilter filter = new AccommodationFilter();

        if (city != null && !city.isEmpty()) {
            filter.setCity(city);
        }

        if (startDate != null && endDate != null) {
            filter.setCheckin(startDate);
            filter.setCheckout(endDate);
        }

        if (guestNum != null && !guestNum.isEmpty()) {
            filter.setGuestNum(Integer.parseInt(guestNum));
        }

        if (priceFrom != null && !priceFrom.isEmpty() && priceTo != null && !priceTo.isEmpty()) {
            filter.setMinPrice(Double.parseDouble(priceFrom));
            filter.setMaxPrice(Double.parseDouble(priceTo));
        }

        if (amenities != null) {
            filter.setAmenities(amenities);
        }

        if (accommodationTypes != null) {
            filter.setTypes(accommodationTypes);
        }

        searchAccommodations(filter);
    }


    private ArrayList<AccommodationType> convertToAccommodationTypeList(ArrayList<String> typeStrings) {
        ArrayList<AccommodationType> accommodationTypes = new ArrayList<>();
        if (typeStrings != null) {
            for (String typeString : typeStrings) {
                AccommodationType type = AccommodationType.valueOf(typeString);
                accommodationTypes.add(type);
            }
        }
        return accommodationTypes;
    }

    //SAVE AND RESTORE STATE LOGIC
    private void updateAccommodationFilterViewModel(View dialogView) {
        accommodationFilterViewModel.setCity(getTextFromEditText(dialogView, R.id.cityInputTextField));
        accommodationFilterViewModel.setGuestNum(getTextFromEditText(dialogView, R.id.guestNumInputTextField));
        accommodationFilterViewModel.setPriceFrom(getTextFromEditText(dialogView, R.id.priceFromInputTextField));
        accommodationFilterViewModel.setPriceTo(getTextFromEditText(dialogView, R.id.priceToInputTextField));
        accommodationFilterViewModel.setSelectedDateRange(getTextFromTextView(dialogView, R.id.selectedDateRangeTextView));

        ArrayList<String> selectedAccommodationTypes = getSelectedCheckboxes(dialogView, R.id.accommodationTypeCheckboxes);
        accommodationFilterViewModel.setSelectedAccommodationTypes(selectedAccommodationTypes);

        ArrayList<String> selectedAmenities = getSelectedCheckboxes(dialogView, R.id.amenitiesCheckboxes);
        accommodationFilterViewModel.setSelectedAmenities(selectedAmenities);
    }
    private void saveState(Bundle state) {
        if (state != null) {
            state.putString("city", accommodationFilterViewModel.getCity());
            state.putString("guestNum", accommodationFilterViewModel.getGuestNum());
            state.putString("priceFrom", accommodationFilterViewModel.getPriceFrom());
            state.putString("priceTo", accommodationFilterViewModel.getPriceTo());
            state.putString("selectedDateRange", accommodationFilterViewModel.getSelectedDateRange());
            state.putStringArrayList("selectedAccommodationTypes", accommodationFilterViewModel.getSelectedAccommodationTypes());
            state.putStringArrayList("selectedAmenities", accommodationFilterViewModel.getSelectedAmenities());
        }
    }
    private void restoreState(View view, Bundle state) {
        if (state != null) {
            setTextToEditText(view, R.id.cityInputTextField, state.getString("city", ""));
            setTextToEditText(view, R.id.guestNumInputTextField, state.getString("guestNum", ""));
            setTextToEditText(view, R.id.priceFromInputTextField, state.getString("priceFrom", ""));
            setTextToEditText(view, R.id.priceToInputTextField, state.getString("priceTo", ""));
            setTextToTextView(view, R.id.selectedDateRangeTextView, state.getString("selectedDateRange", ""));
            setSelectedCheckboxes(view, R.id.accommodationTypeCheckboxes, state.getStringArrayList("selectedAccommodationTypes"));
            setSelectedCheckboxes(view, R.id.amenitiesCheckboxes, state.getStringArrayList("selectedAmenities"));
        }
    }
    private void setTextToEditText(View dialogView, @IdRes int editTextId, String text) {
        TextInputEditText editText = dialogView.findViewById(editTextId);
        if (editText != null) {
            editText.setText(text);
        }
    }
    private void setTextToTextView(View dialogView, @IdRes int textViewId, String text) {
        TextView textView = dialogView.findViewById(textViewId);
        if (textView != null) {
            textView.setText(text);
        }
    }
    private void setSelectedCheckboxes(View view, int checkboxesContainerId, ArrayList<?> selectedValues) {
        LinearLayout checkboxContainer = view.findViewById(checkboxesContainerId);

        for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
            View childView = checkboxContainer.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                Object value = checkBox.getText().toString();

                boolean isSelected = selectedValues != null && selectedValues.contains(value);

                checkBox.setChecked(isSelected);
            }
        }
    }
    private String getTextFromEditText(View dialogView, @IdRes int editTextId) {
        TextInputEditText editText = dialogView.findViewById(editTextId);
        return editText != null ? editText.getText().toString() : null;
    }
    private String getTextFromTextView(View dialogView, @IdRes int textViewId) {
        TextView textView = dialogView.findViewById(textViewId);
        return textView != null ? textView.getText().toString() : null;
    }
    private ArrayList<String> getSelectedCheckboxes(View view, @IdRes int checkboxesContainerId) {
        ArrayList<String> selectedValues = new ArrayList<>();
        LinearLayout checkboxContainer = view.findViewById(checkboxesContainerId);

        for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
            View childView = checkboxContainer.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                if (checkBox.isChecked()) {
                    selectedValues.add(checkBox.getText().toString());
                }
            }
        }

        return selectedValues;
    }





}