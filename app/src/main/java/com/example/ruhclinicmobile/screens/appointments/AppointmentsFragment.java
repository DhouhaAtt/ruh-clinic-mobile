package com.example.ruhclinicmobile.screens.appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruhclinicmobile.R;
import com.example.ruhclinicmobile.api.ApiClient;
import com.example.ruhclinicmobile.api.ApiService;
import com.example.ruhclinicmobile.models.Appointment;
import com.example.ruhclinicmobile.adapters.AppointmentAdapter;

import com.example.ruhclinicmobile.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);

        recyclerView = view.findViewById(R.id.appointments_recycler);
        progressBar = view.findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AppointmentAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        loadAppointments();

        return view;
    }

    private void loadAppointments() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAppointments().enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setAppointments(response.body());
                } else {
                    SnackbarUtils.show(getActivity(), "Failed to load appointments", true);
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                SnackbarUtils.show(getActivity(), "Failed to load appointments: " + t.getMessage(), true);
            }
        });
    }
}
