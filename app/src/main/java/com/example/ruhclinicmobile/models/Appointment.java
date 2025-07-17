package com.example.ruhclinicmobile.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Appointment {

    @SerializedName("id")
    private int id;

    @SerializedName("client_id")
    private int clientId;

    @SerializedName("time")
    private Date time;

    public Appointment() {}

    public Appointment(int clientId, Date time) {
        this.clientId = clientId;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
