package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.reservations.ReservationUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentGuestMainScreenBinding;
import com.example.bookingapptim4.databinding.FragmentGuestReservationsScreenBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.reservations.Reservation;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.example.bookingapptim4.ui.state_holders.adapters.GuestReservationsAdapter;
import com.example.bookingapptim4.ui.state_holders.view_models.AccommodationFilterViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestReservationsScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestReservationsScreen extends Fragment {

    private ArrayList<Reservation> reservations = new ArrayList<>();
    private FragmentGuestReservationsScreenBinding binding;
    ListView reservationsListView;
    public GuestReservationsScreen() {
        // Required empty public constructor
    }

    public static GuestReservationsScreen newInstance() {
        return new GuestReservationsScreen();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGuestReservationsScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reservationsListView = root.findViewById(R.id.guest_reservations_list);
        loadReservations();

        return root;
    }

    private void loadReservations() {
        User user = UserUtils.getCurrentUser();
        Call<ArrayList<Reservation>> call = ReservationUtils.reservationService.getAllForGuest(user.getId(),"Bearer " + user.getJwt());
        call.enqueue(new Callback<ArrayList<Reservation>>() {
            @Override
            public void onResponse(Call<ArrayList<Reservation>> call, Response<ArrayList<Reservation>> response) {
                if (response.code() == 200){
                    Log.d("ReservationUtils","Meesage recieved");
                    System.out.println(response.body());
                    reservations = response.body();

                    GuestReservationsAdapter guestReservationsAdapter = new GuestReservationsAdapter(getActivity(), reservations);
                    reservationsListView.setAdapter(guestReservationsAdapter);
                    guestReservationsAdapter.notifyDataSetChanged();

                }else{
                    Log.d("ReservationUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Reservation>> call, Throwable t) {
                Log.d("ReservationUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}