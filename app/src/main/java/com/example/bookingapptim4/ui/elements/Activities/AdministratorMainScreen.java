package com.example.bookingapptim4.ui.elements.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.databinding.ActivityAdministratorMainScreenBinding;
import com.example.bookingapptim4.databinding.ActivityGuestMainScreenBinding;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.ui.state_holders.view_models.UserViewModel;

public class AdministratorMainScreen extends AppCompatActivity {

    private ActivityAdministratorMainScreenBinding binding;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavController navController;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdministratorMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User admin = getIntent().getParcelableExtra("USER");
        admin.setRole("ADMIN");

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setUser(admin);

        System.out.println( "Successfully logged in as:" +  admin.getEmail());
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null) {
            // postavlja prikazivanje "strelice prema nazad" (back arrow)
            // kao indikatora navigacije na lijevoj strani Toolbar-a.
            actionBar.setDisplayHomeAsUpEnabled(false);
            // postavlja ikonu koja se prikazuje umjesto strelice prema nazad.
            // U ovom slučaju, postavljena je ikona hamburger iz drawable resursa (ic_hamburger).
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            //vo omogućuje da se klikom na gumb 'home' na Toolbar-u
            // aktivira povratak na prethodni zaslon.
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);

        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}