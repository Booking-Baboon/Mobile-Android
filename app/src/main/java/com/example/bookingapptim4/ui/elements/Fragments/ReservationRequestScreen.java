package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;

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

        return view;
    }
}