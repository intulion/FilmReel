package com.akat.filmreel.ui.cinemas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akat.filmreel.R;
import com.akat.filmreel.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private double lat;
    private double lng;
    private double currentLat;
    private double currentLng;
    private String cinemaName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        Bundle bundle = requireArguments();
        cinemaName = bundle.getString(Constants.PARAM.CINEMA_NAME);
        lat = bundle.getDouble(Constants.PARAM.CINEMA_LAT);
        lng = bundle.getDouble(Constants.PARAM.CINEMA_LNG);
        currentLat = bundle.getDouble(Constants.PARAM.CURRENT_LAT);
        currentLng = bundle.getDouble(Constants.PARAM.CURRENT_LNG);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);

        UiSettings settings = googleMap.getUiSettings();
        settings.setCompassEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        LatLng current = new LatLng(currentLat, currentLng);
        LatLng cinema = new LatLng(lat, lng);

        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        latLngBuilder.include(current);
        latLngBuilder.include(cinema);
        LatLngBounds bounds = latLngBuilder.build();

        googleMap.addMarker(new MarkerOptions().position(current));
        googleMap.addMarker(
                new MarkerOptions()
                        .position(cinema)
                        .title(cinemaName)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        );
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
    }
}
