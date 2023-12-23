package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

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