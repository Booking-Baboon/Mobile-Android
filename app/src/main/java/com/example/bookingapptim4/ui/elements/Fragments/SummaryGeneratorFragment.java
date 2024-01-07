package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.SummaryUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.accommodations.summaries.PeriodSummary;
import com.example.bookingapptim4.domain.models.users.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SummaryGeneratorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryGeneratorFragment extends Fragment {
    public SummaryGeneratorFragment() {
        // Required empty public constructor
    }


    public static SummaryGeneratorFragment newInstance() {
        SummaryGeneratorFragment fragment = new SummaryGeneratorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_generator, container, false);

        loadDateRangePicker(view);

        Button sendRequestButton = view.findViewById(R.id.generateSummaryButton);
        sendRequestButton.setOnClickListener(v -> {
            List<String> selectedDates = parseDates(view);
            String startDate = selectedDates.get(0);
            String endDate = selectedDates.get(1);

            if(startDate != null && endDate != null){
                generateSummary(v, startDate, endDate);
            } else {
                Snackbar.make(view, "You must choose a date range", Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }

        private void generateSummary(View view, String startDate, String endDate) {
        User user =  UserUtils.getCurrentUser();

        Call<PeriodSummary> call = SummaryUtils.summaryService.getPeriodSummary(user.getId(),startDate,endDate, "Bearer " + user.getJwt());
        call.enqueue(new Callback<PeriodSummary>() {
            @Override
            public void onResponse(Call<PeriodSummary> call, Response<PeriodSummary> response) {
                if (response.code() == 200){
                    PeriodSummary periodSummary = response.body();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("periodSummary", periodSummary);

                    Navigation.findNavController(view).navigate(R.id.nav_period_summary, bundle);
                    Log.d("SummaryUtils","Meesage recieved");

                }else{
                    Log.d("SummaryUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<PeriodSummary> call, Throwable t) {
                Log.d("SummaryUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    private void loadDateRangePicker(View view) {
        MaterialButton dateRangePickerButton = view.findViewById(R.id.summaryGeneratorDateRangePickerButton);
        TextView selectedDateRangeTextView = view.findViewById(R.id.summaryGeneratorSelectedDateRangeTextView);

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

    private List<String> parseDates(View view) {
        String selectedDateRange = getTextFromTextView(view, R.id.summaryGeneratorSelectedDateRangeTextView);
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