package com.example.bookingapptim4.ui.elements.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.summaries.MonthlySummary;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MonthlySummaryFragment extends Fragment {
    private MonthlySummary monthlySummary;
    private BarChart reservationChart;
    private BarChart profitChart;

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
        View view = inflater.inflate(R.layout.fragment_monthly_summary, container, false);

        // Find chart views
        reservationChart = view.findViewById(R.id.monthlySummaryReservationChart);
        profitChart = view.findViewById(R.id.monthlySummaryProfitChart);

        // Populate charts
        populateReservationChart();
        populateProfitChart();

        TextView periodTextView = view.findViewById(R.id.monthlySummaryPeriod);
        String periodText = String.format("%s - %s", monthlySummary.getTimeSlot().getStartDate(), monthlySummary.getTimeSlot().getEndDate());
        periodTextView.setText(periodText);

        return view;
    }

    private void populateReservationChart() {
        Map<Month, Integer> reservationsData = monthlySummary.getReservationsData();
        List<BarEntry> entries = new ArrayList<>();

        int i = 0;
        List<String> monthNames = new ArrayList<>();

        for (Map.Entry<Month, Integer> entry : reservationsData.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue()));
            monthNames.add(entry.getKey().toString());
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Reservations");
        dataSet.setColor(Color.rgb(75, 192, 192));

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);

        reservationChart.getDescription().setEnabled(false);

        reservationChart.getAxisLeft().setGranularity(1f);
        reservationChart.getAxisLeft().setValueFormatter(new DefaultValueFormatter(0));
        reservationChart.getAxisRight().setGranularity(1f);
        reservationChart.getAxisRight().setValueFormatter(new DefaultValueFormatter(0));

        XAxis xAxis = reservationChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthNames));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Add a label on top of the graph
        Description description = new Description();
        description.setText("Reservations Chart");
        description.setTextSize(14f);
        description.setTextColor(Color.BLACK);
        description.setPosition(0.5f, 0.05f); // Adjust position as needed (0.5f, 0.05f is centered and slightly above)
        reservationChart.setDescription(description);

        reservationChart.setData(data);
        reservationChart.invalidate();
    }

    private void populateProfitChart() {
        Map<Month, Double> profitData = monthlySummary.getProfitData();
        List<BarEntry> entries = new ArrayList<>();

        int i = 0;
        List<String> monthNames = new ArrayList<>();

        for (Map.Entry<Month, Double> entry : profitData.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue().floatValue()));
            monthNames.add(entry.getKey().toString()); // Assuming Month enum has a reasonable toString() method
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Profit");
        dataSet.setColor(Color.rgb(236, 105, 193));

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);

        profitChart.getDescription().setEnabled(false);

        XAxis xAxis = profitChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthNames));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Add a label on top of the graph
        Description description = new Description();
        description.setText("Profit Chart");
        description.setTextSize(14f);
        description.setTextColor(Color.BLACK);
        description.setPosition(0.5f, 0.05f); // Adjust position as needed (0.5f, 0.05f is centered and slightly above)
        profitChart.setDescription(description);

        profitChart.setData(data);
        profitChart.invalidate();
    }
}