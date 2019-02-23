package in.ajna.ajnamobile.ajna;


import android.app.ActivityManager;
import android.app.AlarmManager;

import android.app.Notification;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.TransitionDrawable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import in.ajna.ajnamobile.ajna.Alarm.AlarmReceiver;
import in.ajna.ajnamobile.ajna.MyFamily.FamilyFragment;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsFragmentCollapsed;
import in.ajna.ajnamobile.ajna.Notification.AlwaysOnService;
import in.ajna.ajnamobile.ajna.Activity.RecentMessages;
import in.ajna.ajnamobile.ajna.Activity.RecentMessagesFragmentExpanded;
import in.ajna.ajnamobile.ajna.Settings.SettingsActivity;
import pl.droidsonroids.gif.GifImageView;


import android.os.Bundle;

import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NotificationManagerCompat notificationManager;
    public static final String MESSAGES_EXTRA="messageExtra";
    public static final String TITLE_EXTRA="titleExtra";
    private static final String NAME_KEY = "Name";

    private static final String PHONE_KEY = "Phone";

    private FragmentManager fragmentManager=getSupportFragmentManager();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private SwitchButton btnSwitch;

    private SharedPreferences sp;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseDatabase dbRealtime=FirebaseDatabase.getInstance();
    private DatabaseReference deviceRef;
    private CollectionReference recentMessagesRef;

    private String fullName;
    private String code;

    private boolean isLongPress=false;
    private int longClickDuration=1260;
    private long then;

    private int status,status2=0,flag=0;

    private static MainActivity inst;

    FoldingCell fc2;

    GifImageView gifImageView;

    RelativeLayout layout_main,bottomSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
            //Set Custom toolbar
            Toolbar toolbar =findViewById(R.id.toolbar1);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);


            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


            notificationManager = NotificationManagerCompat.from(this);
        BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


            //Get Shared Preferences
            getSpecificPreferences();

            //start My Immediate Contacts Fragments
            //initMyImmediateContacts();

            //Folding Cell
            fc2 = findViewById(R.id.folding_cell2);

            btnSwitch = findViewById(R.id.btnSwitch);

            layout_main=findViewById(R.id.layout_main);
            bottomSheetLayout=findViewById(R.id.bottomSheetLayout);

            bottomSheetBehavior=BottomSheetBehavior.from(bottomSheetLayout);

            gifImageView= findViewById(R.id.gifImageView);

            gifImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN) {
                        isLongPress = true;
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        changeButtonTransitionState();
                        then = System.currentTimeMillis();
                    }
                        else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if ((System.currentTimeMillis() - then) > longClickDuration) {
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);

                            changeColor();
                            flag=1;
                            return false;
                        } else {
                            restoreColor();
                            return false;
                        }
                    }
                    return true;
                }
            });

            deviceRef = dbRealtime.getReference(code);

            deviceRef.child("status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int a =Integer.parseInt(dataSnapshot.getValue().toString());
                    if(a==0 && flag==0){
                        startFadeTransition(true);
                        status2=0;
                        gifImageView.setImageResource(R.drawable.disarmed);
                    }
                    else if (a==1 && flag==0){
                        startFadeTransition(false);
                        status2=1;
                        gifImageView.setImageResource(R.drawable.armed);
                    }
                    else if(flag==1){
                        flag=0;
                    }
                    Log.d("DATACHANGEMAINACTIVITY", String.valueOf(a));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            btnSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    if (isChecked == true) {
                        if (status == 0) {
                            sendArmedMessage();
                        }

                    } else if (isChecked == false) {
                        if (status == 1) {
                            sendDisarmedMessage();
                        }
                    }
                }


            });

        /*final FoldingCell fc4=findViewById(R.id.folding_cell4);
        fc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc4.toggle(false);
            }
        });*/


            //if(!isMyServiceRunning(AlwaysOnService.class)) startService();


        //}
        //else buildDialog(MainActivity.this).show();
    }

    private void startFadeTransition(Boolean reverseAnimation) {
        TransitionDrawable transition = (TransitionDrawable) layout_main.getBackground();
        if(!reverseAnimation){
            transition.startTransition(1000);
        }
        else{
            transition.reverseTransition(1000);
        }

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
            SharedPreferences sp=getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);
            String phoneNumberIndia = sp.getString("phoneNumber","0");
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("fullName",null);
            edit.putString("code",null);
            edit.putString("phoneNumber",null);
            edit.putString("isSignedIn","0");
            edit.apply();

            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
            stopService();
            finishAndRemoveTask();
        }
        else if(item.getItemId()==R.id.btnSettings){

            Intent intent= new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        switch (item.getItemId()) {
            case R.id.navigation_status:
                break;

            case R.id.navigation_contacts:
                fragment = new MyImmediateContactsFragmentCollapsed();
                break;

            case R.id.navigation_activity:
                fragment = new RecentMessagesFragmentExpanded();
                break;

            case R.id.navigation_family:
                fragment = new FamilyFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void changeColor(){
        if(status2==0){
            startFadeTransition(false);
            status2=1;
            gifImageView.setImageResource(R.drawable.armed);
            sendArmedMessage();
        }
        else{
            startFadeTransition(true);
            status2=0;
            gifImageView.setImageResource(R.drawable.disarmed);
            sendDisarmedMessage();
        }
    }
    private void changeButtonTransitionState(){
        if(status2==0){
            gifImageView.setImageResource(R.drawable.trans_arm);

        }
        else{
            gifImageView.setImageResource(R.drawable.trans_disarm);
        }
    }
    private void restoreColor(){
        if(status2==0){
            gifImageView.setImageResource(R.drawable.disarmed);
        }
        else{
            gifImageView.setImageResource(R.drawable.armed);
        }
    }

}
