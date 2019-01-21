package in.ajna.ajnamobile.ajna;


import android.app.AlarmManager;

import android.app.Notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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


import android.os.Bundle;

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



public class MainActivity extends AppCompatActivity implements BottomSheetMyImmediateContacts.BottomSheetListener, BottomSheetRecentMessages.BottomSheetListener, ConnectivityReceiver.ConnectivityReceiverListener {
    @Override
    public void onButtonClicked(String text) {

    }
    private NotificationManagerCompat notificationManager;
    public static final String MESSAGES_EXTRA="messageExtra";
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
    private int status,detectedStatus;

    private static MainActivity inst;

    private BroadcastReceiver networkReceiver;

    FoldingCell fc2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if(isConnected(MainActivity.this))
         //{
            Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
            //Set Custom toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);


            //To set a light status bar with grey icons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = getWindow().getDecorView();
                decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            notificationManager = NotificationManagerCompat.from(this);

            networkReceiver=new ConnectivityReceiver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

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

            ValueEventListener statusListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        status = dataSnapshot.getValue(Status.class).getStatus();
                        detectedStatus = dataSnapshot.getValue(IntrusionDetected.class).getDetectedStatus();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        deviceRef.child("status").setValue(0);
                        deviceRef.child("detectedStatus").setValue(0);
                    }

                    if (status == 1) {
                        btnSwitch.setChecked(true);
                    } else {
                        btnSwitch.setChecked(false);
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
            startService();


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
        App.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected)
        buildDialog(MainActivity.this).show();

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
            mAuth.signOut();
            Toast.makeText(this, "SignedOut", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.btnSettings){

            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
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
        String message="Your home is safe!";

        Intent serviceIntent = new Intent(this,AlwaysOnService.class);
        serviceIntent.putExtra(MESSAGES_EXTRA,message);
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
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null & wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
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

}
