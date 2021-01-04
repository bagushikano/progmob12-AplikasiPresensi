package com.progmobklp12.aplikasipresensi.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.progmobklp12.aplikasipresensi.App;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.activity.LoginActivity;
import com.progmobklp12.aplikasipresensi.activity.MainActivity;
import com.progmobklp12.aplikasipresensi.activity.SplashActivity;

import java.util.Map;

public class FirebaseMessaging extends FirebaseMessagingService {
    private int loginStatus;
    private String username;
    SharedPreferences loginPreferences;

    NotificationChannel channel;
    NotificationManagerCompat notificationManagerCompat;
    int i=0;
    int j=0;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        loginStatus = loginPreferences.getInt("login_status", 0);
        username = loginPreferences.getString("username", "kosong");

        Map<String, String> data = remoteMessage.getData();
        if (loginStatus == 2) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            if (data.get("type").equals("all")) {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "presensi")
                        .setSmallIcon(R.drawable.ic_baseline_content_paste_24)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);
                notificationManagerCompat = NotificationManagerCompat.from(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    channel = new NotificationChannel("presensi", "Default", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManagerCompat.createNotificationChannel(channel);
                }
                notificationManagerCompat.notify(i+1, notificationBuilder.build());
            }
            else if (data.get("type").equals("notall")){
                if (data.get("username").equals(username)){
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "approval")
                            .setSmallIcon(R.drawable.ic_baseline_content_paste_24)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true);
                    notificationManagerCompat = NotificationManagerCompat.from(this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        channel = new NotificationChannel("approval", "Status approval presensi", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManagerCompat.createNotificationChannel(channel);
                    }
                    notificationManagerCompat.notify(j+1, notificationBuilder.build());
                }
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
