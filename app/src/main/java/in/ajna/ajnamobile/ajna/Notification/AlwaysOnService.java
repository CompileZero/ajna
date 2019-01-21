package in.ajna.ajnamobile.ajna.Notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

import static in.ajna.ajnamobile.ajna.MainActivity.MESSAGES_EXTRA;
import static in.ajna.ajnamobile.ajna.App.CHANNEL_2_ID;


public class AlwaysOnService extends Service {

    Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String message=intent.getStringExtra(MESSAGES_EXTRA);

        Intent notificationIntent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,
                0,notificationIntent,0);

        notification = new NotificationCompat.Builder(this,CHANNEL_2_ID)
                .setContentTitle("AJNA")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_icon1)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.b2))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your battery is at sufficient levels! Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")
                .setBigContentTitle("DEVICE ARMED")
                )
                .build();

        startForeground(1,notification);
        return START_NOT_STICKY;             //TODO:Change this
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
