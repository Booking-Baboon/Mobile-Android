package com.example.bookingapptim4.ui.elements.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;

import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.domain.shared.JwtUtils;
import com.example.bookingapptim4.ui.state_holders.text_watchers.EmailFieldTextWatcher;
import com.example.bookingapptim4.ui.state_holders.text_watchers.RequiredFieldTextWatcher;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity {

    private TextInputLayout textInputEmail, textInputPassword;

    private User user = new User();

    private JwtUtils jwtUtils = new JwtUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Button loginButton = findViewById(R.id.loginButton);
        textInputEmail = findViewById(R.id.editText);
        textInputPassword = findViewById(R.id.editPassword);
        textInputEmail.getEditText().addTextChangedListener(new EmailFieldTextWatcher(textInputEmail));
        textInputPassword.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(textInputPassword));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = textInputEmail.getEditText().getText().toString();
                String password = textInputPassword.getEditText().getText().toString();
                if(!areFieldsValid()) return;
                login(new User(email, password));

            }
        });
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);

                startActivity(intent);
            }
        });
    }

    private void login(User userRequest){

        Call<User> call = UserUtils.userService.login(userRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    user = response.body();
                    user.setRole(jwtUtils.getRole(user.getJwt()));


                    switch (user.getRole()){
                        case GUEST:
                            Intent intent = new Intent(LoginScreen.this, GuestMainScreen.class);
                            intent.putExtra("USER", user);
                            UserUtils.setCurrentUser(user);
                            startActivity(intent);
                            break;
                        case HOST:
                            Intent intent1 = new Intent(LoginScreen.this, HostMainScreen.class);
                            intent1.putExtra("USER", user);
                            UserUtils.setCurrentUser(user);
                            startActivity(intent1);
                            break;
                        case ADMIN:
//                            Intent intent2 = new Intent(LoginScreen.this, AdministratorMainScreen.class);
//                            intent2.putExtra("USER", user);
//                            UserUtils.setCurrentUser(user);
//                            startActivity(intent2);
                            break;
                        default:
                            textInputEmail.setError("Invalid username or password");

                    }
                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                    textInputEmail.setError("Invalid username or password");
                }
            }

            @Override
            public void onFailure(Call<User>call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                textInputEmail.setError("Invalid username or password");
            }
        });

    }

    private boolean areFieldsValid() {
        if (textInputEmail.getEditText().length() == 0) {
            textInputEmail.setError("This field is required");
            return false;
        }

        if (textInputPassword.getEditText().length() == 0) {
            textInputPassword.setError("This field is required");
            return false;
        }

        return true;
    }
}