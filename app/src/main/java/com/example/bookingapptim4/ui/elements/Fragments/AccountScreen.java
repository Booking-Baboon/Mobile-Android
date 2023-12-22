package com.example.bookingapptim4.ui.elements.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.users.GuestService;
import com.example.bookingapptim4.data_layer.repositories.users.GuestUtils;
import com.example.bookingapptim4.data_layer.repositories.users.HostService;
import com.example.bookingapptim4.data_layer.repositories.users.UserService;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.domain.models.users.UserUpdateRequest;
import com.example.bookingapptim4.ui.elements.Activities.LoginScreen;
import com.example.bookingapptim4.ui.state_holders.text_watchers.EmailFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.text_watchers.PhoneFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.text_watchers.RequiredFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.view_models.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountScreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserViewModel userViewModel;
    private UserService userService;
    private GuestService guestService;
    private HostService hostService;
    private User userProfile = new User();

    private TextInputLayout textInputEmail, textInputPassword, textInputFirstName, textInputLastName, textInputPhone, textInputAddress;

    public AccountScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestAccountScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountScreen newInstance(String param1, String param2) {
        AccountScreen fragment = new AccountScreen();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_screen, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user-> {
            if (user != null) {

                Call<User> call = UserUtils.userService.getProfile(user.getEmail(), "Bearer " + user.getJwt());

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {

                            textInputEmail = view.findViewById(R.id.editTextEmail);
                            textInputEmail.getEditText().addTextChangedListener(new EmailFieldTextWatcher(textInputEmail));
                            textInputEmail.getEditText().setText(response.body().getEmail());

                            textInputFirstName = view.findViewById(R.id.editTextFirstName);
                            textInputFirstName.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputFirstName));
                            textInputFirstName.getEditText().setText(response.body().getFirstName());

                            textInputLastName = view.findViewById(R.id.editTextLastName);
                            textInputLastName.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputLastName));
                            textInputLastName.getEditText().setText(response.body().getLastName());

                            textInputPhone = view.findViewById(R.id.editTextPhone);
                            textInputPhone.getEditText().addTextChangedListener(new PhoneFieldTextWatcher(textInputPhone));
                            textInputPhone.getEditText().setText(response.body().getPhoneNumber());

                            textInputAddress = view.findViewById(R.id.editTextAddress);
                            textInputAddress.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputAddress));
                            textInputAddress.getEditText().setText(response.body().getAddress());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });

        Button saveChangesButton = view.findViewById(R.id.saveChangesButton);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all fields are valid
                if (isInputValid()) {
                    // Perform the save changes action
                    saveChanges();
                } else {
                    // Show an error or handle invalid input
                }
            }
        });

        Button logoutButton = view.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, open the LoginScreen activity

                userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

                userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user-> {
                   if (user != null) {
                       Call<Void> call = UserUtils.userService.logout("Bearer " + user.getJwt());
                       call.enqueue(new Callback<Void>() {
                           @Override
                           public void onResponse(Call<Void> call, Response<Void> response) {
                               if (response.code() == 200) {
                                   Intent intent = new Intent(getActivity(), LoginScreen.class);
                                   startActivity(intent);
                               }
                           }

                           @Override
                           public void onFailure(Call<Void> call, Throwable t) {

                           }
                       });
                   }
                });
            }
        });

        return view;
    }

    // Method to check if all input fields are valid
    private boolean isInputValid() {
        return isEmailValid() && isFirstNameValid() && isLastNameValid() && isPhoneValid() && isAddressValid();
    }

    private boolean isEmailValid() {
        return EmailFieldTextWatcher.isValid(textInputEmail.getEditText().getText().toString());
    }

    private boolean isFirstNameValid() {
        return RequiredFieldTextWatcher.isValid(textInputFirstName.getEditText().getText().toString());
    }

    private boolean isLastNameValid() {
        return RequiredFieldTextWatcher.isValid(textInputLastName.getEditText().getText().toString());
    }

    private boolean isPhoneValid() {
        return PhoneFieldTextWatcher.isValid(textInputPhone.getEditText().getText().toString());
    }

    private boolean isAddressValid() {
        return RequiredFieldTextWatcher.isValid(textInputAddress.getEditText().getText().toString());
    }
    private void saveChanges() {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user-> {
           if (user != null) {
               UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                       user.getId(),
                       textInputEmail.getEditText().getText().toString(),
                       textInputFirstName.getEditText().getText().toString(),
                       textInputLastName.getEditText().getText().toString(),
                       textInputAddress.getEditText().getText().toString(),
                       textInputPhone.getEditText().getText().toString(),
                       user.getJwt()
               );
               if(user.getRole().toString() == "GUEST") {
                    Call<Guest> call = GuestUtils.guestService.edit(userUpdateRequest, "Bearer " + user.getJwt());

                    call.enqueue(new Callback<Guest>() {
                        @Override
                        public void onResponse(Call<Guest> call, Response<Guest> response) {
                            if (response.code() == 200) {
                                System.out.println("update succesfull");
                            }
                        }

                        @Override
                        public void onFailure(Call<Guest> call, Throwable t) {

                        }
                    });

               }
           }
        });

    }
}