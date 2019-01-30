package in.ajna.ajnamobile.ajna;


import android.app.ActivityManager;
import android.app.AlarmManager;

import android.app.Notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import in.ajna.ajnamobile.ajna.Alarm.AlarmReceiver;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.BottomSheetMyImmediateContacts;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsFragmentCollapsed;
import in.ajna.ajnamobile.ajna.Notification.AlwaysOnService;
import in.ajna.ajnamobile.ajna.RecentMessages.BottomSheetRecentMessages;
import in.ajna.ajnamobile.ajna.RecentMessages.RecentMessages;
import in.ajna.ajnamobile.ajna.Settings.SettingsActivity;


import android.os.Bundle;

import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;


import com.alexfu.countdownview.CountDownView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ramotion.foldingcell.FoldingCell;
import com.suke.widget.SwitchButton;

import java.util.List;


public class MainActivity extends AppCompatActivity implements BottomSheetMyImmediateContacts.BottomSheetListener, BottomSheetRecentMessages.BottomSheetListener{
    @Override
    public void onButtonClicked(String text) {

    }
    private NotificationManagerCompat notificationManager;
    public static final String MESSAGES_EXTRA="messageExtra";
    public static final String TITLE_EXTRA="titleExtra";
    private static final String NAME_KEY = "Name";

    private static final String PHONE_KEY = "Phone";

    private FragmentManager fragmentManager=getSupportFragmentManager();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private SwipeButton btnSwipe;

    private SwitchButton btnSwitch;

    private CountDownView tvCountdown;

    private SharedPreferences sp;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseDatabase dbRealtime=FirebaseDatabase.getInstance();
    private DatabaseReference deviceRef;
    private CollectionReference recentMessagesRef;

    private String fullName;
    private String code;

    private int status;

    private static MainActivity inst;

    FoldingCell fc2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
            //Set Custom toolbar
            Toolbar toolbar =findViewById(R.id.toolbar1);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);


            //To set a light status bar with grey icons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = getWindow().getDecorView();
                decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            notificationManager = NotificationManagerCompat.from(this);


            //Get Shared Preferences
            getSpecificPreferences();

            //start My Immediate Contacts Fragments
            initMyImmediateContacts();

            //Folding Cell
            fc2 = findViewById(R.id.folding_cell2);

            btnSwipe = findViewById(R.id.btnSwipe);

            btnSwitch = findViewById(R.id.btnSwitch);

            tvCountdown = findViewById(R.id.tvCountdown);

            deviceRef = dbRealtime.getReference(code);

            btnSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    if (isChecked == true) {
                        if (status == 0) {
                            tvCountdown.start();
                            sendArmedMessage();

                        }

                    } else if (isChecked == false) {
                        if (status == 1) {
                            sendDisarmedMessage();
                            tvCountdown.stop();
                            tvCountdown.reset();
                        }
                    }
                }


            });

            btnSwipe.setOnFinishListener(new SwipeButton.OnFinishListener() {
                @Override
                public void onFinish() {
                    Toast.makeText(MainActivity.this, "Armed!", Toast.LENGTH_SHORT).show();
                }
            });
            btnSwipe.setOnReverseListener(new SwipeButton.OnReverseListener() {
                @Override
                public void onReverse() {
                    Toast.makeText(MainActivity.this, "Disarmed!", Toast.LENGTH_SHORT).show();
                }
            });
        /*final FoldingCell fc4=findViewById(R.id.folding_cell4);
        fc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc4.toggle(false);
            }
        });*/


            if(!isMyServiceRunning(AlwaysOnService.class)) startService();


        //}
        //else buildDialog(MainActivity.this).show();
    }

    private void initMyImmediateContacts() {
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.fragmentContainerMyImmediateContacts,new MyImmediateContactsFragmentCollapsed(),"ImmContactsCollapsed")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst=this;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.btnHelp){
            stopService();
            Toast.makeText(this, "SignedOut and Exiting", Toast.LENGTH_SHORT).show();
            mAuth.signOut();

            finishAndRemoveTask();
        }
        else if(item.getItemId()==R.id.btnSettings){

            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            Intent intenet= new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intenet);
        }
        return super.onOptionsItemSelected(item);
    }


    public void sendOnChannel1(){
        Notification notification = new NotificationCompat.Builder(this,App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_icon1)
                .setContentTitle("Hello this is a text!")
                .setContentText("This is the description.")
                .build();
        notificationManager.notify(2,notification);
    }
    public void startService(){

        Intent serviceIntent = new Intent(this,AlwaysOnService.class);
        startService(serviceIntent);
    }

    public void stopService(){
        Intent serviceIntent = new Intent(this,AlwaysOnService.class);
        stopService(serviceIntent);

    }
    private void sendArmedMessage() {
        recentMessagesRef=db.collection(code).document("RecentMessages").collection("Messages");
        RecentMessages message=new RecentMessages(System.currentTimeMillis(),fullName+" armed the device!");

        db.collection(code).document("RecentMessages").set(message);
        recentMessagesRef.document().set(message);
        deviceRef.child("status").setValue(1);
    }

    private void sendDisarmedMessage(){

        recentMessagesRef=db.collection(code).document("RecentMessages").collection("Messages");
        RecentMessages message=new RecentMessages(System.currentTimeMillis(),fullName+" disarmed the device!");

        db.collection(code).document("RecentMessages").set(message);
        recentMessagesRef.document().set(message);


        deviceRef.child("status").setValue(0);

    }
    private void getSpecificPreferences() {
        sp=this.getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);
        fullName=sp.getString("fullName","User");
        code=sp.getString("code","0");
    }

    public static MainActivity instance() {
        return inst;
    }

    public void startAlarm()
    {
        Intent i = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pi =PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pi);
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }
    }


}
