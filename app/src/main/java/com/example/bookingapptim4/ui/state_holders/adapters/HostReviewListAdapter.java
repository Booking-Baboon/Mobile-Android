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
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.reviews.HostReview;
import com.example.bookingapptim4.domain.models.users.Host;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostReviewListAdapter extends ArrayAdapter<HostReview> {

    private List<HostReview> hostReviews;

    public HostReviewListAdapter(@NonNull Context context, @NonNull List<HostReview> hostReviews) {
        super(context, R.layout.host_review_card, hostReviews);
        this.hostReviews = hostReviews;
    }


    @Override
    public int getCount() {
        if (hostReviews != null) {
            return hostReviews.size();
        } else {
            return 0;
        }
    }

    @Override
    public HostReview getItem(int position) {
        return hostReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        HostReview hostReview = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.host_review_card,
                    parent, false);
        }
        TextView reviewer = convertView.findViewById(R.id.host_review_reviewer_text_view);
        TextView reviewDate = convertView.findViewById(R.id.host_review_date);
        TextView comment = convertView.findViewById(R.id.host_review_message);
        RatingBar ratingBar = convertView.findViewById(R.id.host_review_rating_bar);
        Button button = convertView.findViewById(R.id.host_review_report_button);


        if (hostReview != null) {
            loadReviewer(reviewer,hostReview.getReviewer().getId());
            reviewDate.setText("Date: " + hostReview.getCreatedOn().split("T")[0]);
            comment.setText(hostReview.getComment());
            ratingBar.setIsIndicator(true);
            ratingBar.setRating(hostReview.getRating());

            if (hostReview.getReviewedHost().getId() == UserUtils.getCurrentUser().getId()){
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("selectedReview", hostReview);

                        Navigation.findNavController(v).navigate(R.id.nav_report_review, bundle);
                    }
                });
                button.setVisibility(View.VISIBLE);
            }else{
                button.setVisibility(View.GONE);
            }


        }

        return convertView;
    }

    private void loadReviewer(TextView reviewerTextView, Long userId){
        Call<User> call = UserUtils.userService.getById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200){
                    Log.d("UserUtils","Meesage recieved");
                    User reviewer = response.body();
                    reviewerTextView.setText(reviewer.getEmail());
                }else{
                    Log.d("UserUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("UserUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

}
