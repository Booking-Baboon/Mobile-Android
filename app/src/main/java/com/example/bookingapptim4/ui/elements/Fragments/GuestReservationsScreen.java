package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

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
import com.example.bookingapptim4.ui.state_holders.adapters.HostAccommodationListAdapter;
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
    private Spinner statusSpinner;

    private GuestReservationsAdapter guestReservationsAdapter;
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

        loadSpinner(root);

        return root;
    }

    private void loadSpinner(View root) {
        statusSpinner = root.findViewById(R.id.guest_reservation_status_spinner);
        reservationsListView = root.findViewById(R.id.guest_reservations_list);

        // Set up the spinner with status options
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.reservation_status_options,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinnerAdapter);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedStatus = (String) parentView.getItemAtPosition(position);
                loadReservations(selectedStatus, root);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                loadReservations("all", root);
            }
        });
    }

    private ArrayList<Reservation> filterReservationsByStatus(ArrayList<Reservation> reservations, String selectedStatus) {
        ArrayList<Reservation> filteredReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getStatus().toString().equalsIgnoreCase(selectedStatus)) {
                filteredReservations.add(reservation);
            }
        }
        return filteredReservations;
    }


    private void loadReservations(String selectedStatus, View view) {
        User user = UserUtils.getCurrentUser();
        Call<ArrayList<Reservation>> call = ReservationUtils.reservationService.getAllForGuest(user.getId(),"Bearer " + user.getJwt());
        call.enqueue(new Callback<ArrayList<Reservation>>() {
            @Override
            public void onResponse(Call<ArrayList<Reservation>> call, Response<ArrayList<Reservation>> response) {
                if (response.code() == 200){
                    Log.d("ReservationUtils","Meesage recieved");
                    System.out.println(response.body());
                    reservations = response.body();

                    if(!"all".equalsIgnoreCase(selectedStatus)){
                        reservations = filterReservationsByStatus(reservations, selectedStatus);
                    }



                    GuestReservationsAdapter guestReservationsAdapter = new GuestReservationsAdapter(getActivity(), reservations);

                    guestReservationsAdapter.setOnReviewHostClickListener(new GuestReservationsAdapter.OnReviewHostButtonClickListener() {


                        @Override
                        public void onReviewHostButtonClick(Reservation reservation) {
                            Bundle bundle = new Bundle();

                            Navigation.findNavController(view).navigate(R.id.nav_review_host, bundle);
                        }
                    });


                    guestReservationsAdapter.setOnReviewAccommodationClickListener(new GuestReservationsAdapter.OnReviewAccommodationButtonClickListener() {


                        @Override
                        public void onReviewAccommodationButtonClick(Reservation reservation) {
                            Bundle bundle = new Bundle();

                            Navigation.findNavController(view).navigate(R.id.nav_review_accommodation, bundle);
                        }
                    });
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