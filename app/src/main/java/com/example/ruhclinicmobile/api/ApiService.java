package com.example.ruhclinicmobile.api;

import com.example.ruhclinicmobile.models.Appointment;
import com.example.ruhclinicmobile.models.Client;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Clients
    @GET("clients")
    Call<List<Client>> getClients();

    @POST("clients")
    Call<Client> addClient(@Body Client client);

    @PUT("clients/{id}")
    Call<Client> updateClient(@Path("id") int clientId, @Body Client client);

    @DELETE("clients/{id}")
    Call<Void> deleteClient(@Path("id") int clientId);

    // Appointments
    @GET("appointments")
    Call<List<Appointment>> getAppointments();

    @POST("appointments")
    Call<Appointment> addAppointment(@Body Appointment appointment);

    @PUT("appointments/{id}")
    Call<Appointment> updateAppointment(@Path("id") int appointmentId, @Body Appointment appointment);

    @DELETE("appointments/{id}")
    Call<Void> deleteAppointment(@Path("id") int appointmentId);
}