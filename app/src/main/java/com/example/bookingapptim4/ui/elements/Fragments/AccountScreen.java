package com.example.bookingapptim4.ui.elements.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.shared.ConfirmationDialog;
import com.example.bookingapptim4.data_layer.repositories.users.AdminUtils;
import com.example.bookingapptim4.data_layer.repositories.users.GuestService;
import com.example.bookingapptim4.data_layer.repositories.users.GuestUtils;
import com.example.bookingapptim4.data_layer.repositories.users.HostService;
import com.example.bookingapptim4.data_layer.repositories.users.HostUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserService;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.dtos.PasswordChangeRequest;
import com.example.bookingapptim4.domain.models.notifications.NotificationType;
import com.example.bookingapptim4.domain.models.users.Admin;
import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.Host;
import com.example.bookingapptim4.domain.models.users.Role;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.domain.models.users.UserUpdateRequest;
import com.example.bookingapptim4.ui.elements.Activities.LoginScreen;
import com.example.bookingapptim4.ui.state_holders.text_watchers.EmailFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.text_watchers.PasswordFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.text_watchers.PhoneFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.text_watchers.RequiredFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.view_models.UserViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
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

    private TextInputLayout textInputEmail, textInputPassword, textInputFirstName, textInputLastName, textInputPhone, textInputAddress, textInputCurrentPassword, textInputNewPassword, textInputConfirmPassword;

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

                            userProfile = response.body();

                            textInputEmail = view.findViewById(R.id.editTextEmail);
                            textInputEmail.getEditText().addTextChangedListener(new EmailFieldTextWatcher(textInputEmail));

                            textInputFirstName = view.findViewById(R.id.editTextFirstName);
                            textInputFirstName.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputFirstName));

                            textInputLastName = view.findViewById(R.id.editTextLastName);
                            textInputLastName.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputLastName));

                            textInputPhone = view.findViewById(R.id.editTextPhone);
                            textInputPhone.getEditText().addTextChangedListener(new PhoneFieldTextWatcher(textInputPhone));

                            textInputAddress = view.findViewById(R.id.editTextAddress);
                            textInputAddress.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputAddress));

                            textInputCurrentPassword = view.findViewById(R.id.editTextCurrentPassword);

                            textInputNewPassword = view.findViewById(R.id.editTextNewPassword);
                            textInputConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
                            setDefaultInformation();

                            // Add TextWatcher after initializing TextInputLayout instances
                            textInputNewPassword.getEditText().addTextChangedListener(new PasswordFieldTextWatcher(textInputNewPassword.getEditText(), textInputConfirmPassword.getEditText()));
                            textInputConfirmPassword.getEditText().addTextChangedListener(new PasswordFieldTextWatcher(textInputNewPassword.getEditText(), textInputConfirmPassword.getEditText()));

                            loadNotifications(view, user.getRole());
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
                System.out.println(isInputValid());
                if (isInputValid()) {
                    // Perform the save changes action
                    saveChanges(view);
                } else {
                    // Show an error or handle invalid input
                }
            }
        });

        Button discardChanges = view.findViewById(R.id.discardChangesButton);

        discardChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultInformation();
            }
        });

        Button logoutButton = view.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, open the LoginScreen activity
                logout();
                Intent intent = new Intent(getActivity(), LoginScreen.class);
                startActivity(intent);
            }
        });

        Button deleteAccountButton = view.findViewById(R.id.deleteAccountButton);

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Show confirmation dialog
                ConfirmationDialog.show(requireContext(), "Confirm Deletion",
                        "Are you sure you want to delete your account? This action cannot be undone.",
                        new ConfirmationDialog.ConfirmationDialogListener() {
                            @Override
                            public void onConfirm() {
                                // User clicked Yes, proceed with account deletion
                                deleteAccount(view);
                            }

                            @Override
                            public void onCancel() {
                                // User clicked No, do nothing or dismiss the dialog
                            }
                        });
            }
        });



        return view;
    }

    private void loadNotifications(View view,Role role) {
        SwitchMaterial reservationCreatedSwitch = view.findViewById(R.id.accept_reservation_created_notifications);
        SwitchMaterial reservationCancelledSwitch = view.findViewById(R.id.accept_reservation_cancelled_notifications);
        SwitchMaterial hostReviewSwitch = view.findViewById(R.id.accept_host_review_notifications);
        SwitchMaterial accommodationReviewSwitch = view.findViewById(R.id.accept_accommodation_review_notifications);
        SwitchMaterial reservationResponseSwitch = view.findViewById(R.id.accept_reservation_response_notifications);

        TextView notificationsDescription = view.findViewById(R.id.notificationsDescription);
        TextView notificationsTitle = view.findViewById(R.id.notificationsTitle);

        reservationCreatedSwitch.setChecked(true);
        reservationCancelledSwitch.setChecked(true);
        hostReviewSwitch.setChecked(true);
        accommodationReviewSwitch.setChecked(true);
        reservationResponseSwitch.setChecked(true);


        for (NotificationType notificationType : userProfile.getIgnoredNotifications()) {
            switch (notificationType.toString()) {
                case "ReservationCreated":
                    reservationCreatedSwitch.setChecked(false);
                    break;
                case "ReservationCancelled":
                    reservationCancelledSwitch.setChecked(false);
                    break;
                case "HostReview":
                    hostReviewSwitch.setChecked(false);
                    break;
                case "AccommodationReview":
                    accommodationReviewSwitch.setChecked(false);
                    break;
                case "ReservationRequestResponse":
                    reservationResponseSwitch.setChecked(false);
                    break;
                // Add more cases as needed for other notification types
            }
        }

        if (role.equals(Role.GUEST)) {
            reservationCreatedSwitch.setVisibility(View.GONE);
            reservationCancelledSwitch.setVisibility(View.GONE);
            hostReviewSwitch.setVisibility(View.GONE);
            accommodationReviewSwitch.setVisibility(View.GONE);
        } else if (role.equals(Role.HOST)) {
            reservationResponseSwitch.setVisibility(View.GONE);
        } else {
            reservationCreatedSwitch.setVisibility(View.GONE);
            reservationCancelledSwitch.setVisibility(View.GONE);
            hostReviewSwitch.setVisibility(View.GONE);
            accommodationReviewSwitch.setVisibility(View.GONE);
            reservationResponseSwitch.setVisibility(View.GONE);
            notificationsDescription.setVisibility(View.GONE);
            notificationsTitle.setVisibility(View.GONE);
        }

        reservationCreatedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleNotification(NotificationType.ReservationCreated);
            }
        });

        reservationCancelledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleNotification(NotificationType.ReservationCancelled);
            }
        });

        hostReviewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleNotification(NotificationType.HostReview);
            }
        });

        accommodationReviewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleNotification(NotificationType.AccommodationReview);
            }
        });

        reservationResponseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleNotification(NotificationType.ReservationRequestResponse);
            }
        });
        
    }

    private void toggleNotification(NotificationType notificationType) {
        Call<User> call = UserUtils.userService.toggleNotifications(userProfile.getId(), notificationType, "Bearer " + UserUtils.getCurrentUser().getJwt());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void setDefaultInformation() {
        textInputEmail.getEditText().setText(userProfile.getEmail());
        textInputFirstName.getEditText().setText(userProfile.getFirstName());
        textInputLastName.getEditText().setText(userProfile.getLastName());
        textInputPhone.getEditText().setText(userProfile.getPhoneNumber());
        textInputAddress.getEditText().setText(userProfile.getAddress());
    }

    private void deleteAccount(View view) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user-> {
            if (user != null) {
                if(user.getRole().toString() == "GUEST") {
                    Call<Guest> call = GuestUtils.guestService.remove(user.getId(), "Bearer " + user.getJwt());

                    call.enqueue(new Callback<Guest>() {
                        @Override
                        public void onResponse(Call<Guest> call, Response<Guest> response) {
                            if (response.code() == 403) {
                                showSnackbar(view, "Deletion failed: You have active reservations.");
                            }
                            if (response.code() == 200) {
                                logout();
                                Intent intent = new Intent(getActivity(), LoginScreen.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Guest> call, Throwable t) {

                        }
                    });

                } else if (user.getRole().toString() == "HOST") {
                    Call<Host> call = HostUtils.hostService.remove(user.getId(), "Bearer " + user.getJwt());

                    call.enqueue(new Callback<Host>() {
                        @Override
                        public void onResponse(Call<Host> call, Response<Host> response) {
                            if (response.code() == 403) {
                                showSnackbar(view, "Deletion failed. One or more of your accommodations have active reservations");
                            }
                            if (response.code() == 200) {
                                logout();
                                Intent intent = new Intent(getActivity(), LoginScreen.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Host> call, Throwable t) {

                        }
                    });

                } else if (user.getRole().toString() == "ADMIN") {
                    Call<Admin> call = AdminUtils.adminService.remove(user.getId(), "Bearer " + user.getJwt());
                    call.enqueue(new Callback<Admin>() {
                        @Override
                        public void onResponse(Call<Admin> call, Response<Admin> response) {
                            if (response.code() == 200) {
                                logout();
                                Intent intent = new Intent(getActivity(), LoginScreen.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onFailure(Call<Admin> call, Throwable t) {

                        }
                    });

                }
            }
        });
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    private void logout() {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user-> {
            if (user != null) {
                Call<Void> call = UserUtils.userService.logout("Bearer " + user.getJwt());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
    }

    // Method to check if all input fields are valid
    private boolean isInputValid() {
        return isEmailValid() && isFirstNameValid() && isLastNameValid() && isPhoneValid() && isAddressValid() && isPasswordValid();
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

    private boolean isPasswordValid() {
        String currentPassword = textInputCurrentPassword.getEditText().getText().toString();
        String newPassword = textInputNewPassword.getEditText().getText().toString();
        String confirmPassword = textInputConfirmPassword.getEditText().getText().toString();
        if (currentPassword.equals("")) {
            return true;
        }else if(newPassword.equals("") || confirmPassword.equals("")) {
            textInputConfirmPassword.getEditText().setError("New password cannot be empty");
        }
        return PasswordFieldTextWatcher.isValid(newPassword, confirmPassword);
    }
    private void saveChanges(View view) {
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
                            if (response.code() == 500) {
                                textInputEmail.getEditText().setError("Email already in use!");
                            }
                            if (response.code() == 200) {
                            }
                        }
                        @Override
                        public void onFailure(Call<Guest> call, Throwable t) {
                        }
                    });

               } else if (user.getRole().toString() == "HOST") {
                   Call<Host> call = HostUtils.hostService.edit(userUpdateRequest, "Bearer " + user.getJwt());
                   call.enqueue(new Callback<Host>() {
                       @Override
                       public void onResponse(Call<Host> call, Response<Host> response) {
                           if (response.code() == 200) {
                           }
                       }
                       @Override
                       public void onFailure(Call<Host> call, Throwable t) {
                       }
                   });

               } else if (user.getRole().toString() == "ADMIN") {
                   Call<Admin> call = AdminUtils.adminService.edit(userUpdateRequest, "Bearer " + user.getJwt());
                   call.enqueue(new Callback<Admin>() {
                       @Override
                       public void onResponse(Call<Admin> call, Response<Admin> response) {
                           if (response.code() == 200) {
                           }
                       }
                       @Override
                       public void onFailure(Call<Admin> call, Throwable t) {
                       }
                   });
               }
               String currentPassword = textInputCurrentPassword.getEditText().getText().toString();
               String newPassword = textInputNewPassword.getEditText().getText().toString();
               String confirmPassword = textInputConfirmPassword.getEditText().getText().toString();

               if (!currentPassword.equals("") && !currentPassword.equals("") && !currentPassword.equals("")) {
                   PasswordChangeRequest request = new PasswordChangeRequest(currentPassword, newPassword);
                    Call<User> call = UserUtils.userService.changePassword(user.getId(), request, "Bearer " + user.getJwt());
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 401) {
                                textInputCurrentPassword.getEditText().setError("Wrong password!");
                            }
                            if (response.code() == 200) {
                            logout();
                                Intent intent = new Intent(getActivity(), LoginScreen.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                        }
                    });
               } else {
                   showSnackbar(view, "Update successful");
               }
           }
        });
    }
}