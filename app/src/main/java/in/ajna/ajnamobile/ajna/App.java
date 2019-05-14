package in.ajna.ajnamobile.ajna;


import android.app.Application;

public class App extends Application {

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
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            NotificationChannel statusServiceChannel=new NotificationChannel(
//                    CHANNEL_ID,
//                    "Status Service",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//
//            NotificationManager manager=getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(statusServiceChannel);


//        }
    }
}
