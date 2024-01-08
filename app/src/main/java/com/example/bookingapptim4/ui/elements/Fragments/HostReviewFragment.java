package com.example.bookingapptim4.ui.elements.Fragments;

import android.content.Intent;
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
import com.example.bookingapptim4.data_layer.repositories.reviews.HostReviewUtils;
import com.example.bookingapptim4.data_layer.repositories.users.GuestUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentAddAvailabilityBinding;
import com.example.bookingapptim4.databinding.FragmentHostReviewBinding;
import com.example.bookingapptim4.domain.dtos.reviews.CreateHostReviewRequest;
import com.example.bookingapptim4.domain.dtos.users.UserReference;
import com.example.bookingapptim4.domain.models.reservations.Reservation;
import com.example.bookingapptim4.domain.models.reviews.HostReview;
import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.ui.elements.Activities.LoginScreen;
import com.example.bookingapptim4.ui.elements.Activities.RegisterScreen;
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
 * Use the {@link HostReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostReviewFragment extends Fragment {

    private static final String RESERVATION_PARAM = "selectedReservation";

    private FragmentHostReviewBinding binding;

    private Reservation reservation;

    private TextInputLayout textInputComment;

    private short rating;

    private FragmentManager fragmentManager;

    public HostReviewFragment() {
        // Required empty public constructor
    }


    public static HostReviewFragment newInstance() {
        HostReviewFragment fragment = new HostReviewFragment();
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
        binding = FragmentHostReviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RatingBar ratingBar = view.findViewById(R.id.review_host_rating_bar);
        rating = (short) ratingBar.getRating();
        textInputComment = view.findViewById(R.id.review_host_comment);
        textInputComment.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputComment));
        Button submitButton = view.findViewById(R.id.host_review_submit_button);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!areFieldsValid()) return;
                CreateHostReviewRequest review = new CreateHostReviewRequest(new UserReference(UserUtils.getCurrentUser().getId()), todayAsString, rating, textInputComment.getEditText().getText().toString(), new UserReference(reservation.getAccommodation().getHost().getId()) );
                submitReview(review);
            }
        });

        return view;

    }

    private void submitReview(CreateHostReviewRequest review) {
        Call<HostReview> call = HostReviewUtils.hostReviewService.create(review, "Bearer " + UserUtils.getCurrentUser().getJwt());

        call.enqueue(new Callback<HostReview>() {
            @Override
            public void onResponse(Call<HostReview> call, Response<HostReview> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    Toast toast = Toast.makeText(getContext(), "Review submitted!", Toast.LENGTH_LONG);
                    toast.show();
                    fragmentManager.popBackStack();

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                    Toast toast = Toast.makeText(getContext(), "Review already exists!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<HostReview>call, Throwable t) {
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