package com.example.bookingapptim4.ui.state_holders.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.users.HostUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.reviews.AccommodationReview;
import com.example.bookingapptim4.domain.models.users.Host;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAccommodationReviewer = itemView.findViewById(R.id.textViewAccommodationReviewer);
            textViewAccommodationReviewDate = itemView.findViewById(R.id.textViewAccommodationReviewDate);
            textViewAccommodationReviewComment = itemView.findViewById(R.id.textViewAccommodationReviewComment);
            ratingBarAccommodationReview = itemView.findViewById(R.id.ratingBarAccommodationReview);
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
}
