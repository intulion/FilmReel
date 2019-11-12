package com.akat.filmreel.ui.cinemas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.InjectorUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CinemaListFragment extends Fragment
        implements CinemaListAdapter.CinemaListAdapterOnItemClickHandler, OnSuccessListener<Location> {

    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 13;

    private FusedLocationProviderClient fusedLocationClient;
    private CinemaListAdapter cinemaListAdapter;

    private double currentLat;
    private double currentLng;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cinema_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_cinema_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration mDividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        cinemaListAdapter = new CinemaListAdapter(requireActivity(), this);
        recyclerView.setAdapter(cinemaListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCinemas();
    }

    @Override
    public void onItemClick(View view, String name, Double lat, Double lng) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM.CINEMA_NAME, name);
        bundle.putDouble(Constants.PARAM.CINEMA_LAT, lat);
        bundle.putDouble(Constants.PARAM.CINEMA_LNG, lng);
        bundle.putDouble(Constants.PARAM.CURRENT_LAT, currentLat);
        bundle.putDouble(Constants.PARAM.CURRENT_LNG, currentLng);

        Navigation.findNavController(view).navigate(R.id.fragment_maps, bundle);
    }

    private void showCinemas() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            doShowCinemas();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            doShowCinemas();
        }
    }

    private void doShowCinemas() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this);
    }

    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            currentLat = location.getLatitude();
            currentLng = location.getLongitude();

            CinemaListViewModelFactory factory =
                    InjectorUtils.provideCinemaListViewModelFactory(currentLat, currentLng);
            CinemaListViewModel viewModel = ViewModelProviders.of(this, factory).get(CinemaListViewModel.class);
            viewModel.getNearbyCinemas().observe(this,
                    entries -> cinemaListAdapter.setItems(entries)
            );
        }
    }
}
