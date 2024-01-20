package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.reports.UserReportUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserService;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentReportedUsersScreenBinding;
import com.example.bookingapptim4.domain.models.reports.UserReport;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.domain.models.users.UserStatus;
import com.example.bookingapptim4.ui.state_holders.adapters.ReportedUsersListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportedUsersScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportedUsersScreen extends Fragment {

    private ArrayList<UserReport> userReports = new ArrayList<>();
    private FragmentReportedUsersScreenBinding binding;
    ListView userReportsListView;

    public ReportedUsersScreen() {
        // Required empty public constructor
    }

    public static ReportedUsersScreen newInstance() {
        return new ReportedUsersScreen();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReportedUsersScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userReportsListView = root.findViewById(R.id.reported_users_list);

        loadUserReports(root);

        return root;
    }

    private void loadUserReports(View view) {
        User user = UserUtils.getCurrentUser();
        Call<ArrayList<UserReport>> call = UserReportUtils.userReportService.getAll("Bearer " + user.getJwt());
        call.enqueue(new Callback<ArrayList<UserReport>>() {
            @Override
            public void onResponse(Call<ArrayList<UserReport>> call, Response<ArrayList<UserReport>> response) {
                if (response.code() == 200){
                    Log.d("UserReportUtils","Meesage recieved");
                    System.out.println(response.body());
                    userReports = response.body();

                    ReportedUsersListAdapter reportedUsersListAdapter = new ReportedUsersListAdapter(getActivity(), userReports);

                    reportedUsersListAdapter.setOnBlockUserButtonClickListener(new ReportedUsersListAdapter.OnBlockUserButtonClickListener() {
                        @Override
                        public void onBlockUserButtonClickListener(UserReport userReport) {
                            Call<User> call = UserUtils.userService.block(userReport.getReportedGuest() != null ? userReport.getReportedGuest().getId() : userReport.getReportedHost().getId(),"Bearer " + user.getJwt());
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    reportedUsersListAdapter.updateStatus(userReport.getReportedGuest() != null ? userReport.getReportedGuest().getEmail() : userReport.getReportedHost().getEmail(), UserStatus.Blocked);
                                    showSnackbar(view, "User successfully blocked");
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                        }
                    });

                    reportedUsersListAdapter.setOnUnBlockUserButtonClickListener(new ReportedUsersListAdapter.OnUnBlockUserButtonClickListener() {
                        @Override
                        public void onUnBlockUserButtonClickListener(UserReport userReport) {
                            Call<User> call = UserUtils.userService.unblock(userReport.getReportedGuest() != null ? userReport.getReportedGuest().getId() : userReport.getReportedHost().getId(),"Bearer " + user.getJwt());
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    reportedUsersListAdapter.updateStatus(userReport.getReportedGuest() != null ? userReport.getReportedGuest().getEmail() : userReport.getReportedHost().getEmail(), UserStatus.Active);
                                    showSnackbar(view, "User successfully unblocked");
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                        }
                    });

                    userReportsListView.setAdapter(reportedUsersListAdapter);
                    reportedUsersListAdapter.notifyDataSetChanged();

                }else{
                    Log.d("UserReportUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserReport>> call, Throwable t) {
                Log.d("UserReportUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}