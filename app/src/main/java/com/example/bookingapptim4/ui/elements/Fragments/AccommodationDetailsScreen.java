package com.example.bookingapptim4.ui.elements.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AmenityUtils;
import com.example.bookingapptim4.data_layer.repositories.reviews.AccommodationReviewUtils;
import com.example.bookingapptim4.data_layer.repositories.shared.ImageUtils;
import com.example.bookingapptim4.data_layer.repositories.users.GuestUtils;
import com.example.bookingapptim4.data_layer.repositories.users.HostUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;

import com.example.bookingapptim4.domain.models.reviews.AccommodationReview;
import com.example.bookingapptim4.domain.models.shared.Image;
import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.Host;
import com.example.bookingapptim4.domain.models.users.Role;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationReviewAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
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
    ImageCarousel imageCarousel;
    List<CarouselItem> images = new ArrayList<>();
    RatingBar ratingBar;
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

        NestedScrollView nestedScrollView = view.findViewById(R.id.accommodation_details_screen);
        nestedScrollView.setFillViewport(true);

        //MAP
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById((R.id.maps));
        mapFragment.getMapAsync(this);

        //REGISTER CAROUSEL
        imageCarousel = view.findViewById(R.id.accommodationCarousel);
        imageCarousel.registerLifecycle(getLifecycle());

        //REGISER RATING BAR
        ratingBar = view.findViewById(R.id.ratingBarAccommodation);
        ratingBar.setIsIndicator(true);

        //FILL
        fillViewWithDetails(view);

        Button makeReservationButton = view.findViewById(R.id.buttonMakeReservation);
        if(UserUtils.getCurrentUser().getRole() == Role.GUEST && !accommodation.isBeingEdited()){
            makeReservationButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedAccommodation", accommodation);

                Navigation.findNavController(v).navigate(R.id.nav_reservation_request, bundle);
            });
        } else {
            makeReservationButton.setEnabled(false);
        }

        Button hostReviewsButton = view.findViewById(R.id.host_reviews_button);
        hostReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("selectedHost", accommodation.getHost().getId());

                Navigation.findNavController(v).navigate(R.id.nav_host_reviews_list, bundle);
            }
        });


        return view;
    }

    private void fillViewWithDetails(View view){
        //IMAGES
        for(Image image: accommodation.getImages()){
            loadImage(image.getId());
        }

        //TITLE
        TextView titleTextView = view.findViewById(R.id.textViewAccommodationTitle);
        titleTextView.setText(accommodation.getName());

        //AVERAGE RATING
        loadAverageRating(view);

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

        //REVIEWS
        loadReviews(view);

        //FAVORITE BUTTON
        loadFavoriteButton(view);
    }

    private void loadFavoriteButton(View view) {
        User user = UserUtils.getCurrentUser();
        Call<ArrayList<Accommodation>> call = GuestUtils.guestService.getFavorites(user.getId(), "Bearer " + user.getJwt());
        call.enqueue(new Callback<ArrayList<Accommodation>>() {
            @Override
            public void onResponse(Call<ArrayList<Accommodation>> call, Response<ArrayList<Accommodation>> response) {
                if (response.code() == 200){
                    Log.d("GuestUtils","Meesage recieved");
                    ArrayList<Accommodation> favorites = response.body();

                    //Check if accommodation is in favorites
                    boolean isFavorite = false;
                    for (Accommodation favorite : favorites) {
                        if (Objects.equals(favorite.getId(), accommodation.getId())) {
                            isFavorite = true;
                            break;
                        }
                    }

                    setupHeartIcon(view, isFavorite);

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
    private void setupHeartIcon(View view, boolean isFavorite) {
        ImageView heartIcon = view.findViewById(R.id.heartIcon);

        if (isFavorite) {
            heartIcon.setImageResource(R.drawable.ic_filled_heart);
        } else {
            heartIcon.setImageResource(R.drawable.ic_empty_heart);
        }

        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newFavoriteStatus = !isFavorite;

                if (newFavoriteStatus) {
                    heartIcon.setImageResource(R.drawable.ic_filled_heart);
                    addToFavorites();
                } else {
                    heartIcon.setImageResource(R.drawable.ic_empty_heart);
                    removeFromFavorites();
                }
            }
        });
    }
    private void addToFavorites() {
        User user = UserUtils.getCurrentUser();
        Call<Guest> call = GuestUtils.guestService.addFavorite(user.getId(), accommodation.getId(), "Bearer " + user.getJwt());
        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                if (response.code() == 200){
                    Log.d("GuestUtils","Added to favorites");

                }else{
                    Log.d("GuestUtils","Meesage recieved: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<Guest> call, Throwable t) {
                Log.d("GuestUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
    private void removeFromFavorites() {
        User user = UserUtils.getCurrentUser();
        Call<Guest> call = GuestUtils.guestService.removeFavorite(user.getId(), accommodation.getId(), "Bearer " + user.getJwt());
        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                if (response.code() == 200){
                    Log.d("GuestUtils","Removed from favorites");

                }else{
                    Log.d("GuestUtils","Meesage recieved: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<Guest> call, Throwable t) {
                Log.d("GuestUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        // Assuming locationText is in the format "Country, City, Address"
//        String locationText = String.format(
//                "%s, %s, %s",
//                accommodation.getLocation().getCountry(),
//                accommodation.getLocation().getCity(),
//                accommodation.getLocation().getAddress()
//        );

        //TEMP LOCATION
        String locationText = "Serbia, Novi Sad, Bulevar Oslobodjenja 14";


        // Perform geocoding to get latitude and longitude
        Geocoder geocoder = new Geocoder(requireContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationText, 1);

            if (!addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                // Add a marker at the accommodation's location
                LatLng accommodationLocation = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(accommodationLocation).title(accommodation.getName()));

                // Move the camera and zoom to the accommodation's location
                float zoomLevel = 15.0f;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accommodationLocation, zoomLevel));
            } else {
                Log.d("MapUtils", "Geocoding failed, address not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHost(View view){
        Call<Host> call = HostUtils.hostService.getById(accommodation.getHost().getId());
        call.enqueue(new Callback<Host>() {
            @Override
            public void onResponse(Call<Host> call, Response<Host> response) {
                if (response.code() == 200){
                    Log.d("HostUtils","Meesage recieved");
                    Host host = response.body();
                    TextView hostTextView = view.findViewById(R.id.textViewAccommodationHostUsername);
                    hostTextView.setText(host.getFirstName() + " " +host.getLastName());

                }else{
                    Log.d("HostUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Host> call, Throwable t) {
                Log.d("HostUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    private void loadImage(Long imageId){
        Call<ResponseBody> call = ImageUtils.imageService.getById(imageId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){

                    try {
                        Log.d("REZ","Meesage recieved");
                        byte[] imageContent = response.body().bytes();
                        String base64Image = ImageUtils.encodeImage(imageContent);
                        String dataUri = "data:image/png;base64," + base64Image;
                        images.add(new CarouselItem(dataUri));
                        imageCarousel.setData(images);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    Log.d("ImageUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ImageUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void loadReviews(View view){
        Call<ArrayList<AccommodationReview>> call = AccommodationReviewUtils.accommodationReviewService.getAccommodationReviews(accommodation.getId());
        call.enqueue(new Callback<ArrayList<AccommodationReview>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationReview>> call, Response<ArrayList<AccommodationReview>> response) {
                if (response.code() == 200){
                    Log.d("AccommodationReviewUtils","Meesage recieved");
                    ArrayList<AccommodationReview> reviews = response.body();

                    TextView accommodationReviewTextView = view.findViewById(R.id.accommodationReviewTextView);
                    if(!reviews.isEmpty()){
                        //FILL REVIEWS
                        RecyclerView reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
                        AccommodationReviewAdapter reviewAdapter = new AccommodationReviewAdapter(reviews);
                        reviewRecyclerView.setAdapter(reviewAdapter);
                        accommodationReviewTextView.setText("Reviws:");
                    } else {
                        accommodationReviewTextView.setText("No reviews");
                    }

                }else{
                    Log.d("AccommodationReviewUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AccommodationReview>> call, Throwable t) {
                Log.d("AccommodationReviewUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void loadAverageRating(View view){
        Call<Float> call = AccommodationReviewUtils.accommodationReviewService.getAverateRating(accommodation.getId());
        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if (response.code() == 200){
                    Log.d("AccommodationReviewUtils","Meesage recieved");
                    Float averageRating = response.body();

                    if(averageRating != null) {
                        ratingBar.setRating(averageRating);
                    }
                }else{
                    Log.d("AccommodationReviewUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {
                Log.d("AccommodationReviewUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}