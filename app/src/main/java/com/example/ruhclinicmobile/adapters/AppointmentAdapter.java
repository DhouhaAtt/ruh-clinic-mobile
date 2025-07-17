package com.example.ruhclinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruhclinicmobile.R;
import com.example.ruhclinicmobile.models.Appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<Appointment> appointments = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public AppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setAppointments(List<Appointment> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.clientIdText.setText("Client ID: " + appointment.getClientId());
        holder.dateText.setText(dateFormat.format(appointment.getTime()));
        holder.timeText.setText(timeFormat.format(appointment.getTime()));
    }

    @Override
    public int getItemCount() {
        return appointments != null ? appointments.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clientIdText, dateText, timeText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            clientIdText = itemView.findViewById(R.id.text_client_id);
            dateText = itemView.findViewById(R.id.text_date);
            timeText = itemView.findViewById(R.id.text_time);
        }
    }
}
