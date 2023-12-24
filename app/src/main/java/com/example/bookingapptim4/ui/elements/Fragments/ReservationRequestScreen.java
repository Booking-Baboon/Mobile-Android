package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcel;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AvailablePeriod;
import com.example.bookingapptim4.domain.models.shared.TimeSlot;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationRequestScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationRequestScreen extends Fragment {

    Accommodation accommodation;

    public ReservationRequestScreen() {
        // Required empty public constructor
    }


    public static ReservationRequestScreen newInstance() {
        ReservationRequestScreen fragment = new ReservationRequestScreen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey("selectedAccommodation")) {
            accommodation = args.getParcelable("selectedAccommodation");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_request_screen, container, false);

        loadDateRangePicker(view);
        setTextToEditText(view, R.id.requestAccommodationInputTextField, accommodation.getName());

        addTextWatchers(view);

        Button sendRequestButton = view.findViewById(R.id.sendRequestButton);
        sendRequestButton.setOnClickListener(v -> {
            //Send request
        });

        return view;
    }

    private void addTextWatchers(View view){
        TextInputEditText guestNumInputTextField = view.findViewById(R.id.requestGuestNumInputTextField);
        TextView selectedDateRangeTextView = view.findViewById(R.id.requestSelectedDateRangeTextView);
        guestNumInputTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                List<String> selectedDates = parseDates(view);
                updateTotalPrice(view, selectedDates.get(0), selectedDates.get(1), editable.toString());
            }
        });

        selectedDateRangeTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                List<String> selectedDates = parseDates(view);
                String guestNum = getTextFromTextView(view, R.id.requestGuestNumInputTextField);
                updateTotalPrice(view, selectedDates.get(0), selectedDates.get(1), guestNum);
            }
        });
    }


    private void loadDateRangePicker(View view) {
        MaterialButton dateRangePickerButton = view.findViewById(R.id.requestDateRangePickerButton);
        TextView selectedDateRangeTextView = view.findViewById(R.id.requestSelectedDateRangeTextView);

        MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setCalendarConstraints(buildCalendarConstraints(accommodation.getAvailablePeriods()))
                .build();

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

    private CalendarConstraints buildCalendarConstraints(List<AvailablePeriod> availablePeriods) {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                for (AvailablePeriod availablePeriod : availablePeriods) {
                    long startMillis = convertDateStringToMillis(availablePeriod.getTimeSlot().getStartDate());
                    long endMillis = convertDateStringToMillis(availablePeriod.getTimeSlot().getEndDate());

                    if (date >= startMillis && date <= endMillis) {
                        return true;
                    }
                }
                return false;
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

    private void updateTotalPrice(View view, String checkin, String checkout, String guestNumText){
        if(guestNumText == null || guestNumText.isEmpty() || checkin == null || checkin.isEmpty() || checkout == null || checkout.isEmpty()){
            setTextToTextView(view, R.id.requestTotalPrice, "0" );
            return;
        }

        Call<Float> call = AccommodationUtils.accommodationService.getTotalPrice( accommodation.getId(), checkin, checkout);
        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if (response.code() == 200){
                    Log.d("AccommodationUtils","Meesage recieved");
                    System.out.println(response.body());
                    Float totalPrice = response.body();
                    int guestNum = Integer.parseInt(guestNumText);

                    if(totalPrice != null && totalPrice > 0 && guestNum > 0){
                        totalPrice = totalPrice * guestNum;
                        setTextToTextView(view, R.id.requestTotalPrice, totalPrice.toString() + "\u20AC");
                    } else {
                        setTextToTextView(view, R.id.requestTotalPrice, "0" );
                    }

                }else{
                    Log.d("AccommodationUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {
                Log.d("AccommodationUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    private List<String> parseDates(View view) {
        String selectedDateRange = getTextFromTextView(view, R.id.requestSelectedDateRangeTextView);
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
}