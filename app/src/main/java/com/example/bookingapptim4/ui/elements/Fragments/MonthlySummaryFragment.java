package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.summaries.MonthlySummary;


public class MonthlySummaryFragment extends Fragment {

    MonthlySummary monthlySummary;

    public MonthlySummaryFragment() {
        // Required empty public constructor
    }

    public static MonthlySummaryFragment newInstance() {
        MonthlySummaryFragment fragment = new MonthlySummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey("monthlySummary")) {
            monthlySummary = args.getParcelable("monthlySummary");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monthly_summary, container, false);
    }
}