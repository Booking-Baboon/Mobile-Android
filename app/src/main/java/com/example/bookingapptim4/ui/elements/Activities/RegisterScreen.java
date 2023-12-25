package com.example.bookingapptim4.ui.elements.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.users.GuestUtils;
import com.example.bookingapptim4.data_layer.repositories.users.HostUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.Host;
import com.example.bookingapptim4.domain.models.users.Role;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.ui.state_holders.text_watchers.EmailFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.text_watchers.PhoneFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.text_watchers.RequiredFieldTextWatcher;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterScreen extends AppCompatActivity {

    private TextInputLayout textInputFirstName, textInputLastName, textInputEmail, textInputAddress, textInputPhone, textInputPassword, textInputPasswordConfirm;
    private SwitchMaterial hostSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        Button registerButton = findViewById(R.id.registerConfirmButton);
        textInputFirstName = findViewById(R.id.firstNameRegister);
        textInputLastName = findViewById(R.id.lastNameRegister);
        textInputEmail = findViewById(R.id.emailRegister);
        textInputAddress = findViewById(R.id.addressRegister);
        textInputPhone = findViewById(R.id.phoneRegister);
        textInputPassword = findViewById(R.id.passwordRegister);
        textInputPassword = findViewById(R.id.passwordRegister);
        textInputPasswordConfirm = findViewById(R.id.passwordConfirmRegister);
        hostSwitch = findViewById(R.id.hostSwitchRegister);
        textInputFirstName.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputFirstName));
        textInputLastName.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputLastName));
        textInputEmail.getEditText().addTextChangedListener(new EmailFieldTextWatcher(textInputEmail));
        textInputAddress.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputAddress));
        textInputPassword.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputPassword));
        textInputPasswordConfirm.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputPasswordConfirm));
        textInputPhone.getEditText().addTextChangedListener(new PhoneFieldTextWatcher(textInputPhone));
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!areFieldsValid()) return;
                String firstName = textInputFirstName.getEditText().getText().toString();
                String lastName = textInputLastName.getEditText().getText().toString();
                String email = textInputEmail.getEditText().getText().toString();
                String phone = textInputPhone.getEditText().getText().toString();
                String address = textInputAddress.getEditText().getText().toString();
                String password = textInputPassword.getEditText().getText().toString();
                String passwordConfirm = textInputPasswordConfirm.getEditText().getText().toString();
                if(hostSwitch.isChecked()){
                    User user = new User(password, email, firstName, lastName, address, phone, Role.HOST);
                    registerHost(user);
                }else{
                    User user = new User(password, email, firstName, lastName, address, phone, Role.GUEST);
                    registerGuest(user);
                }
            }
        });

    }

    private void registerGuest(User userRequest){

        Call<Guest> call = GuestUtils.guestService.create(new Guest(userRequest));

        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);

                    startActivity(intent);

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Guest>call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    private void registerHost(User userRequest){

        Call<Host> call = HostUtils.hostService.create(new Host(userRequest));

        call.enqueue(new Callback<Host>() {
            @Override
            public void onResponse(Call<Host> call, Response<Host> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);

                    startActivity(intent);

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Host>call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    private boolean areFieldsValid() {

        if (textInputFirstName.getEditText().length() == 0) {
            textInputFirstName.setError("This field is required");
            return false;
        }else{
            textInputFirstName.setError(null);
        }

        if (textInputLastName.getEditText().length() == 0) {
            textInputLastName.setError("This field is required");
            return false;
        }


        if (textInputEmail.getEditText().length() == 0) {
            textInputEmail.setError("This field is required");
            return false;
        }

        if (textInputAddress.getEditText().length() == 0) {
            textInputAddress.setError("This field is required");
            return false;
        }

        if (textInputPhone.getEditText().length() == 0) {
            textInputPhone.setError("This field is required");
            return false;
        }

        if (textInputPassword.getEditText().length() == 0) {
            textInputPassword.setError("This field is required");
            return false;
        }

        if (textInputPasswordConfirm.getEditText().length() == 0) {
            textInputPasswordConfirm.setError("This field is required");
            return false;
        }

        if(!textInputPassword.getEditText().getText().toString().equals(textInputPasswordConfirm.getEditText().getText().toString())){
            textInputPassword.setError("Passwords must match");
            textInputPasswordConfirm.setError("Passwords must match");
            return false;
        }

        return true;
    }
}


