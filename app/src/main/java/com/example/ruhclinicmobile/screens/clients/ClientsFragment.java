package com.example.ruhclinicmobile.screens.clients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruhclinicmobile.R;
import com.example.ruhclinicmobile.api.ApiClient;
import com.example.ruhclinicmobile.api.ApiService;
import com.example.ruhclinicmobile.models.Client;
import com.example.ruhclinicmobile.adapters.ClientAdapter;

import com.example.ruhclinicmobile.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClientAdapter adapter;
    private ProgressBar progressBar;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients, container, false);

        recyclerView = view.findViewById(R.id.clients_recycler);
        progressBar = view.findViewById(R.id.progress_bar);
        searchView = view.findViewById(R.id.search_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ClientAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        setupSearch();
        loadClients();

        return view;
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    private void loadClients() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getClients().enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setClients(response.body());
                } else {
                    SnackbarUtils.show(getActivity(), "Failed to load clients", true);
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                SnackbarUtils.show(getActivity(), "Failed to load clients: " + t.getMessage(), true);
            }
        });
    }

}
