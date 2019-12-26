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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.MovieApplication;
import com.akat.filmreel.R;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.SnackbarMessage;
import com.akat.filmreel.util.SnackbarUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

public class CinemaListFragment extends Fragment
        implements CinemaListAdapter.CinemaListAdapterOnItemClickHandler {

    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 13;

    @Inject
    public ViewModelProvider.Factory factory;

    private FusedLocationProviderClient fusedLocationClient;
    private CinemaListAdapter cinemaListAdapter;
    private CinemaListViewModel viewModel;

    private RecyclerView recyclerView;
    private View loadingIndicator;
    private View noItemView;

    private boolean showLoading = true;

    private Location location = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        MovieApplication.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cinema_list, container, false);

        loadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        noItemView = view.findViewById(R.id.cinemas_no_item);
        view.findViewById(R.id.no_item_retry).setOnClickListener(v -> {
            showLoading = true;
            showCinemas();
        });

        recyclerView = view.findViewById(R.id.recycler_view_cinema_list);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration mDividerItemDecoration =
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
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
        bundle.putDouble(Constants.PARAM.CURRENT_LAT, location.getLatitude());
        bundle.putDouble(Constants.PARAM.CURRENT_LNG, location.getLongitude());

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
        } else {
            showLoading = false;
            updateDataView();
        }
    }

    private void doShowCinemas() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location == null) {
                        Snackbar.make(loadingIndicator, R.string.error_location, Snackbar.LENGTH_LONG).show();
                    } else {
                        this.location = location;
                        loadData();
                    }
                });
        updateDataView();
    }

    private void loadData() {
        if (viewModel != null && location != null) {
            viewModel.forceUpdate(location.getLatitude(), location.getLongitude());
        } else {
            viewModel = ViewModelProviders.of(this, factory).get(CinemaListViewModel.class);
            viewModel.getNearbyCinemas(location.getLatitude(),
                    location.getLongitude()).observe(this,
                    entries -> {
                        showLoading = false;
                        cinemaListAdapter.setItems(entries);
                        updateDataView();
                    }
            );
            setupSnackbar();
        }
    }

    private void updateDataView() {
        if (cinemaListAdapter.getItemCount() == 0) {
            if (showLoading) {
                recyclerView.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.VISIBLE);
                noItemView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.GONE);
                noItemView.setVisibility(View.VISIBLE);
            }
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.GONE);
            noItemView.setVisibility(View.GONE);
        }
    }

    private void setupSnackbar() {
        viewModel.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
                        SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId))
        );
    }
}
