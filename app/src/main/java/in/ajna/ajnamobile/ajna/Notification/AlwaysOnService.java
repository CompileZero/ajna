package in.ajna.ajnamobile.ajna.Notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import in.ajna.ajnamobile.ajna.Alarm.AlarmReceiver;
import in.ajna.ajnamobile.ajna.App;
import in.ajna.ajnamobile.ajna.ConnectivityReceiver;
import in.ajna.ajnamobile.ajna.IntrusionDetected;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;
import in.ajna.ajnamobile.ajna.Status;

import static in.ajna.ajnamobile.ajna.MainActivity.MESSAGES_EXTRA;
import static in.ajna.ajnamobile.ajna.App.CHANNEL_2_ID;
import static in.ajna.ajnamobile.ajna.MainActivity.TITLE_EXTRA;


public class AlwaysOnService extends Service implements ConnectivityReceiver.ConnectivityReceiverListener {

    private BroadcastReceiver networkReceiver;
    int notificationId=1;
    String recentMessage;

    private FirebaseDatabase dbRealtime=FirebaseDatabase.getInstance();
    private DatabaseReference deviceRef;

    private int status,detectedStatus;

    private SharedPreferences sp;
    private String code,fullName;
    @Override
    public void onCreate() {
        super.onCreate();
        App.getInstance().setConnectivityListener(this);

        this.startForeground(notificationId,getMyActivityNotification("AJNA","Waiting..."));

        if(ConnectivityReceiver.isConnected()){
            updateNotification("Device Connected " + "\u2022" + " Armed", "Your home is safe!");
        }
        else{
            updateNotification("No internet Connection!","Make sure you are connected to the internet");
        }
        networkReceiver=new ConnectivityReceiver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        getSpecificPreferences();
        deviceRef=dbRealtime.getReference(code);

        ValueEventListener statusListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    status = dataSnapshot.getValue(Status.class).getStatus();
                    detectedStatus = dataSnapshot.getValue(IntrusionDetected.class).getDetectedStatus();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    deviceRef.child("status").setValue(0);
                    deviceRef.child("detectedStatus").setValue(0);
                }

                if (detectedStatus == 1) {
                    startAlarm();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        deviceRef.addValueEventListener(statusListener);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;             //TODO:Change this
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unregisterReceiver(networkReceiver);
        }
        Log.d("AlwaysOnService","Service destroyed");
        stopForeground(true);
        stopSelf(notificationId);

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getMyActivityNotification(String title,String content){
        // The PendingIntent to launch our activity if the user selects
        // this notification
        PendingIntent contentIntent=PendingIntent.getActivity(this,
                0,new Intent(this,MainActivity.class),0);

        return new NotificationCompat.Builder(this,CHANNEL_2_ID)
                .setContentTitle(title)
                .setContentText(content+"\n"+recentMessage)
                .setSmallIcon(R.drawable.ic_icon1)
                .setColor(getResources().getColor(R.color.b2))
                .setOnlyAlertOnce(true) // so when data is updated don't make sound and alert in android 8.0+
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();
    }
    private void updateNotification(String title,String content){
        Notification notification=getMyActivityNotification(title,content);
        Log.d(title,content);

        NotificationManager mNotificationManager=(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId,notification);
        Log.d("notification","notified");
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected){
            Log.d("AlwaysOnService","Network not connected");
            updateNotification("No internet Connection!","Make sure you are connected to the internet");
        }
        else {
            Log.d("AlwaysOnService", "Network connected");
            updateNotification("Device Connected " + "\u2022" + " Armed", "Your home is safe!");
        }

    }

    public void startAlarm()
    {
        Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pi =PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pi);
    }


    private void getSpecificPreferences() {
        sp=this.getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);
        fullName=sp.getString("fullName","User");
        code=sp.getString("code","0");
    }
}
