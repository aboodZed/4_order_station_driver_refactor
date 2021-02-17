package com.webapp.a4_order_station_driver.utils.location;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.provider.Settings;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

/** Utility class to ask for location services */
class LocationServicesManager {

    protected Activity activity;
    protected LocationSettingsRequest.Builder locationBuilder;

    LocationServicesManager(Activity activity) {
        this.activity = activity;
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationBuilder = new LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest);
        locationBuilder.setAlwaysShow(true);
    }

    void askForLocationServices(final int requestCode) {
        Task<LocationSettingsResponse> result =
                LocationServices
                        .getSettingsClient(activity)
                        .checkLocationSettings(locationBuilder.build());

        result.addOnCompleteListener(task -> {
            try {
                task.getResult(ApiException.class);
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            // Show dialog to turn on location services
                            resolvable.startResolutionForResult(activity, requestCode);
                        } catch (IntentSender.SendIntentException |ClassCastException ignored) {}
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        openLocationSettings();
                        break;
                }
            }
        });
    }

    // Get location services the old fashioned way
    protected void openLocationSettings() {
        ToolUtils.showLongToast(activity.getString(R.string.turn_on_location_services), activity);
        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}