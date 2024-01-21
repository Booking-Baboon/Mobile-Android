package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.os.Parcel;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AvailablePeriodUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentAccommodationCreationBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationRequest;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationType;
import com.example.bookingapptim4.domain.models.accommodations.AmenityReference;
import com.example.bookingapptim4.domain.models.accommodations.AvailablePeriod;
import com.example.bookingapptim4.domain.models.accommodations.Location;
import com.example.bookingapptim4.domain.models.shared.TimeSlot;
import com.example.bookingapptim4.domain.models.users.HostReference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import com.example.bookingapptim4.databinding.FragmentAddAvailabilityBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAvailabilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAvailabilityFragment extends Fragment {


    private FragmentAddAvailabilityBinding binding;
    private TextInputLayout priceInput;

    private ChipGroup chipGroup;

    private static final String ACCOMMODATION_PARAM = "accommodation";

    private Accommodation accommodation;

    private List<AvailablePeriod> availablePeriods = new ArrayList<>();

    private List<AvailablePeriod> removedPeriods = new ArrayList<>();

    private AvailablePeriod selectedPeriod;



    public AddAvailabilityFragment() {
        // Required empty public constructor
    }

    public static AddAvailabilityFragment newInstance() {
        AddAvailabilityFragment fragment = new AddAvailabilityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("accommodation")) {
            accommodation = args.getParcelable("accommodation");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddAvailabilityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        availablePeriods = accommodation.getAvailablePeriods();

        loadDateRangePicker(view);

        chipGroup = view.findViewById(R.id.add_availability_chips_av);

        fillChipGroup();

        priceInput = view.findViewById(R.id.add_availability_price_av);






        Button submit = view.findViewById(R.id.submit_changes_button_av);



        MaterialButton addAvailabilityButton = view.findViewById(R.id.add_availability_button_av);
        addAvailabilityButton.setOnClickListener(v -> {
            AvailablePeriod request = parseAvailabilityPeriodInfo(view);
            if(request != null){
                if(selectedPeriod!=null){

                    if(selectedPeriod.getId()!=null){
                        request.setId(selectedPeriod.getId());
                    }
                    availablePeriods.remove(selectedPeriod);

                    availablePeriods.add(request);
                    editChip(selectedPeriod,request);
                }else{
                    availablePeriods.add(request);
                    addChip(request);
                }

            }

            selectedPeriod = null;
            loadDateRangePicker(binding.getRoot());
            priceInput.getEditText().setText("");
            TextView selectedDateRangeTextView = view.findViewById(R.id.add_availability_date_text_view_av);
            selectedDateRangeTextView.setText("");
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for (AvailablePeriod removedPeriod: removedPeriods) {
                    removePeriod(removedPeriod);
                }
                for (AvailablePeriod availablePeriod: availablePeriods) {
                    if(availablePeriod.getId() != null){
                        updatePeriod(availablePeriod);
                    }else{
                        createPeriod(availablePeriod);
                    }

                }
                showSnackbar(view, "Availability successfully updated!");
                Bundle bundle = new Bundle();
                Navigation.findNavController(v).navigate(R.id.nav_accommodations, bundle);

            }
        });



        return view;
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void removePeriod(AvailablePeriod period){
        Call<Accommodation> call = AccommodationUtils.accommodationService.remove(accommodation.getId(),period.getId());
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());


                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    public void updatePeriod(AvailablePeriod period){
        Call<AvailablePeriod> call = AvailablePeriodUtils.availablePeriodService.update(period);
        call.enqueue(new Callback<AvailablePeriod>() {
            @Override
            public void onResponse(Call<AvailablePeriod> call, Response<AvailablePeriod> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());


                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<AvailablePeriod> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void createPeriod(AvailablePeriod period) {
        Call<AvailablePeriod> call = AvailablePeriodUtils.availablePeriodService.create(period);
        call.enqueue(new Callback<AvailablePeriod>() {
            @Override
            public void onResponse(Call<AvailablePeriod> call, Response<AvailablePeriod> response) {
                if (response.code() == 201){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    AvailablePeriod availablePeriod = response.body();
                    addPeriod(availablePeriod);



                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<AvailablePeriod> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void addPeriod(AvailablePeriod period) {
        Call<Accommodation> call = AccommodationUtils.accommodationService.addPeriod(accommodation.getId(),period.getId(), "Bearer " + UserUtils.getCurrentUser().getJwt());
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 201){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());


                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void editChip(AvailablePeriod selectedPeriod, AvailablePeriod request) {
        String chipTextToFind = selectedPeriod.getTimeSlot().getStartDate()+" to "+selectedPeriod.getTimeSlot().getEndDate()+"| Price per night: "+Float.toString(request.getPricePerNight())+"€";
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            View child = chipGroup.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                if (chipTextToFind.equals(chip.getText().toString())) {
                    chipGroup.removeView(chip);
                    addChip(request);
                    break;
                }
            }
        }
    }

    public void addChip(AvailablePeriod request){
        Chip chip = new Chip(new ContextThemeWrapper(getContext(), R.style.Theme_BookingAppTim4));
        chip.setText(request.getTimeSlot().getStartDate()+" to "+request.getTimeSlot().getEndDate()+"| Price per night: "+Float.toString(request.getPricePerNight()) +"€");

        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(request.getId()!=null){
                    removedPeriods.add(request);
                }

                availablePeriods.remove(request);
                chipGroup.removeView(chip);
                selectedPeriod = null;
            }
        });

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPeriod = request;
                loadDateRangePicker(binding.getRoot());
                priceInput.getEditText().setText(Float.toString(request.getPricePerNight()) );
            }
        });

        chipGroup.addView(chip);
    }

    private AvailablePeriod parseAvailabilityPeriodInfo(View view){
        List<String> selectedDates = parseDates(view);
        String checkin = selectedDates.get(0);
        String checkout = selectedDates.get(1);
        String totalPriceText = priceInput.getEditText().getText().toString();

        if(checkin == null || checkin.isEmpty() || checkout == null || checkout.isEmpty() || totalPriceText == null || totalPriceText.isEmpty()){
            return null;
        }

        float totalPrice = Float.parseFloat(totalPriceText);

        return new AvailablePeriod(new TimeSlot(checkin,checkout), totalPrice);
    }

    private List<String> parseDates(View view) {
        String selectedDateRange = getTextFromTextView(view, R.id.add_availability_date_text_view_av);
        String startDate = null;
        String endDate = null;
        if(selectedDateRange != null){
            String[] dateParts = selectedDateRange.split(" to ");
            if (dateParts.length == 2) {
                startDate = dateParts[0];
                endDate = dateParts[1];
            }
        }

        return Arrays.asList(startDate, endDate);
    }

    private String getTextFromTextView(View dialogView, @IdRes int textViewId) {
        TextView textView = dialogView.findViewById(textViewId);
        return textView != null ? textView.getText().toString() : null;
    }

    private void loadDateRangePicker(View view) {
        MaterialButton dateRangePickerButton = view.findViewById(R.id.add_availability_date_range_button_av);
        TextView selectedDateRangeTextView = view.findViewById(R.id.add_availability_date_text_view_av);

        MaterialDatePicker<Pair<Long, Long>> datePicker;

        if(selectedPeriod!=null){
            datePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setCalendarConstraints(buildCalendarConstraints())
                    .setSelection(Pair.create(convertDateStringToMillis(selectedPeriod.getTimeSlot().getStartDate()) +86400000, convertDateStringToMillis(selectedPeriod.getTimeSlot().getEndDate())+86400000))
                    .build();
        }else{
            datePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setCalendarConstraints(buildCalendarConstraints())
                    .build();
        }



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

    private CalendarConstraints buildCalendarConstraints() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                for (AvailablePeriod availablePeriod : availablePeriods) {
                    if(selectedPeriod!=null){
                        if (availablePeriod.equals(selectedPeriod)){
                            continue;
                        }
                    }
                    long startMillis = convertDateStringToMillis(availablePeriod.getTimeSlot().getStartDate());
                    long endMillis = convertDateStringToMillis(availablePeriod.getTimeSlot().getEndDate());

                    if (date >= startMillis && date <= endMillis + 86400000) {
                        return false;
                    }
                }
                return date > new Date().getTime();
            }

            @Override
            public int describeContents() {
                return 0;
            }


            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };

        constraintsBuilder.setValidator(dateValidator);

        return constraintsBuilder.build();
    }

    private long convertDateStringToMillis(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void fillChipGroup(){
        for (AvailablePeriod period: availablePeriods) {
            addChip(period);
        }
    }

}