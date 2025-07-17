package com.example.ruhclinicmobile.screens.appointments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ruhclinicmobile.R;
import com.example.ruhclinicmobile.api.ApiClient;
import com.example.ruhclinicmobile.api.ApiService;
import com.example.ruhclinicmobile.models.Appointment;
import com.example.ruhclinicmobile.models.Client;
import com.example.ruhclinicmobile.utils.SnackbarUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAppointmentFragment extends Fragment {

    private Spinner spinnerClients;
    private TextView textSelectedDate, textSelectedTime, textErrorMessage;
    private Button buttonSubmit;

    private List<Client> clients = new ArrayList<>();
    private Client selectedClient;
    private List<Appointment> existingAppointments = new ArrayList<>();

    private int selectedYear = -1, selectedMonth = -1, selectedDay = -1;
    private int selectedHour = -1, selectedMinute = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_appointment, container, false);

        spinnerClients = view.findViewById(R.id.spinner_clients);
        textSelectedDate = view.findViewById(R.id.text_selected_date);
        textSelectedTime = view.findViewById(R.id.text_selected_time);
        textErrorMessage = view.findViewById(R.id.text_error_message);
        buttonSubmit = view.findViewById(R.id.button_submit);

        loadClients();
        loadExistingAppointments();

        textSelectedDate.setOnClickListener(v -> showDatePicker());
        textSelectedTime.setOnClickListener(v -> showTimePicker());

        buttonSubmit.setOnClickListener(v -> onSubmit());

        return view;
    }

    private void loadClients() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getClients().enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    clients = response.body();
                    setupClientSpinner();
                } else {
                    SnackbarUtils.show(getActivity(), "Failed to load clients", true);
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                SnackbarUtils.show(getActivity(), "Failed to load clients", true);
            }
        });
    }

    private void loadExistingAppointments() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAppointments().enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    existingAppointments = response.body();
                } else {
                    // Optionally handle failure to load appointments
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                // Optionally handle failure
            }
        });
    }

    private void setupClientSpinner() {
        List<String> clientNames = new ArrayList<>();
        clientNames.add("Select a client");
        for (Client client : clients) {
            clientNames.add(client.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                clientNames
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);

        spinnerClients.setAdapter(adapter);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(requireContext(),
                (DatePicker view, int y, int m, int d) -> {
                    selectedYear = y;
                    selectedMonth = m;
                    selectedDay = d;
                    textSelectedDate.setText(String.format("%04d-%02d-%02d", y, m + 1, d));
                    // clear time selection when date changes
                    selectedHour = -1;
                    selectedMinute = -1;
                    textSelectedTime.setText("No time selected");
                }, year, month, day);

        picker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        picker.show();
    }

    private void showTimePicker() {
        if (selectedYear == -1) {
            showError("Please select a date first.");
            return;
        }

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog picker = new TimePickerDialog(requireContext(),
                (view, h, m) -> {
                    selectedHour = h;
                    selectedMinute = m;
                    textSelectedTime.setText(String.format("%02d:%02d", h, m));
                }, hour, minute, true);

        picker.show();
    }

    private void onSubmit() {
        clearError();

        int clientPosition = spinnerClients.getSelectedItemPosition();
        if (clientPosition == 0) {
            showError("Please choose a client.");
            return;
        }
        selectedClient = clients.get(clientPosition - 1);

        if (selectedYear == -1 || selectedMonth == -1 || selectedDay == -1) {
            showError("Please choose a date.");
            return;
        }
        if (selectedHour == -1 || selectedMinute == -1) {
            showError("Please choose a time.");
            return;
        }

        if (!isValidDateTime()) {
            return;
        }

        Calendar appointmentCal = Calendar.getInstance();
        appointmentCal.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);

        // Check for duplicate appointment
        if (isDuplicateAppointment(selectedClient, appointmentCal)) {
            SnackbarUtils.show(getActivity(), "Appointment already exists for this client at the selected date and time.", true);
            return;
        }

        buttonSubmit.setEnabled(false);

        Appointment appointment = new Appointment();
        appointment.setClientId(Integer.parseInt(selectedClient.getId()));
        appointment.setTime(appointmentCal.getTime());

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.addAppointment(appointment).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                buttonSubmit.setEnabled(true);
                if (response.isSuccessful()) {
                    SnackbarUtils.show(getActivity(), "Appointment added successfully", false);
                    requireActivity().getSupportFragmentManager().popBackStack();

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, new AppointmentsFragment())
                            .commit();
                } else {
                    showError("Failed to add appointment.");
                }

            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                buttonSubmit.setEnabled(true);
                showError("Failed to add appointment.");
            }
        });
    }

    private boolean isDuplicateAppointment(Client client, Calendar dateTime) {
        for (Appointment appt : existingAppointments) {
            if (appt.getClientId() == Integer.parseInt(client.getId())) {
                Calendar apptCal = Calendar.getInstance();
                apptCal.setTime(appt.getTime());

                boolean sameDate = apptCal.get(Calendar.YEAR) == dateTime.get(Calendar.YEAR)
                        && apptCal.get(Calendar.DAY_OF_YEAR) == dateTime.get(Calendar.DAY_OF_YEAR);

                boolean sameTime = apptCal.get(Calendar.HOUR_OF_DAY) == dateTime.get(Calendar.HOUR_OF_DAY)
                        && apptCal.get(Calendar.MINUTE) == dateTime.get(Calendar.MINUTE);

                if (sameDate && sameTime) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidDateTime() {
        Calendar now = Calendar.getInstance();

        Calendar selected = Calendar.getInstance();
        selected.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);

        if (selected.before(now)) {
            if (isSameDate(now, selected)) {
                showError("Time must be in the future.");
            } else {
                showError("Date and time must be in the future.");
            }
            return false;
        }
        return true;
    }

    private boolean isSameDate(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    private void showError(String message) {
        textErrorMessage.setVisibility(View.VISIBLE);
        textErrorMessage.setText(message);
    }

    private void clearError() {
        textErrorMessage.setVisibility(View.GONE);
        textErrorMessage.setText("");
    }
}
