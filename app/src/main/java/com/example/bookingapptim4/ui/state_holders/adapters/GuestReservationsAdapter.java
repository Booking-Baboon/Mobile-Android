package com.example.bookingapptim4.ui.state_holders.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.reservations.Reservation;

import java.util.ArrayList;

public class GuestReservationsAdapter extends ArrayAdapter<Reservation> {
    private ArrayList<Reservation> reservations;

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
}
