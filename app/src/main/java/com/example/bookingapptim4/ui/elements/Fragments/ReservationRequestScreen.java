package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AvailablePeriod;
import com.example.bookingapptim4.domain.models.shared.TimeSlot;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        return view;
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
}