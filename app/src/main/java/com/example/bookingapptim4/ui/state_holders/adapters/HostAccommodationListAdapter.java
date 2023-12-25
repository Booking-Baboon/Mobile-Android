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
import androidx.navigation.Navigation;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.reviews.AccommodationReviewUtils;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostAccommodationListAdapter extends ArrayAdapter<Accommodation> {
    private ArrayList<Accommodation> accommodations;
    public HostAccommodationListAdapter(Context context, ArrayList<Accommodation> accommodations) {
        super(context, R.layout.host_accommodation_card, accommodations);
        this.accommodations = accommodations;
    }

    @Override
    public int getCount() {
        if (accommodations != null) {
            return accommodations.size();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public Accommodation getItem(int position) {
        return accommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Accommodation accommodation = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.host_accommodation_card,
                    parent, false);
        }
        LinearLayout accommodationCard = convertView.findViewById(R.id.host_accommodation_card);
        TextView accommodationName = convertView.findViewById(R.id.host_accommodation_title);
        TextView accommodationLocation = convertView.findViewById(R.id.host_accommodation_location);
        TextView accommodationRating = convertView.findViewById(R.id.host_accommodation_rating);
        Button addAvailabilityButton = convertView.findViewById(R.id.add_availability_accommodation_button);


        if (accommodation != null) {
            accommodationName.setText(accommodation.getName());


            String locationText = String.format("%s, %s, %s",
                    accommodation.getLocation().getCountry(),
                    accommodation.getLocation().getCity(),
                    accommodation.getLocation().getAddress());
            accommodationLocation.setText(locationText);

            loadAverageRating(accommodation.getId(),accommodationRating);

            accommodationCard.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedAccommodation", accommodation);

                Navigation.findNavController(v).navigate(R.id.nav_accommodation_details, bundle);
            });

            addAvailabilityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("accommodation", accommodation);

                    Navigation.findNavController(v).navigate(R.id.nav_add_availability, bundle);
                }
            });
        }

        return convertView;
    }

    private void loadAverageRating(Long accommodationId, TextView accommodationRating){
        Call<Float> call = AccommodationReviewUtils.accommodationReviewService.getAverateRating(accommodationId);
        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if (response.code() == 200){
                    Log.d("AccommodationReviewUtils","Meesage recieved");
                    System.out.println(response.body());
                    if(response.body() == -1){
                        accommodationRating.setText("No reviews");
                    }else{
                        accommodationRating.setText(response.body().toString());
                    }

                }else{
                    Log.d("AccommodationReviewUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {
                Log.d("AccommodationReviewUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
