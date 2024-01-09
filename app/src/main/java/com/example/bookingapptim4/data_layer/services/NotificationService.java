package com.example.bookingapptim4.data_layer.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.bookingapptim4.BuildConfig;
import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.notifications.Notification;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class NotificationService extends Service {

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";

    private static final String CHANNEL_ID = "Notification channel";

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    private int notificationID = 50;

    private WebSocket webSocket;

    private NotificationManager notificationManager;
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = getSystemService(NotificationManager.class);
        if (intent != null) {
            String action = intent.getAction();
            Log.i("SERVICE STARTED", "YES");

            if (ACTION_START_FOREGROUND_SERVICE.equals(action)) {
                startForegroundService();
                startWebSocket();
            }
        }
        return START_NOT_STICKY;
    }

    private void startWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://"+ BuildConfig.IP_ADDR +":8080/notifications-socket")
                .build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // WebSocket connection is established
                Log.d("WebSocket", "Connection opened");
                // Subscribe to the topic
//                String topic = "/user/notification-publisher/" + UserUtils.getCurrentUser().getId();  // Replace getUserId() with your actual method to get the user ID
                String topic = "/notification-publisher" ;
                webSocket.send("SUBSCRIBE " + topic);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("WebSocket", "Connection Message");
                // Handle incoming messages
                // The 'text' parameter contains the message received from the server
                // Handle the notification logic here
                // Handle incoming messages
                Gson gson = new Gson();
                Notification notification = gson.fromJson(text, Notification.class);
                showNotification(notification);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d("WebSocket", "Connection Closed");
                // WebSocket connection is closed
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.d("WebSocket", "Connection Failure");
                // Handle connection failure
            }
        });
    }

    private void startForegroundService()
    {
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.");

        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationID, intent, PendingIntent.FLAG_MUTABLE);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.");
        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always," +
                "it can be controlled by user via notification.");
        // Set big text style.
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIconBitmap);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

        // Start foreground service.
        startForeground(notificationID, builder.build());
    }

    private void showNotification(Notification notification) {
        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationID, intent, PendingIntent.FLAG_MUTABLE);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        builder.setContentTitle(notification.getType().toString());
        builder.setContentText(notification.getMessage());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIconBitmap);
        builder.setContentIntent(pendingIntent);

        // Trigger the notification.
        notificationManager.notify(notificationID, builder.build());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Stop foreground service.", Toast.LENGTH_LONG).show();

        if (webSocket != null) {
            Log.d("WebSocket", "Connection closed");
            webSocket.close(1000, "Service destroyed");
        }

        stopForeground(true);
        stopSelf();
    }
}