package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentAccommodationsScreenBinding;
import com.example.bookingapptim4.databinding.FragmentGuestMainScreenBinding;
import com.example.bookingapptim4.databinding.FragmentHostMainScreenBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.example.bookingapptim4.ui.state_holders.adapters.HostAccommodationListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationsScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationsScreen extends Fragment {

    private ArrayList<Accommodation> accommodations = new ArrayList<>();

    ListView accommodationListView;

    private FragmentAccommodationsScreenBinding binding;

    public static AccommodationsScreen newInstance() {
        return new AccommodationsScreen();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAccommodationsScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        accommodationListView = root.findViewById(R.id.host_accommodations_list);

        loadAccommodations(UserUtils.getCurrentUser().getId(), UserUtils.getCurrentUser().getJwt());

        Button button = root.findViewById(R.id.add_accommodation_button);
        button.setOnClickListener(v -> {
            // Handle click on the item at 'position'
            Bundle bundle = new Bundle();

            Navigation.findNavController(v).navigate(R.id.nav_accommodation_creation, bundle);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadAccommodations(Long hostId, String jwt){

        Call<ArrayList<Accommodation>> call = AccommodationUtils.accommodationService.getAllByHost(hostId, "Bearer " + jwt);

        call.enqueue(new Callback<ArrayList<Accommodation>>() {
            @Override
            public void onResponse(Call<ArrayList<Accommodation>> call, Response<ArrayList<Accommodation>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    accommodations = response.body();

                    HostAccommodationListAdapter accommodationListAdapter = new HostAccommodationListAdapter(getActivity(), accommodations);
                    accommodationListView.setAdapter(accommodationListAdapter);
                    accommodationListAdapter.notifyDataSetChanged();

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Accommodation>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }
}