package com.lanacion.ahora;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.lanacion.ahora.activities.MainActivity;
import com.lanacion.ahora.model.BeneficioResponse;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class PushReceiverService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Pusher pusher = new Pusher("df698f7442932f863424");

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting!");
            }
        }, ConnectionState.ALL);

        // Subscribe to a channel
        Channel channel = pusher.subscribe("test_channel");

        Handler h = new Handler();
        // Bind to listen for events called "my-event" sent to "my-channel"
        channel.bind("my_event", (channel1, event, data) -> {
            System.out.println("Received event with data: " + data);
            h.post(new Runnable() {
                @Override
                public void run() {
                    BeneficioResponse beneficio = new Gson().fromJson(data, BeneficioResponse.class);
                    showNotification(beneficio);
                }
            });
        });

        // Disconnect from the service (or become disconnected my network conditions)
        pusher.disconnect();

        // Reconnect, with all channel subscriptions and event bindings automatically recreated
        pusher.connect();
        // The state change listener is notified when the connection has been re-established,
        // the subscription to "my-channel" and binding on "my-event" still exist.

        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification(final BeneficioResponse b) {
        Ahora.runOnUiThread(() -> {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(MainActivity.BENEFICIO, b);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("Nuevo beneficio!")
                    .setContentText(b.beneficio.descripcion)
                    .setAutoCancel(true)
                    .setTicker("Nuevo beneficio!")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.notification_dot);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(123, builder.build());
        });
    }

    private void showNearbyNotification(BeneficioResponse benefit) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Nuevo Beneficio!")
                .setContentText(benefit.beneficio.descripcion)
                .setAutoCancel(true)
                .setTicker("Nuevo Beneficio!")
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(123, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
