package com.example.ruhclinicmobile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.ruhclinicmobile.screens.appointments.AddAppointmentFragment;
import com.example.ruhclinicmobile.screens.appointments.AppointmentsFragment;
import com.example.ruhclinicmobile.screens.clients.ClientsFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_clients) {
                selectedFragment = new ClientsFragment();
            } else if (itemId == R.id.nav_appointments) {
                selectedFragment = new AppointmentsFragment();
            } else if (itemId == R.id.nav_add) {
                selectedFragment = new AddAppointmentFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }
            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, new AppointmentsFragment())
                    .commit();
        }
    }
}