package com.example.bookingapptim4.ui.state_holders.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.reservations.ReservationUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.reservations.Reservation;
import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostReservationsAdapter extends ArrayAdapter<Reservation> {
    private ArrayList<Reservation> reservations;

    public HostReservationsAdapter(Context context, ArrayList<Reservation> reservations) {
        super(context, R.layout.host_reservation_card, reservations);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.host_reservation_card,
                    parent, false);
        }

        LinearLayout hostReservationCard = convertView.findViewById(R.id.host_reservation_card);
        TextView accommodationName = convertView.findViewById(R.id.host_reservation_accommodation_name);
        TextView status = convertView.findViewById(R.id.host_reservation_status);
        TextView reservationGuest = convertView.findViewById(R.id.host_reservation_guest);
        TextView guestCancellations = convertView.findViewById(R.id.host_reservation_guest_cancellations);
        TextView period = convertView.findViewById(R.id.host_reservation_period);


        if (reservation != null) {
            if ("approved".equalsIgnoreCase(reservation.getStatus().toString())) {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            } else if ("denied".equalsIgnoreCase(reservation.getStatus().toString())) {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            } else {
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }

            accommodationName.setText(reservation.getAccommodation().getName());
            status.setText(reservation.getStatus().toString());
            reservationGuest.setText(reservation.getGuest().getEmail());
            loadCancellations(guestCancellations, reservation.getGuest());

            String periodText = String.format("%s - %s",
                    reservation.getTimeSlot().getStartDate(),
                    reservation.getTimeSlot().getEndDate());
            period.setText(periodText);
        }

        return convertView;
    }

    private void loadCancellations(TextView guestCancellations, Guest guest) {
        User user = UserUtils.getCurrentUser();
        Call<Long> call = ReservationUtils.reservationService.getGuestCancellationCount(guest.getId(),"Bearer " + user.getJwt());
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.code() == 200){
                    Log.d("ReservationUtils","Meesage recieved");
                    System.out.println(response.body());
                    Long cancellationCount = response.body();
                    guestCancellations.setText(String.format("cancellation count: %s", String.valueOf(cancellationCount)));

                }else{
                    Log.d("ReservationUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.d("ReservationUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
