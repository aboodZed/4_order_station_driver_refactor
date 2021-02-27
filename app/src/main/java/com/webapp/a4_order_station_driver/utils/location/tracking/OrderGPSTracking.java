package com.webapp.a4_order_station_driver.utils.location.tracking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.GPSLocation;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;

public class OrderGPSTracking {

    private Order order;
    private Context context;
    private LocationManager lm;
    private LocationListener locationListener;

    public OrderGPSTracking(Context context, Order order) {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
        this.order = order;
    }

    public void startGPSTracking() {
        final DatabaseReference db;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, R.string.unable_location, Toast.LENGTH_SHORT).show();
            return;
        }
        if (order.getType().equals(AppContent.TYPE_ORDER_PUBLIC)) {
            db = FirebaseDatabase.getInstance().getReference(AppContent.PUBLIC_TRACKING_INSTANCE).child(order.getId() + "");
        } else {
            db = FirebaseDatabase.getInstance().getReference(AppContent.TRACKING_INSTANCE).child(order.getId() + "");
        }
        GPSLocation gpsLocation = new GPSLocation();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    gpsLocation.setLat(location.getLatitude());
                    gpsLocation.setLng(location.getLongitude());
                    db.setValue(gpsLocation);
                    Log.e(getClass().getName() + " : tracking", gpsLocation.toString());
                } else {
                    lm.removeUpdates(locationListener);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }

    public void removeUpdates() {
        if (this.locationListener != null)
            this.lm.removeUpdates(this.locationListener);
        AppController.getInstance().getAppSettingsPreferences().removeOrder();
    }
}
