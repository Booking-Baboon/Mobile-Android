package com.example.bookingapptim4.ui.elements.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.bookingapptim4.domain.models.users.User;
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
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = textInputFirstName.getEditText().getText().toString();
                String lastName = textInputLastName.getEditText().getText().toString();
                String email = textInputEmail.getEditText().getText().toString();
                String phone = textInputPhone.getEditText().getText().toString();
                String address = textInputAddress.getEditText().getText().toString();
                String password = textInputPassword.getEditText().getText().toString();
                String passwordConfirm = textInputPasswordConfirm.getEditText().getText().toString();
                User user = new User(password, email, firstName, lastName, address, phone);
                if(hostSwitch.isChecked()){
                    registerHost(user);
                }else{
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
}