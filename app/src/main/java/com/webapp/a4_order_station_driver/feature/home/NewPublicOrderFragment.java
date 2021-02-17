package com.webapp.a4_order_station_driver.feature.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.AttachmentAdapter;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.location.LocationManager;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class NewPublicOrderFragment extends Fragment implements OnMapReadyCallback, LocationManager.Listener {

    @BindView(R.id.tv_id) TextView tvOrderId;
    @BindView(R.id.tv_store_name) TextView tvStoreName;
    //@BindView(R.id.tv_datetime) TextView tvDatetime;
    @BindView(R.id.map_view) MapView mapView;
    @BindView(R.id.tv_order_details) TextView tvOrderDetails;
    @BindView(R.id.rv_attachments) RecyclerView rvAttachments;
    @BindView(R.id.tv_delivery_price) TextView tvDeliveryPrice;
    @BindView(R.id.tv_tax_price) TextView tvTaxPrice;
    @BindView(R.id.tv_order_dues) TextView tvOrderDues;
    @BindView(R.id.tv_app_dues) TextView tvAppDues;
    @BindView(R.id.tv_driver_dues) TextView tvDriverDues;
    @BindView(R.id.btn_accept) Button btnAccept;
    @BindView(R.id.btn_reject) Button btnReject;

    private boolean active = false;
    private AttachmentAdapter adapter;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private boolean denialLock;
    private Listener listener;
    private static NavigationView view;
    private PublicOrder publicOrder;
    private static Tracking tracking;
    private ArrayList<LatLng> points = new ArrayList<>();
    PolylineOptions lineOptions = null;
    private Polyline mPolyline;

    public static NewPublicOrderFragment newInstance(NavigationView navigationView, Tracking t) {
        view = navigationView;
        tracking = t;
        NewPublicOrderFragment fragment = new NewPublicOrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_public_order, container, false);
        ButterKnife.bind(this, view);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        locationManager = new LocationManager(this, getActivity(), this);
        return view;
    }

    @OnClick(R.id.btn_accept)
    public void accept() {
        if (active) {
            if (ToolUtils.checkTheInternet()) {
                WaitDialogFragment.newInstance().show(getFragmentManager(), "");
                AppController.getInstance().getApi().pickupPublicOrder(publicOrder.getId())
                        .enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                if (response.isSuccessful()) {
                                    WaitDialogFragment.newInstance().dismiss();
                                    AppController.getInstance().getAppSettingsPreferences().setTrackingPublicOrder(publicOrder);
                                    tracking.startGPSTracking();
                                    ToolUtils.showLongToast(getString(R.string.closeApp), getActivity());
                                    OrdersFragment.page = 1;
                                    WalletFragment.page = 1;
                                    view.navigate(3);
                                } else {
                                    ToolUtils.showError(getActivity(), response.errorBody());
                                    WaitDialogFragment.newInstance().dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                t.printStackTrace();
                                ToolUtils.showLongToast(getString(R.string.error), getActivity());
                                WaitDialogFragment.newInstance().dismiss();
                            }
                        });
            }
        }
    }

    @OnClick(R.id.btn_reject)
    public void rejectOrder() {
        view.navigate(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        // Run this here instead of onCreate() to cover the case where they return from turning on location
        if (!denialLock) {
            locationManager.fetchCurrentLocation();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
            listener.setDataInNewPublicOrder();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
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

    private void zoomToLocation(LatLng location) {
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

    public void setLine() {
        lineOptions = new PolylineOptions();
        lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(Color.BLUE);
        lineOptions.geodesic(true);
        googleMap.addPolyline(lineOptions);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setData(PublicOrder publicOrder) {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();

        this.publicOrder = publicOrder;
        tvOrderDetails.setText(publicOrder.getNote());
        tvOrderId.setText(getString(R.string.order) + "#" + publicOrder.getInvoice_number());
        tvStoreName.setText(publicOrder.getStore_name());
        tvDeliveryPrice.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getDelivery_cost())) + " " + currency);
        tvTaxPrice.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getTax())) + " " + currency);
        tvOrderDues.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getTotal())) + " " + currency);
        tvAppDues.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getApp_revenue())) + " " + currency);
        tvDriverDues.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getDriver_revenue())) + " " + currency);
        points.add(new LatLng(Double.parseDouble(this.publicOrder.getStore_lat())
                , Double.parseDouble(this.publicOrder.getStore_lng())));
        points.add(new LatLng(Double.parseDouble(this.publicOrder.getDestination_lat())
                , Double.parseDouble(this.publicOrder.getDestination_lng())));
        setMarkers(points.get(0));
        setMarkers(points.get(1));
        setLine();
        active = true;
        setAttachment();
    }

    private void setAttachment() {
        AttachmentAdapter adapter = new AttachmentAdapter(getActivity()
                , publicOrder.getAttachmentArrays(), getFragmentManager());
        rvAttachments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAttachments.setItemAnimator(new DefaultItemAnimator());
        rvAttachments.setAdapter(adapter);
    }

    interface Listener {
        void setDataInNewPublicOrder();
    }
}
