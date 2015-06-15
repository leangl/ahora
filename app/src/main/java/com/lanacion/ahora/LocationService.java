package com.lanacion.ahora;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lanacion.ahora.activities.MainActivity;
import com.lanacion.ahora.model.BeneficioResponse;
import com.lanacion.ahora.model.MyLocation;
import com.lanacion.ahora.util.DataStore;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildGoogleApiClient();
        return START_STICKY;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            DataStore.getInstance().putObject("last_location", new MyLocation(mLastLocation));
        }

        LocationRequest request = LocationRequest.create();
        request.setInterval(TimeUnit.SECONDS.toMillis(10));
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w("Location", "Location suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Location", "Location failed");
    }

    @Override
    public void onLocationChanged(Location location) {

        DataStore.getInstance().putObject("last_location", new MyLocation(location));

        /*AhoraService.getInstance().getBeneficios(location.getLatitude(), location.getLongitude(), 1000).subscribe(response -> {
            if (!response.isEmpty()) {
                showNearbyNotification(response);
            }
        }, error -> {
            Log.e("Location", "Error fetching benefits.");
        });*/
    }

    private void showNearbyNotification(List<BeneficioResponse> benefits) {
        Ahora.runOnUiThread(() -> {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("Beneficios cercanos!")
                    .setContentText("Hay " + benefits.size() + " beneficios cerca tuyo.")
                    .setAutoCancel(true)
                    .setTicker("Beneficios cercanos!")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(123, builder.build());
        });
    }

}
