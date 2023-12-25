package com.example.bookingapptim4.ui.elements.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationModificationUtils;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.shared.ConfirmationDialog;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentAccommodationDetailsScreenBinding;
import com.example.bookingapptim4.databinding.FragmentAccommodationsApprovalScreenBinding;
import com.example.bookingapptim4.databinding.FragmentGuestMainScreenBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationModification;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationModificationStatus;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationModificationType;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationModificationListAdapter;
import com.example.bookingapptim4.ui.state_holders.adapters.HostAccommodationListAdapter;
import com.example.bookingapptim4.ui.state_holders.view_models.AccommodationFilterViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationsApprovalScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationsApprovalScreen extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ArrayList<AccommodationModification> accommodationModifications = new ArrayList<>();
    ListView accommodationListView;
    private FragmentAccommodationsApprovalScreenBinding binding;
    private Bundle dialogInstanceState;

    private AccommodationModificationListAdapter accommodationListAdapter;

    public AccommodationsApprovalScreen() {
        // Required empty public constructor
    }
    public static AccommodationsApprovalScreen newInstance(String param1, String param2) {
        AccommodationsApprovalScreen fragment = new AccommodationsApprovalScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAccommodationsApprovalScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        accommodationListView = root.findViewById(R.id.accommodation_modifications_list);

        Call<ArrayList<AccommodationModification>> call = AccommodationModificationUtils.accommodationModificationService.getAll("Bearer " + UserUtils.getCurrentUser().getJwt());
                call.enqueue(new Callback<ArrayList<AccommodationModification>>() {
                    @Override
                    public void onResponse(Call<ArrayList<AccommodationModification>> call, Response<ArrayList<AccommodationModification>> response) {
                        if (response.code() == 200){
                            Log.d("REZ","Meesage recieved");
                            System.out.println(response.body());
                            accommodationModifications = response.body();

                            accommodationListAdapter = new AccommodationModificationListAdapter(getActivity(), accommodationModifications);
                            accommodationListAdapter.setOnApproveButtonClickListener(new AccommodationModificationListAdapter.OnApproveButtonClickListener() {
                                @Override
                                public void onApproveButtonClick(AccommodationModification modification) {
                                    // Handle the approve button click for the specific item
                                    // You can perform your approval logic or show a confirmation dialog
                                    // For example:
                                    handleNewApproveRequest(modification);
                                }
                            });

                            accommodationListAdapter.setOnDenyButtonClickListener(new AccommodationModificationListAdapter.OnDenyButtonClickListener() {
                                @Override
                                public void onDenyButtonClick(AccommodationModification modification) {
                                    // Handle the approve button click for the specific item
                                    // You can perform your approval logic or show a confirmation dialog
                                    // For example:
                                    handleNewDenyRequest(modification);
                                }
                            });
                            accommodationListView.setAdapter(accommodationListAdapter);
                            accommodationListAdapter.notifyDataSetChanged();

                        }else{
                            Log.d("REZ","Meesage recieved: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<AccommodationModification>> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    }
                });
        // Inflate the layout for this fragment

        return root;


    }
    private void handleNewApproveRequest(AccommodationModification modification) {
        if (modification.getRequestType() == AccommodationModificationType.New) {
            Call<Accommodation> call = AccommodationUtils.accommodationService.updateEditingStatus(modification.getId(), false, "Bearer " + UserUtils.getCurrentUser().getJwt());

            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                    if (response.code() == 200){
                        Log.d("REZ","Meesage recieved");
                        approveRequest(modification.getId());

                    }else{
                        Log.d("REZ","Meesage recieved: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<Accommodation> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
    }

    private void approveRequest(Long id) {
        Call<AccommodationModification> call = AccommodationModificationUtils.accommodationModificationService.approve(id, "Bearer " + UserUtils.getCurrentUser().getJwt());

        call.enqueue(new Callback<AccommodationModification>() {
            @Override
            public void onResponse(Call<AccommodationModification> call, Response<AccommodationModification> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    accommodationListAdapter.updateModificationStatus(id, AccommodationModificationStatus.Approved);

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<AccommodationModification> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void handleNewDenyRequest(AccommodationModification modification) {
    }
}