package com.webapp.a4_order_station_driver.feature.order.newPublicOrder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentNewPublicOrderBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.main.adapter.AttachmentAdapter;
import com.webapp.a4_order_station_driver.feature.main.hame.HomeFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.location.LocationManager;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class NewPublicOrderFragment extends Fragment implements OnMapReadyCallback, LocationManager.Listener, DialogView<Message> {

    public final static int page = 502;

    private FragmentNewPublicOrderBinding binding;

    private boolean active = false;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private boolean denialLock;
    private BaseActivity baseActivity;
    private PublicOrder publicOrder;
    private ArrayList<LatLng> points = new ArrayList<>();
    private PolylineOptions lineOptions = null;
    private NewPublicOrderPresenter presenter;

    public NewPublicOrderFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static NewPublicOrderFragment newInstance(BaseActivity baseActivity, PublicOrder publicOrder) {
        NewPublicOrderFragment fragment = new NewPublicOrderFragment(baseActivity);
        Bundle args = new Bundle();
        args.putSerializable(AppContent.ORDER_OBJECT, publicOrder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewPublicOrderBinding.inflate(getLayoutInflater());
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(this);
        //location Manager
        locationManager = new LocationManager(this, getActivity(), this);
        //presenter
        presenter = new NewPublicOrderPresenter(baseActivity, this);
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnAccept.setOnClickListener(view -> accept());
        binding.btnReject.setOnClickListener(view -> baseActivity.onBackPressed());
    }

    public void accept() {
        if (active) {
            presenter.accept(publicOrder);
        }
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

    @Override
    public void onServicesOrPermissionChoice() {
        denialLock = false;
    }

    @Override
    public void onLocationFound(double latitude, double longitude) {
        if (!MainActivity.isLoadingNewOrder) {
            //zoomToLocation(new LatLng(latitude, longitude));
            //listener.setDataInNewPublicOrder();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        data();
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

    private void setMarkers(LatLng location) {
        googleMap.addMarker(new MarkerOptions().position(location));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(16)
                .bearing(0)
                .tilt(0)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void data() {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();
        publicOrder = (PublicOrder) requireArguments().getSerializable(AppContent.ORDER_OBJECT);

        assert publicOrder != null;
        binding.tvOrderDetails.setText(publicOrder.getNote());
        binding.tvId.setText((getString(R.string.order) + "#" + publicOrder.getInvoice_number()));
        binding.tvStoreName.setText(publicOrder.getStore_name());
        binding.tvDeliveryPrice.setText((DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getDelivery_cost())) + " " + currency));
        binding.tvTaxPrice.setText((DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getTax())) + " " + currency));
        binding.tvOrderDues.setText((DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getTotal())) + " " + currency));
        binding.tvAppDues.setText((DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getApp_revenue())) + " " + currency));
        binding.tvDriverDues.setText((DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getDriver_revenue())) + " " + currency));
        points.add(new LatLng(Double.parseDouble(this.publicOrder.getStore_lat())
                , Double.parseDouble(this.publicOrder.getStore_lng())));
        points.add(new LatLng(Double.parseDouble(this.publicOrder.getDestination_lat())
                , Double.parseDouble(this.publicOrder.getDestination_lng())));

        for (int i = 0; i < points.size(); i++) {
            setMarkers(points.get(i));
        }
        setLine();
        setAttachment();
        active = true;
    }

    public void setLine() {
        lineOptions = new PolylineOptions();
        lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(Color.BLUE);
        lineOptions.geodesic(true);
        googleMap.addPolyline(lineOptions);
    }

    private void setAttachment() {
        AttachmentAdapter adapter = new AttachmentAdapter(getActivity()
                , publicOrder.getAttachmentArrays(), getChildFragmentManager());
        binding.rvAttachments.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAttachments.setItemAnimator(new DefaultItemAnimator());
        binding.rvAttachments.setAdapter(adapter);
    }

    @Override
    public void setData(Message message) {
    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }
}
