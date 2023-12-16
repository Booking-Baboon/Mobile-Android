package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.ui.state_holders.adapters.AmenityListAdapter;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationDetailsScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationDetailsScreen extends Fragment implements OnMapReadyCallback{

    ListView amenitiesListView;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accommodation_details_screen, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById((R.id.maps));
        mapFragment.getMapAsync(this);


        //AMENITIES
        ArrayList<Amenity> amenities = (ArrayList<Amenity>) accommodation.getAmenities();

        amenitiesListView=(ListView)view.findViewById(R.id.amenityList);
//        ArrayAdapter amenityListAdapter = new ArrayAdapter(getActivity(),R.layout.amenity_item,amenities);

        AmenityListAdapter amenityListAdapter=new AmenityListAdapter(getActivity(),amenities);
        amenitiesListView.setAdapter(amenityListAdapter);//sets the adapter for listView

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(46.8182, 8.2275);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Switzerland"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}