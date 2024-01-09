package com.example.bookingapptim4.ui.state_holders.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.reviews.AccommodationReview;
import com.example.bookingapptim4.domain.models.reviews.Review;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReviewAdapter extends RecyclerView.Adapter<AccommodationReviewAdapter.ViewHolder> {

    private List<AccommodationReview> reviews;

    public AccommodationReviewAdapter(List<AccommodationReview> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accommodation_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccommodationReview review = reviews.get(position);

        loadReviewer(holder, review.getReviewer().getId());
        checkCurrentUser(holder, review);
        holder.textViewAccommodationReviewDate.setText("Date: " + review.getCreatedOn().split("T")[0]);
        holder.textViewAccommodationReviewComment.setText(review.getComment());
        holder.ratingBarAccommodationReview.setIsIndicator(true);
        holder.ratingBarAccommodationReview.setRating(review.getRating());


    }


    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAccommodationReviewer;
        TextView textViewAccommodationReviewDate;
        TextView textViewAccommodationReviewComment;
        RatingBar ratingBarAccommodationReview;
        Button reportReviewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAccommodationReviewer = itemView.findViewById(R.id.textViewAccommodationReviewer);
            textViewAccommodationReviewDate = itemView.findViewById(R.id.textViewAccommodationReviewDate);
            textViewAccommodationReviewComment = itemView.findViewById(R.id.textViewAccommodationReviewComment);
            ratingBarAccommodationReview = itemView.findViewById(R.id.ratingBarAccommodationReview);
            reportReviewButton = itemView.findViewById(R.id.accommodation_report_button);
        }
    }

    private void loadReviewer(ViewHolder holder, Long userId){
        Call<User> call = UserUtils.userService.getById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200){
                    Log.d("UserUtils","Meesage recieved");
                    User reviewer = response.body();
                    holder.textViewAccommodationReviewer.setText(reviewer.getEmail());
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

    private void checkCurrentUser(ViewHolder holder , AccommodationReview review){
        Call<Accommodation> call = AccommodationUtils.accommodationService.getById(review.getReviewedAccommodation().getId());
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 200){
                    Log.d("UserUtils","Meesage recieved");
                    Accommodation accommodation = response.body();
                    if (accommodation.getHost().getId() == UserUtils.getCurrentUser().getId()){
                        holder.reportReviewButton.setVisibility(View.VISIBLE);
                        holder.reportReviewButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("selectedReview", review);

                                Navigation.findNavController(v).navigate(R.id.nav_report_review, bundle);
                            }
                        });
                    }else{
                        holder.reportReviewButton.setVisibility(View.GONE);
                    }
                }else{
                    Log.d("UserUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                Log.d("UserUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }


}
