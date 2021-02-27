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
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.MyLocation;
import com.webapp.a4_order_station_driver.utils.AppController;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GPSTracking {

    private Context context;
    //private OrderStation order;
    //private PublicOrder publicOrder;
    private LocationManager lm;
    private LocationListener locationListener;
    //public static GPSTracking gpsTracking;
    private static GPSTracking myGpsTracking;

    public GPSTracking(Context context) {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
    }

    /*public GPSTracking(Context context, OrderStation order) {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
        this.order = order;
    }

    public GPSTracking(Context context, PublicOrder publicOrder) {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
        this.publicOrder = publicOrder;
    }*/

    public static GPSTracking getInstance(Context context) {
        if (myGpsTracking == null) {
            myGpsTracking = new GPSTracking(context);
        }
        return myGpsTracking;
    }

   /* public static GPSTracking getInstance(Context context, OrderStation order) {
        if (gpsTracking == null) {
            gpsTracking = new GPSTracking(context, order);
        }
        return gpsTracking;
    }

    public static GPSTracking getInstance(Context context, PublicOrder publicOrder) {
        if (gpsTracking == null) {
            gpsTracking = new GPSTracking(context, publicOrder);
        }
        return gpsTracking;
    }*/

    public void startMyGPSTracking() {
        MyLocation myLocation = new MyLocation();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("delivery_app_tracking")
                .child(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getId() + "");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, R.string.unable_location, Toast.LENGTH_SHORT).show();
            return;
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                HashMap<String, String> map = new HashMap<>();
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    myLocation.setLat(location.getLatitude());
                    map.put("lat", "" + location.getLatitude());
                    myLocation.setLng(location.getLongitude());
                    map.put("lng", "" + location.getLongitude());
                    myLocation.setName(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getName());
                    myLocation.setMobile(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getMobile());
                    myLocation.setDriver_id(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getId());
                    myLocation.setCountry_id(AppController.getInstance().getAppSettingsPreferences().getCountry().getId());
                    if (AppController.getInstance().getAppSettingsPreferences().getLogin()
                            .getUser().getIs_online().equals(MainActivity.online)) {
                        if (AppController.getInstance().getAppSettingsPreferences().getTrackingOrder() == null) {
                            myLocation.setStatus("online");
                        } else {
                            myLocation.setStatus("busy");
                        }
                    } else {
                        myLocation.setStatus("offline");
                    }
                    db.setValue(myLocation);
                    map.put("driver_id", AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getId() + "");
                    AppController.getInstance().getApi().updateLocation(map)
                            .enqueue(new Callback<Message>() {
                                @Override
                                public void onResponse(Call<Message> call, Response<Message> response) {
                                    Log.e("trackInServer", myLocation.toString());
                                }

                                @Override
                                public void onFailure(Call<Message> call, Throwable t) {

                                }
                            });
                    Log.e("trackings", myLocation.toString());
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
/*
    public void startGPSTracking() {
        final DatabaseReference db;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, R.string.unable_location, Toast.LENGTH_SHORT).show();
            return;
        }
        if (publicOrder != null) {
            db = FirebaseDatabase.getInstance().getReference("PublicTracking").child(publicOrder.getId() + "");
        } else {
            db = FirebaseDatabase.getInstance().getReference("Tracking").child(order.getId() + "");
        }
        com.webapp.a4_order_station_driver.models.GPSLocation userlocation = new com.webapp.a4_order_station_driver.models.GPSLocation();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(GPSLocation location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    userlocation.setLat(location.getLatitude());
                    userlocation.setLng(location.getLongitude());
                    db.setValue(userlocation);
                    Log.e("trackings", userlocation.toString());
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
        gpsTracking = null;
        AppController.getInstance().getAppSettingsPreferences().removeOrder();
    }*/

    public void removeMyUpdates() {
        if (this.locationListener != null)
            this.lm.removeUpdates(this.locationListener);
        myGpsTracking = null;
    }
}