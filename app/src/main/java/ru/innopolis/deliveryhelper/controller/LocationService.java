package ru.innopolis.deliveryhelper.controller;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;
import ru.innopolis.deliveryhelper.model.dataframes.request.LocationUpdateModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.AcceptanceModel;

import static ru.innopolis.deliveryhelper.model.PlainConsts.internal_server_error_message;

/**
 * Code based on example provided on
 * <p>
 * https://github.com/codepath/android_guides/issues/220
 */

public class LocationService extends Service {
    private static final String TAG = "LocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private ApiInterface api;
    private Gson gson;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.i(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
            String locationResults = String.format("%s;%s", Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            notifyLocation(locationResults);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);
        }
    }


    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate");

        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[1]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        gson = new Gson();
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    /**
     * Send current coordinate of given device to server
     *
     * @param latlng location in format 'latitude;longitude', e.g. '55.66;44.56'
     */
    public void notifyLocation(String latlng) {
        try {
            LocationUpdateModel gtrm = new LocationUpdateModel(SafeStorage.getUsername(), latlng);
            Call<AcceptanceModel> call = api.requestUpdateLocation(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<AcceptanceModel>() {
                @Override
                public void onResponse(Call<AcceptanceModel> call, Response<AcceptanceModel> response) {
                    if (response.body() != null) {

                        AcceptanceModel irm = response.body();
                        if (irm.getError() == null || irm.getError().equals("null")) {

                        } else {
                            Log.e(TAG, irm.getError());
                        }
                    } else {
                        Log.e(TAG, internal_server_error_message);
                    }
                }

                @Override
                public void onFailure(Call<AcceptanceModel> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    call.cancel();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}