package com.example.bookingapptim4.UI.Elements.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bookingapptim4.UI.Elements.Accommodations.AccommodationUtils;
import com.example.bookingapptim4.UI.Elements.Adapters.AccommodationListAdapter;
import com.example.bookingapptim4.databinding.FragmentAccommodationListBinding;
import com.example.bookingapptim4.UI.Elements.Models.accommodation_handling.Accommodation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAccommodationList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccommodationList extends ListFragment {

    private AccommodationListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    private FragmentAccommodationListBinding binding;

    public static FragmentAccommodationList newInstance(){
        FragmentAccommodationList fragment = new FragmentAccommodationList();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("BookingBaboon", "onCreateView Accommodation List Fragment");
        binding = FragmentAccommodationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "onCreate Accommodation List Fragment");
        getDataFromAccommodation();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookingBaboon", "onCreate Accommodation List Fragment");

//        if (getArguments() != null) {
//            accommodations = getArguments().getParcelableArrayList(ARG_PARAM);
//            adapter = new AccommodationListAdapter(getActivity(), accommodations);
//            setListAdapter(adapter);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromAccommodation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    private void getDataFromAccommodation(){
        /*
         * Poziv REST servisa se odvija u pozadini i mi ne moramo da vodimo racuna o tome
         * Samo je potrebno da registrujemo sta da se desi kada odgovor stigne od nas
         * Taj deo treba da implementiramo dodavajuci Callback<List<Event>> unutar enqueue metode
         *
         * Servis koji pozivamo izgleda:
         * http://<service_ip_adress>:<service_port>/api/product
         * */
        Call<ArrayList<Accommodation>> call = AccommodationUtils.accommodationService.getAll();
        call.enqueue(new Callback<ArrayList<Accommodation>>() {
            @Override
            public void onResponse(Call<ArrayList<Accommodation>> call, Response<ArrayList<Accommodation>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    accommodations = response.body();
                    System.out.println("Accommodation:");
                    System.out.println(accommodations.get(2).getName());
                    adapter = new AccommodationListAdapter(getActivity(), accommodations);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();

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