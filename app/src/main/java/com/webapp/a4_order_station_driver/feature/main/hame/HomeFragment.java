package com.webapp.a4_order_station_driver.feature.main.hame;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.webapp.a4_order_station_driver.databinding.FragmentHomeBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.utils.location.GPSTracking;
import com.webapp.a4_order_station_driver.utils.location.LocationManager;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationManager.Listener {

    public static final int page = 201;

    private FragmentHomeBinding binding;

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private boolean denialLock;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(this);

        locationManager = new LocationManager(this, getActivity(), this);

        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
    }

    @Override
    public void onServicesOrPermissionChoice() {
        denialLock = false;
    }

    @Override
    public void onLocationFound(double latitude, double longitude) {
        if (!MainActivity.isLoadingNewOrder) {
            zoomToLocation(new LatLng(latitude, longitude));
            //tracking
            GPSTracking.getInstance(getActivity()).startMyGPSTracking();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode != LocationManager.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        // No need to check if the location permission has been granted because of the onResume() block
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            denialLock = true;
            locationManager.showLocationPermissionDialog();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationManager.LOCATION_SERVICES_CODE) {
            if (resultCode == RESULT_OK) {
                locationManager.fetchAutomaticLocation();
            } else {
                denialLock = true;
                locationManager.showLocationDenialDialog();
            }
        }
    }

    public void zoomToLocation(LatLng location) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(location));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(16)
                .bearing(0)
                .tilt(0)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();

        // Run this here instead of onCreate() to cover the case where they return from turning on location
        if (!denialLock) {
            locationManager.fetchCurrentLocation();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
        locationManager.cleanUp();
    }
}
