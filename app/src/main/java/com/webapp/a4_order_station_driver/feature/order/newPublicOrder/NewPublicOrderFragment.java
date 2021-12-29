package com.webapp.a4_order_station_driver.feature.order.newPublicOrder;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.webapp.a4_order_station_driver.databinding.FragmentNewPublicOrderBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity2;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.location.LocationManager;

public class NewPublicOrderFragment extends Fragment implements OnMapReadyCallback, LocationManager.Listener, DialogView<Message> {

    public final static int page = 502;

    private FragmentNewPublicOrderBinding binding;

    private boolean active = false;
   // private GoogleMap googleMap;
    //private LocationManager locationManager;
    private boolean denialLock;
    private BaseActivity baseActivity;
    private PublicOrder publicOrder;
    //private ArrayList<LatLng> points = new ArrayList<>();
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
        /*binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(this);*/
        //location Manager
        //locationManager = new LocationManager(this, getActivity(), this);
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
        //binding.mapView.onResume();

        // Run this here instead of onCreate() to cover the case where they return from turning on location
//        if (!denialLock) {
//            locationManager.fetchCurrentLocation();
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //binding.mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        //binding.mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        //binding.mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //binding.mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //binding.mapView.onDestroy();
        //locationManager.cleanUp();
    }

    @Override
    public void onServicesOrPermissionChoice() {
        denialLock = false;
    }

    @Override
    public void onLocationFound(double latitude, double longitude) {
        if (!MainActivity2.isLoadingNewOrder) {
            //zoomToLocation(new LatLng(latitude, longitude));
            //listener.setDataInNewPublicOrder();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        this.googleMap = googleMap;
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        googleMap.getUiSettings().setAllGesturesEnabled(true);
//        data();
    }

    private void setMarkers(LatLng location) {
//        googleMap.addMarker(new MarkerOptions().position(location));
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(location)
//                .zoom(16)
//                .bearing(0)
//                .tilt(0)
//                .build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void data() {
        String currency = AppController.getInstance().getAppSettingsPreferences().getUser()
                .getCountry().getCurrency_code();
        publicOrder = (PublicOrder) requireArguments().getSerializable(AppContent.ORDER_OBJECT);

        assert publicOrder != null;
        //binding.tvOrderDetails.setText(testOrder.getd());
        //binding..setText((getString(R.string.order) + "#" + publicOrder.getInvoice_number()));
        binding.tvFrom.setText(publicOrder.getClient().getName());
        binding.tvDelivery.setText(DecimalFormatterManager.getFormatterInstance()
                .format(publicOrder.getDelivery_cost() + " " + currency));
        binding.tvVat.setText(DecimalFormatterManager.getFormatterInstance()
                .format(publicOrder.getTax() + " " + currency));
        binding.tvSubTotalAfter.setText(DecimalFormatterManager.getFormatterInstance()
                .format(publicOrder.getNote() + " " + currency));
        binding.tvTotal.setText(DecimalFormatterManager.getFormatterInstance()
                .format(publicOrder.getTotal() + " " + currency));
//        binding.tvDriverDues.setText((DecimalFormatterManager.getFormatterInstance()
//                .format(Double.parseDouble(testOrder.getDriver_revenue())) + " " + currency));
//        points.add(new LatLng(Double.parseDouble(this.testOrder.getStore_lat())
//                , Double.parseDouble(this.testOrder.getStore_lng())));
//        points.add(new LatLng(Double.parseDouble(this.testOrder.getDestination_lat())
//                , Double.parseDouble(this.testOrder.getDestination_lng())));
//
//        for (int i = 0; i < points.size(); i++) {
//            setMarkers(points.get(i));
//        }
        setLine();
        //setAttachment();
        active = true;
    }

    public void setLine() {
        lineOptions = new PolylineOptions();
        //lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(Color.BLUE);
        lineOptions.geodesic(true);
      //  googleMap.addPolyline(lineOptions);
    }

    /*private void setAttachment() {
        AttachmentAdapter adapter = new AttachmentAdapter(getActivity()
                , testOrder.getAttachmentArrays(), getChildFragmentManager());
        binding.rvAttachments.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAttachments.setItemAnimator(new DefaultItemAnimator());
        binding.rvAttachments.setAdapter(adapter);
    }*/

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
