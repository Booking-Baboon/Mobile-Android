package com.example.bookingapptim4.ui.state_holders.adapters;

import android.content.Context;
import android.os.Bundle;
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
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.reservations.Reservation;
import com.example.bookingapptim4.domain.models.reservations.ReservationStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GuestReservationsAdapter extends ArrayAdapter<Reservation> {
    private ArrayList<Reservation> reservations;

    private OnReviewHostButtonClickListener reviewHostButtonClickListener;

    private OnReviewAccommodationButtonClickListener reviewAccommodationButtonClickListener;

    public interface OnReviewHostButtonClickListener {
        void onReviewHostButtonClick(Reservation reservation);
    }

    public interface OnReviewAccommodationButtonClickListener {
        void onReviewAccommodationButtonClick(Reservation reservation);
    }

    public GuestReservationsAdapter(Context context, ArrayList<Reservation> reservations) {
        super(context, R.layout.guest_reservation_card, reservations);
        this.reservations = reservations;
    }

    @Override
    public int getCount() {
        if (reservations != null) {
            return reservations.size();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public Reservation getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnReviewHostClickListener(OnReviewHostButtonClickListener listener) {
        this.reviewHostButtonClickListener = listener;
    }

    public void setOnReviewAccommodationClickListener(OnReviewAccommodationButtonClickListener listener) {
        this.reviewAccommodationButtonClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reservation reservation = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.guest_reservation_card,
                    parent, false);
        }

        LinearLayout guestReservationCard = convertView.findViewById(R.id.guest_reservation_card);
        TextView accommodationName = convertView.findViewById(R.id.guest_reservation_accommodation_name);
        TextView status = convertView.findViewById(R.id.guest_reservation_status);
        TextView accommodationHost = convertView.findViewById(R.id.guest_reservation_host);
        TextView period = convertView.findViewById(R.id.guest_reservation_period);


        if (reservation != null) {
            if ("approved".equalsIgnoreCase(reservation.getStatus().toString())) {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            } else if ("denied".equalsIgnoreCase(reservation.getStatus().toString())) {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            } else {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }

            Button reviewHost = convertView.findViewById(R.id.guest_reservation_review_host_button);

            reviewHost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewHostButtonClickListener != null) {
                        reviewHostButtonClickListener.onReviewHostButtonClick(getItem(position));
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("selectedReservation", reservation);

                        Navigation.findNavController(v).navigate(R.id.nav_review_host, bundle);
                    }
                }
            });

            reviewHost.setEnabled(isHostReviewable(reservation));

            Button reviewAccommodation = convertView.findViewById(R.id.guest_reservation_review_accommodation_button);

            reviewAccommodation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewAccommodationButtonClickListener != null) {
                        reviewAccommodationButtonClickListener.onReviewAccommodationButtonClick(getItem(position));
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("selectedReservation", reservation);

                        Navigation.findNavController(v).navigate(R.id.nav_review_accommodation, bundle);
                    }
                }
            });

            reviewAccommodation.setEnabled(isAccommodationReviewable(reservation));

            accommodationName.setText(reservation.getAccommodation().getName());
            status.setText(reservation.getStatus().toString());
            accommodationHost.setText(reservation.getAccommodation().getHost().getEmail());

            String periodText = String.format("%s - %s",
                    reservation.getTimeSlot().getStartDate(),
                    reservation.getTimeSlot().getEndDate());
            period.setText(periodText);
        }

        return convertView;
    }

    private boolean isAccommodationReviewable(Reservation reservation){
        String dateString = reservation.getTimeSlot().getEndDate();
        try{
            Date inputDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            Calendar sevenDaysAgo = Calendar.getInstance();
            sevenDaysAgo.add(Calendar.DAY_OF_MONTH, -7);
            return reservation.getStatus().equals(ReservationStatus.Finished)&&inputDate.after(sevenDaysAgo.getTime());
        }catch (Exception e){
            return false;
        }


    }

    private boolean isHostReviewable(Reservation reservation){
        return reservation.getStatus().equals(ReservationStatus.Finished);
    }
}
