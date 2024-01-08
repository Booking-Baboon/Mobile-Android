package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.reports.HostReportUtils;
import com.example.bookingapptim4.data_layer.repositories.reviews.HostReviewUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentHostReportBinding;
import com.example.bookingapptim4.domain.dtos.reports.CreateHostReportRequest;
import com.example.bookingapptim4.domain.dtos.reviews.CreateHostReviewRequest;
import com.example.bookingapptim4.domain.dtos.users.UserReference;
import com.example.bookingapptim4.domain.models.reports.HostReport;
import com.example.bookingapptim4.domain.models.reports.ReportStatus;
import com.example.bookingapptim4.domain.models.reservations.Reservation;
import com.example.bookingapptim4.domain.models.reviews.HostReview;
import com.example.bookingapptim4.ui.state_holders.text_watchers.RequiredFieldTextWatcher;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostReportFragment extends Fragment {

    private static final String RESERVATION_PARAM = "selectedReservation";

    private FragmentHostReportBinding binding;

    private Reservation reservation;

    private TextInputLayout textInputMessage;


    private FragmentManager fragmentManager;

    public HostReportFragment() {
        // Required empty public constructor
    }


    public static HostReportFragment newInstance() {
        HostReportFragment fragment = new HostReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(RESERVATION_PARAM)) {
            reservation = args.getParcelable(RESERVATION_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHostReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        textInputMessage = view.findViewById(R.id.report_host_message);
        textInputMessage.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputMessage));
        Button submitButton = view.findViewById(R.id.host_report_submit_button);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!areFieldsValid()) return;
                CreateHostReportRequest review = new CreateHostReportRequest(new UserReference(UserUtils.getCurrentUser().getId()), todayAsString, ReportStatus.Pending, textInputMessage.getEditText().getText().toString(), new UserReference(reservation.getAccommodation().getHost().getId()) );
                submitReview(review);
            }
        });

        return view;

    }

    private void submitReview(CreateHostReportRequest report) {
        Call<HostReport> call = HostReportUtils.hostReportService.create(report, "Bearer " + UserUtils.getCurrentUser().getJwt());

        call.enqueue(new Callback<HostReport>() {
            @Override
            public void onResponse(Call<HostReport> call, Response<HostReport> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    Toast toast = Toast.makeText(getContext(), "Review submitted!", Toast.LENGTH_LONG);
                    toast.show();
                    fragmentManager.popBackStack();

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                    Toast toast = Toast.makeText(getContext(), "Review already exists!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<HostReport>call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private boolean areFieldsValid() {

        if (textInputMessage.getEditText().length() == 0) {
            textInputMessage.setError("This field is required");
            return false;
        }else{
            textInputMessage.setError(null);
        }

        return true;
    }
}