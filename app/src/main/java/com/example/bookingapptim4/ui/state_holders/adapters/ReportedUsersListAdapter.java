package com.example.bookingapptim4.ui.state_holders.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.domain.models.reports.UserReport;
import com.example.bookingapptim4.domain.models.reports.UserReport;

import java.util.ArrayList;

public class ReportedUsersListAdapter extends ArrayAdapter<UserReport> {
    private ArrayList<UserReport> userReports;

    private ReportedUsersListAdapter.OnBlockUserButtonClickListener blockUserButtonClickListener;
    private ReportedUsersListAdapter.OnUnBlockUserButtonClickListener unBlockUserButtonClickListener;

    public void updateStatus(UserReport report) {
        for (UserReport userReport : userReports) {
            if (userReport.equals(report)) {
                userReports.remove(report);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public interface OnBlockUserButtonClickListener {
        void onBlockUserButtonClickListener(UserReport userReport);
    }

    public void setOnBlockUserButtonClickListener(ReportedUsersListAdapter.OnBlockUserButtonClickListener listener) {
        this.blockUserButtonClickListener = listener;
    }

    public interface OnUnBlockUserButtonClickListener {
        void onUnBlockUserButtonClickListener(UserReport userReport);
    }

    public void setOnUnBlockUserButtonClickListener(ReportedUsersListAdapter.OnUnBlockUserButtonClickListener listener) {
        this.unBlockUserButtonClickListener = listener;
    }

    public ReportedUsersListAdapter(Context context, ArrayList<UserReport> userReports) {
        super(context, R.layout.user_report_card, userReports);
        this.userReports = userReports;
    }

    @Override
    public int getCount() {
        if (userReports != null) {
            return userReports.size();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public UserReport getItem(int position) {
        return userReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserReport userReport = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_report_card,
                    parent, false);
        }

        LinearLayout hostReservationCard = convertView.findViewById(R.id.user_report_card);
        TextView reportedUser = convertView.findViewById(R.id.user_report_reported_email);
        TextView reportee = convertView.findViewById(R.id.user_report_reportee);
        TextView message = convertView.findViewById(R.id.user_report_message);
        TextView createdOn = convertView.findViewById(R.id.user_report_created_on);
        Button blockUserButton = convertView.findViewById(R.id.user_report_block_button);
        Button unBlockUserButton = convertView.findViewById(R.id.user_report_unblock_button);


/*        if (userReport != null) {
            if ("approved".equalsIgnoreCase(userReport.getStatus().toString())) {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            } else if ("denied".equalsIgnoreCase(userReport.getStatus().toString())) {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            } else {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }*/

        if(userReport.getReportedGuest().equals(null)) {
            reportedUser.setText(userReport.getReportedHost().getEmail());
        } else {
            reportedUser.setText(userReport.getReportedGuest().getEmail());
        }
        message.setText(userReport.getMessage());
        reportee.setText(userReport.getReportee().getEmail());
        createdOn.setText(userReport.getCreatedOn().toString());


        blockUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blockUserButtonClickListener != null) {
                    blockUserButtonClickListener.onBlockUserButtonClickListener(getItem(position));
                }
            }
        });

        unBlockUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unBlockUserButtonClickListener != null) {
                    unBlockUserButtonClickListener.onUnBlockUserButtonClickListener(getItem(position));
                }
            }
        });

        return convertView;
    }
}
