package com.example.bookingapptim4.ui.elements.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.SummaryUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.accommodations.summaries.MonthlySummary;
import com.example.bookingapptim4.domain.models.accommodations.summaries.PeriodSummary;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.ui.state_holders.adapters.PeriodSummaryListAdapter;
import com.github.mikephil.charting.charts.BarChart;

import java.io.IOException;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PeriodSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeriodSummaryFragment extends Fragment {
    private PeriodSummary periodSummary;
    public PeriodSummaryFragment() {
        // Required empty public constructor
    }

    public static PeriodSummaryFragment newInstance() {
        PeriodSummaryFragment fragment = new PeriodSummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey("periodSummary")) {
            periodSummary = args.getParcelable("periodSummary");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_period_summary, container, false);

        TextView periodTextView = view.findViewById(R.id.periodSummaryPeriod);
        String periodText = String.format("%s - %s", periodSummary.getPeriod().getStartDate(), periodSummary.getPeriod().getEndDate());
        periodTextView.setText(periodText);

        //Fill list view
        ListView summaryList = view.findViewById(R.id.periodSummaryListView);
        PeriodSummaryListAdapter periodSummaryListAdapter = new PeriodSummaryListAdapter(getContext(),periodSummary.getAccommodationsData());
        summaryList.setAdapter(periodSummaryListAdapter);
        periodSummaryListAdapter.notifyDataSetChanged();

        setupDownloadButton(view);

        return view;
    }

    private void setupDownloadButton(View view) {
        Button downloadButton = view.findViewById(R.id.periodSummaryDownloadPDFButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadMonthlySummaryPDF();
            }
        });
    }

    private void downloadMonthlySummaryPDF() {
        User user = UserUtils.getCurrentUser();
        Call<ResponseBody> call = SummaryUtils.summaryService.getPeriodSummaryPDF(user.getId(), periodSummary.getPeriod().getStartDate(), periodSummary.getPeriod().getEndDate(), "Bearer " + user.getJwt());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("SummaryUtils","Download successful");
                    savePdf(response.body());
                } else {
                    Log.d("SummaryUtils","Meesage recieved: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("SummaryUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void savePdf(ResponseBody body) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "period_summary.pdf");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");

            Uri uri = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                uri = getActivity().getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            }

            OutputStream outputStream = getActivity().getContentResolver().openOutputStream(uri);

            if (outputStream != null) {
                outputStream.write(body.bytes());
                outputStream.close();

                openPdf(uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openPdf(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}