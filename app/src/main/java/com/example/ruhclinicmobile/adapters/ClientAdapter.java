package com.example.ruhclinicmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ruhclinicmobile.R;
import com.example.ruhclinicmobile.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<Client> clients = new ArrayList<>();
    private List<Client> allClients = new ArrayList<>();

    public ClientAdapter(List<Client> clients) {
        this.clients = clients;
        this.allClients = new ArrayList<>(clients);
    }

    public void setClients(List<Client> newClients) {
        this.clients = newClients;
        this.allClients = new ArrayList<>(newClients);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        clients.clear();
        if (text.isEmpty()) {
            clients.addAll(allClients);
        } else {
            text = text.toLowerCase();
            for (Client client : allClients) {
                if ((client.getName() != null && client.getName().toLowerCase().contains(text)) ||
                        (client.getEmail() != null && client.getEmail().toLowerCase().contains(text))) {
                    clients.add(client);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.name.setText(client.getName());
        holder.email.setText(client.getEmail());
        holder.phone.setText(client.getPhone());
        if (client.getName() != null && !client.getName().isEmpty()) {
            holder.avatar.setText(client.getName().substring(0, 1).toUpperCase());
        } else {
            holder.avatar.setText("?");
        }
    }

    @Override
    public int getItemCount() {
        return clients != null ? clients.size() : 0;
    }

    static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, phone, avatar;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clientName);
            email = itemView.findViewById(R.id.clientEmail);
            phone = itemView.findViewById(R.id.clientPhone);
            avatar = itemView.findViewById(R.id.clientAvatar);
        }
    }
}
