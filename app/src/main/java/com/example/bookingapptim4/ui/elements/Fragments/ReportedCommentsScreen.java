package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.reports.ReviewReportUtils;
import com.example.bookingapptim4.data_layer.repositories.reservations.ReservationUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentReportedCommentsScreenBinding;
import com.example.bookingapptim4.domain.models.reports.ReviewReport;
import com.example.bookingapptim4.domain.models.reservations.Reservation;
import com.example.bookingapptim4.domain.models.reservations.ReservationStatus;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.ui.state_holders.adapters.HostReservationsAdapter;
import com.example.bookingapptim4.ui.state_holders.adapters.ReportedCommentsListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportedCommentsScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportedCommentsScreen extends Fragment {

    private ArrayList<ReviewReport> reviewReports = new ArrayList<>();
    private FragmentReportedCommentsScreenBinding binding;
    ListView reviewReportsListView;

    public ReportedCommentsScreen() {
        // Required empty public constructor
    }

    public static ReportedCommentsScreen newInstance() {
        return new ReportedCommentsScreen();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReportedCommentsScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reviewReportsListView = root.findViewById(R.id.reported_comments_list);

        loadReviewReports(root);

        return root;
    }

    private void loadReviewReports(View view) {
        User user = UserUtils.getCurrentUser();
        Call<ArrayList<ReviewReport>> call = ReviewReportUtils.reviewReportService.getAll("Bearer " + user.getJwt());
        call.enqueue(new Callback<ArrayList<ReviewReport>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewReport>> call, Response<ArrayList<ReviewReport>> response) {
                if (response.code() == 200){
                    Log.d("ReviewReportUtils","Meesage recieved");
                    System.out.println(response.body());
                    reviewReports = response.body();

                    ReportedCommentsListAdapter reportedCommentsListAdapter = new ReportedCommentsListAdapter(getActivity(), reviewReports);

                    reportedCommentsListAdapter.setOnDeleteReviewReportButtonClickListener(new ReportedCommentsListAdapter.OnDeleteReviewReportButtonClickListener() {
                        @Override
                        public void onDeleteReviewReportButtonClickListener(ReviewReport reservation) {

                        }
                    });

                    reviewReportsListView.setAdapter(reportedCommentsListAdapter);
                    reportedCommentsListAdapter.notifyDataSetChanged();

                }else{
                    Log.d("ReviewReportUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReviewReport>> call, Throwable t) {
                Log.d("ReviewReportUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}