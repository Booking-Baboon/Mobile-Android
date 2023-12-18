package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AmenityUtils;
import com.example.bookingapptim4.data_layer.repositories.users.HostUtils;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;

import com.example.bookingapptim4.domain.models.users.Host;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationDetailsScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationDetailsScreen extends Fragment implements OnMapReadyCallback{
    Accommodation accommodation;
    public AccommodationDetailsScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccommodationDetailsScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationDetailsScreen newInstance() {
        AccommodationDetailsScreen fragment = new AccommodationDetailsScreen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey("selectedAccommodation")) {
            accommodation = args.getParcelable("selectedAccommodation");
            //populate detailed screen ...


        }


    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_accommodation_details_screen, container, false);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodation_details_screen, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById((R.id.maps));
        mapFragment.getMapAsync(this);

        fillViewWithDetails(view);

        return view;
    }

    private void fillViewWithDetails(View view){
        //TITLE
        TextView titleTextView = view.findViewById(R.id.textViewAccommodationTitle);
        titleTextView.setText(accommodation.getName());

        //DESCRIPTION
        TextView descriptionTextView = view.findViewById(R.id.textViewAccommodationDescription);
        descriptionTextView.setText(accommodation.getDescription());

        //LOCATION
        TextView locationTextView = view.findViewById(R.id.textViewAccommodationLocation);
        String locationText = String.format("%s, %s, %s",
                accommodation.getLocation().getCountry(),
                accommodation.getLocation().getCity(),
                accommodation.getLocation().getAddress());
        locationTextView.setText(locationText);

        //GUESTS NUM
        TextView guestNumTextView = view.findViewById(R.id.textViewAccommodationGuestsNum);
        String guestsText = String.format("MIN %s - MAX %s", accommodation.getMinGuests(), accommodation.getMaxGuests());
        guestNumTextView.setText(guestsText);

        //AMENITIES
        TextView amenitiesTextView = view.findViewById(R.id.textViewAccommodationAmenities);
        ArrayList<String> amenityNames = new ArrayList<>();
        for (Amenity amenity : accommodation.getAmenities()) {
            amenityNames.add(amenity.getName());
        }
        amenitiesTextView.setText(TextUtils.join(",", amenityNames));

        //HOST
        loadHost(view);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(46.8182, 8.2275);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Switzerland"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void loadHost(View view){
        Call<Host> call = HostUtils.hostService.getById(accommodation.getHost().getId());
        call.enqueue(new Callback<Host>() {
            @Override
            public void onResponse(Call<Host> call, Response<Host> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    Host host = response.body();
                    TextView hostTextView = view.findViewById(R.id.textViewAccommodationHostUsername);
                    hostTextView.setText(host.getFirstName() + " " +host.getLastName());

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Host> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }
}