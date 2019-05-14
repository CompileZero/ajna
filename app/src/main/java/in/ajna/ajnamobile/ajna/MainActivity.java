package in.ajna.ajnamobile.ajna;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.ref.WeakReference;
import java.util.List;

import in.ajna.ajnamobile.ajna.Activity.RecentMessages;
import in.ajna.ajnamobile.ajna.Activity.RecentMessagesFragmentExpanded;
import in.ajna.ajnamobile.ajna.MyFamily.FamilyFragment;
import in.ajna.ajnamobile.ajna.Settings.SettingsActivity;
import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseDatabase dbRealtime=FirebaseDatabase.getInstance();
    private DatabaseReference deviceRef;
    private CollectionReference recentMessagesRef;

    private String fullName;
    private String code;

    private int longClickDuration=1260;
    private long then;

    RelativeLayout bottomSheetLayout;

    GifImageView gifImageView;
    PleaseWaitDialog pleaseWaitDialog = new PleaseWaitDialog();
    private BottomSheetBehavior bottomSheetBehavior;
    private int status2 = 0, flag = 0;
    private TextView tvStatus;
    private RelativeLayout layout_1, layout_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("MAINACT", "onCreate called");

            setContentView(R.layout.activity_main);
            //Set Custom toolbar
            Toolbar toolbar =findViewById(R.id.toolbar1);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);


            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

            //Get Shared Preferences
            getSpecificPreferences();

        layout_1 = findViewById(R.id.layout_1);
        layout_2 = findViewById(R.id.layout_2);

            bottomSheetLayout=findViewById(R.id.bottomSheetLayout);

            bottomSheetBehavior=BottomSheetBehavior.from(bottomSheetLayout);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        loadFragment(new FamilyFragment());

            gifImageView= findViewById(R.id.gifImageView);
        tvStatus = findViewById(R.id.tvStatus);

            gifImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN) {

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

        final DocumentReference docRef = db.collection(code).document("RecentMessages");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    String message = snapshot.get("message", String.class);
                    tvStatus.setText(message);
                } else {
                    Log.d("MAIN_ACT", "Current data: null");
                }
            }
        });

            deviceRef = dbRealtime.getReference(code);

            deviceRef.child("status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int a =Integer.parseInt(dataSnapshot.getValue().toString());
                    if(a==0 && flag==0){
                        //startFadeTransition(true);
                        status2=0;
                        gifImageView.setImageResource(R.drawable.disarmed);

                    }
                    else if (a==1 && flag==0){
                        //startFadeTransition(false);
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

        Tovuti.from(this).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                if (!isConnected) {
                    layout_1.setVisibility(View.GONE);
                    layout_2.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "No Internet!!!", Toast.LENGTH_SHORT).show();
                } else if (isConnected) {
                    layout_2.setVisibility(View.GONE);
                    layout_1.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Tovuti.from(this).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tovuti.from(this).stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.btnHelp){

        }
        else if(item.getItemId()==R.id.btnSettings){

            Intent intent= new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void getSpecificPreferences() {

        SharedPreferences sp = this.getSharedPreferences("DEVICE_CODE", MODE_PRIVATE);
        fullName=sp.getString("fullName","User");
        code=sp.getString("code","0");
    }

    private void addAutoStartup() { //TODO change this/look into this

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

            status2=1;
            gifImageView.setImageResource(R.drawable.armed);

            //arm device
            ArmAsyncTask armAsyncTask = new ArmAsyncTask(this);
            armAsyncTask.execute();

        }
        else{

            status2=0;
            gifImageView.setImageResource(R.drawable.disarmed);
            //disarm device
            DisarmAsyncTask disarmAsyncTask = new DisarmAsyncTask(this);
            disarmAsyncTask.execute();
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

    //ARM Async Task
    private static class ArmAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<MainActivity> activityWeakReference;

        ArmAsyncTask(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.pleaseWaitDialog.setCancelable(false);
            activity.pleaseWaitDialog.show(activity.getSupportFragmentManager(), null);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }

            activity.recentMessagesRef = activity.db.collection(activity.code).document("RecentMessages").collection("Messages");
            RecentMessages message = new RecentMessages(System.currentTimeMillis(), activity.fullName, "Device Armed");
            activity.db.collection(activity.code).document("RecentMessages").set(message);
            activity.recentMessagesRef.document().set(message);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.deviceRef.child("status").setValue(1);
            activity.pleaseWaitDialog.dismiss();

            Toast.makeText(activity, "Device Armed!", Toast.LENGTH_SHORT).show();  //TODO change this
        }

    }

    //DISARM Async Task
    private static class DisarmAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<MainActivity> activityWeakReference;

        DisarmAsyncTask(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.pleaseWaitDialog.setCancelable(false);
            activity.pleaseWaitDialog.show(activity.getSupportFragmentManager(), null);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }

            activity.recentMessagesRef = activity.db.collection(activity.code).document("RecentMessages").collection("Messages");
            RecentMessages message = new RecentMessages(System.currentTimeMillis(), activity.fullName, "Device Disarmed");
            activity.db.collection(activity.code).document("RecentMessages").set(message);
            activity.recentMessagesRef.document().set(message);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.deviceRef.child("status").setValue(0);
            activity.deviceRef.child("detectedStatus").setValue(0);
            activity.pleaseWaitDialog.dismiss();

            Toast.makeText(activity, "Device Disarmed!", Toast.LENGTH_SHORT).show(); //TODO change this
        }
    }
}
