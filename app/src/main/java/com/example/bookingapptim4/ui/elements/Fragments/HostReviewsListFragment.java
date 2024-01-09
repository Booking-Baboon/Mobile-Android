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
import com.example.bookingapptim4.data_layer.repositories.reviews.HostReviewUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentHostReviewsListBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.reviews.AccommodationReview;
import com.example.bookingapptim4.domain.models.reviews.HostReview;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationReviewAdapter;
import com.example.bookingapptim4.ui.state_holders.adapters.HostAccommodationListAdapter;
import com.example.bookingapptim4.ui.state_holders.adapters.HostReviewListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostReviewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostReviewsListFragment extends Fragment {

    private static final String HOST_PARAM = "selectedHost";

    private Long hostId;

    private List<HostReview> reviews = new ArrayList<>();

    ListView reviewsListView;

    private FragmentHostReviewsListBinding binding;

    public static HostReviewsListFragment newInstance() {
        HostReviewsListFragment fragment = new HostReviewsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(HOST_PARAM)) {
            hostId = args.getLong(HOST_PARAM);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHostReviewsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reviewsListView = root.findViewById(R.id.review_list);

        loadReviews(hostId, UserUtils.getCurrentUser().getJwt(), root);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadReviews(Long hostId, String jwt, View view){

        Call<List<HostReview>> call = HostReviewUtils.hostReviewService.getAllByHost(hostId, "Bearer " + jwt);

        call.enqueue(new Callback<List<HostReview>>() {
            @Override
            public void onResponse(Call<List<HostReview>> call, Response<List<HostReview>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    reviews = response.body();

                    HostReviewListAdapter hostReviewListAdapter = new HostReviewListAdapter(getActivity(), reviews);

                    reviewsListView.setAdapter(hostReviewListAdapter);
                    hostReviewListAdapter.notifyDataSetChanged();

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<HostReview>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

}