package in.ajna.ajnamobile.ajna;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import in.ajna.ajnamobile.ajna.Alarm.AlarmReceiver;

public class MessageService extends FirebaseMessagingService {

    private String ARMED_ID = "Armed", DISARMED_ID = "Disarmed", DETECTED_ID = "Detected";
//    private SharedPreferences sp;
//    private String code;

    public MessageService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        getSpecificPreferences();

        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
        }

        if (remoteMessage.getNotification() != null) {
            final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            if (title.equals("Device Armed")) {
                notificationManager.cancel(1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(ARMED_ID, "Device Armed", NotificationManager.IMPORTANCE_HIGH);

                    channel.setDescription("This channel will receive all arming notifications");
                    channel.enableLights(true);
                    channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                    channel.setLightColor(Color.RED);
                    channel.enableVibration(true);

                    notificationManager.createNotificationChannel(channel);
                }

                final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ARMED_ID);

                notificationBuilder
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_icon1)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true);

                notificationManager.notify(1, notificationBuilder.build());
            } else if (title.equals("Device Disarmed")) {
                notificationManager.cancel(2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(DISARMED_ID, "Device Disarmed", NotificationManager.IMPORTANCE_HIGH);

                    channel.setDescription("This channel will receive all disarming notifications");
                    channel.enableLights(true);
                    channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                    channel.setLightColor(Color.RED);
                    channel.enableVibration(true);

                    notificationManager.createNotificationChannel(channel);
                }

                final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, DISARMED_ID);

                notificationBuilder
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_icon1)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true);

                notificationManager.notify(2, notificationBuilder.build());
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MESSAGINGSERVICE", "getinstanceidfailed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        SharedPreferences sp = getSharedPreferences("DEVICE_CODE", MODE_PRIVATE);

                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("token", token);
                        edit.apply();

                        // Log and toast
                        Log.d("TOKEN", token);
                    }
                });
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        //startService();
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String content = data.get("content");


        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(3
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(DETECTED_ID, "Intrusion Detected", NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription("This channel will receive all intrusion related notifications");
            channel.enableLights(true);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.RED);

            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel);
        }

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, DETECTED_ID);

        notificationBuilder
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_icon1)
                .setOnlyAlertOnce(true)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);

        notificationManager.notify(3, notificationBuilder.build());
        startAlarm();
    }

    public void startAlarm() {
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        }

    }

//    private void getSpecificPreferences() {
//        sp = this.getSharedPreferences("DEVICE_CODE", MODE_PRIVATE);
//        this.code = sp.getString("code", "0");
//    }
}

