package in.ajna.ajnamobile.ajna;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_1_ID="SampleChannel";
    public static final String CHANNEL_2_ID="AlwaysOnService";

    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        createNotificationChannel();
    }

    public static synchronized App getInstance() {

        return mInstance;
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(
                    CHANNEL_1_ID,
                    "Ajna",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationChannel AlwaysOnServiceChannel=new NotificationChannel(
                    CHANNEL_2_ID,
                    "AlwaysOnService",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(AlwaysOnServiceChannel);

        }
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener){
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
