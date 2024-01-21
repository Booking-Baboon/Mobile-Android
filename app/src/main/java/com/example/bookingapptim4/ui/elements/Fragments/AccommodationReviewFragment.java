package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.reviews.AccommodationReviewUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.dtos.accommodations.AccommodationReference;
import com.example.bookingapptim4.domain.dtos.reviews.CreateAccommodationReviewRequest;
import com.example.bookingapptim4.domain.dtos.users.UserReference;
import com.example.bookingapptim4.domain.models.reservations.Reservation;
import com.example.bookingapptim4.databinding.FragmentAccommodationReviewBinding;
import com.example.bookingapptim4.domain.models.reviews.AccommodationReview;
import com.example.bookingapptim4.ui.state_holders.text_watchers.RequiredFieldTextWatcher;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationReviewFragment extends Fragment {

    private static final String RESERVATION_PARAM = "selectedReservation";

    private FragmentAccommodationReviewBinding binding;

    private Reservation reservation;

    private TextInputLayout textInputComment;

    private short rating;


    public AccommodationReviewFragment() {
        // Required empty public constructor
    }


    public static AccommodationReviewFragment newInstance() {
        AccommodationReviewFragment fragment = new AccommodationReviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(RESERVATION_PARAM)) {
            reservation = args.getParcelable(RESERVATION_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccommodationReviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RatingBar ratingBar = view.findViewById(R.id.review_accommodation_rating_bar);
        rating = (short) ratingBar.getRating();
        textInputComment = view.findViewById(R.id.review_accommodation_comment);
        textInputComment.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputComment));
        Button submitButton = view.findViewById(R.id.accommodation_review_submit_button);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!areFieldsValid()) return;
                CreateAccommodationReviewRequest review = new CreateAccommodationReviewRequest(new UserReference(UserUtils.getCurrentUser().getId()), todayAsString, rating, textInputComment.getEditText().getText().toString(), new AccommodationReference(reservation.getAccommodation().getId()) );
                submitReview(review);
            }
        });

        return view;

    }

    private void submitReview(CreateAccommodationReviewRequest review) {
        Call<AccommodationReview> call = AccommodationReviewUtils.accommodationReviewService.create(review, "Bearer " + UserUtils.getCurrentUser().getJwt());

        call.enqueue(new Callback<AccommodationReview>() {
            @Override
            public void onResponse(Call<AccommodationReview> call, Response<AccommodationReview> response) {
                if (response.code() == 201){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    Toast toast = Toast.makeText(getContext(), "Review submitted!", Toast.LENGTH_LONG);
                    toast.show();

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                    Toast toast = Toast.makeText(getContext(), "Review already exists!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<AccommodationReview>call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private boolean areFieldsValid() {

        if (textInputComment.getEditText().length() == 0) {
            textInputComment.setError("This field is required");
            return false;
        }else{
            textInputComment.setError(null);
        }

        return true;
    }
}