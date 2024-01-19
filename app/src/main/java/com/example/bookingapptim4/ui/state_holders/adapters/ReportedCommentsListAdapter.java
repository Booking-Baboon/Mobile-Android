package com.example.bookingapptim4.ui.state_holders.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.reports.ReviewReport;
import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportedCommentsListAdapter extends ArrayAdapter<ReviewReport> {
    private ArrayList<ReviewReport> reviewReports;

    private OnDeleteReviewReportButtonClickListener deleteReviewReportButtonClickListener;

    public interface OnDeleteReviewReportButtonClickListener {
        void onDeleteReviewReportButtonClickListener(ReviewReport reviewReport);
    }

    public void setOnDeleteReviewReportButtonClickListener(OnDeleteReviewReportButtonClickListener listener) {
        this.deleteReviewReportButtonClickListener = listener;
    }

    public ReportedCommentsListAdapter(Context context, ArrayList<ReviewReport> reviewReports) {
        super(context, R.layout.review_report_card, reviewReports);
        this.reviewReports = reviewReports;
    }

    @Override
    public int getCount() {
        if (reviewReports != null) {
            return reviewReports.size();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public ReviewReport getItem(int position) {
        return reviewReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReviewReport reviewReport = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_report_card,
                    parent, false);
        }

        LinearLayout hostReservationCard = convertView.findViewById(R.id.review_report_card);
        TextView reportedItemName = convertView.findViewById(R.id.review_report_reported_item_name);
        TextView reportee = convertView.findViewById(R.id.review_report_reportee);
        TextView message = convertView.findViewById(R.id.review_report_message);
        TextView rating = convertView.findViewById(R.id.review_report_rating);
        TextView reviewer = convertView.findViewById(R.id.review_report_reviewer);
        TextView createdOn = convertView.findViewById(R.id.review_report_created_on);
        Button deleteReviewReport = convertView.findViewById(R.id.review_report_delete_button);


/*        if (reviewReport != null) {
            if ("approved".equalsIgnoreCase(reviewReport.getStatus().toString())) {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            } else if ("denied".equalsIgnoreCase(reviewReport.getStatus().toString())) {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            } else {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }*/

        if(reviewReport.getReportedReview().getReviewedAccommodation().equals(null)) {
            reportedItemName.setText(reviewReport.getReportedReview().getReviewedHost().getEmail());
        } else {
            reportedItemName.setText(reviewReport.getReportedReview().getReviewedAccommodation().getName());
        }
        message.setText(reviewReport.getMessage());
        reportee.setText(reviewReport.getReportee().getEmail());
        createdOn.setText(reviewReport.getCreatedOn().toString());
        rating.setText(String.valueOf(reviewReport.getReportedReview().getRating()));
        reviewer.setText(reviewReport.getReportedReview().getReviewer().getEmail());
        createdOn.setText(reviewReport.getCreatedOn().toString());


            deleteReviewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteReviewReportButtonClickListener != null) {
                        deleteReviewReportButtonClickListener.onDeleteReviewReportButtonClickListener(getItem(position));
                    }
                }
            });

        return convertView;
    }
}
