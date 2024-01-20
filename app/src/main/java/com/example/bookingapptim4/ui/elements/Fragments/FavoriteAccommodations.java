package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.bookingapptim4.data_layer.repositories.users.GuestUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentFavoriteAccommodationsBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.example.bookingapptim4.ui.state_holders.view_models.AccommodationFilterViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteAccommodations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteAccommodations extends Fragment {
    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    ListView favoriteAccommodationsListView;
    private FragmentFavoriteAccommodationsBinding binding;

    public static FragmentGuestMainScreen newInstance() {
        return new FragmentGuestMainScreen();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteAccommodationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        favoriteAccommodationsListView = root.findViewById(R.id.notifications_list);

        loadFavoriteAccommodations();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void loadFavoriteAccommodations(){
        User user = UserUtils.getCurrentUser();
        Call<ArrayList<Accommodation>> call = GuestUtils.guestService.getFavorites(user.getId(), "Bearer " + user.getJwt());
        call.enqueue(new Callback<ArrayList<Accommodation>>() {
            @Override
            public void onResponse(Call<ArrayList<Accommodation>> call, Response<ArrayList<Accommodation>> response) {
                if (response.code() == 200){
                    Log.d("GuestUtils","Meesage recieved");
                    System.out.println(response.body());
                    accommodations = response.body();

                    AccommodationListAdapter accommodationListAdapter = new AccommodationListAdapter(getActivity(), accommodations);
                    favoriteAccommodationsListView.setAdapter(accommodationListAdapter);
                    accommodationListAdapter.notifyDataSetChanged();

                }else{
                    Log.d("GuestUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Accommodation>> call, Throwable t) {
                Log.d("GuestUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}