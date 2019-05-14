package in.ajna.ajnamobile.ajna.Alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;

import in.ajna.ajnamobile.ajna.Activity.RecentMessages;
import in.ajna.ajnamobile.ajna.IntrusionDetected;
import in.ajna.ajnamobile.ajna.PleaseWaitDialog;
import in.ajna.ajnamobile.ajna.R;

public class AlarmActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    Ringtone ringtone;
    Vibrator v;

    PleaseWaitDialog pleaseWaitDialog = new PleaseWaitDialog();
    private AppCompatButton btnDisarm;
    private SharedPreferences sp;
    private String code;
    private String fullName;
    private FirebaseDatabase dbRealtime=FirebaseDatabase.getInstance();
    private DatabaseReference deviceRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int detectedStatus;

    private int ringerMode;

    AudioManager audioManager;
    private CollectionReference recentMessagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        btnDisarm = findViewById(R.id.btnDisarm);

        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {

                //set the ringer mode to normal(loud)
                //audioManager=(AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE);
                //ringerMode = audioManager.getRingerMode();
                //audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                //setup ringtone
                Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                ringtone = RingtoneManager.getRingtone(AlarmActivity.this, alarmUri);

                ringtone.play();
                //TODO:

                //setup vibration
                // add this in manifest - <uses-permission android:name="android.permission.VIBRATE" />
                v =(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                long[] vibPattern=new long[]{0,1000,1000};
                v.vibrate(vibPattern,1);
            }
        };
        handler.post(runnable);
        getSpecificPreferences();
        deviceRef = dbRealtime.getReference(code);

        ValueEventListener detectedListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                detectedStatus=dataSnapshot.getValue(IntrusionDetected.class).getDetectedStatus();

                if(detectedStatus==0){
                    v.cancel();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        deviceRef.addValueEventListener(detectedListener);

        btnDisarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisarmAsyncTask disarmAsyncTask = new DisarmAsyncTask(AlarmActivity.this);
                disarmAsyncTask.execute();
            }
        });


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                |WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                |WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        finish();
        startActivity(new Intent(intent));
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //reset the ringer mode to the user's previous ringer mode
        //audioManager.setRingerMode(ringerMode);

        if(handler!=null)
            handler.removeCallbacks(runnable);

        if(ringtone.isPlaying())
            ringtone.stop();

        v.cancel();
    }

    private void getSpecificPreferences() {
        sp=this.getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);
        fullName = sp.getString("fullName", "User");
        code=sp.getString("code","0");
    }

    private void sendDisarmedMessage() {

        recentMessagesRef = db.collection(code).document("RecentMessages").collection("Messages");
        RecentMessages message = new RecentMessages(System.currentTimeMillis(), fullName, "Device Disarmed");

        db.collection(code).document("RecentMessages").set(message);
        recentMessagesRef.document().set(message);

        deviceRef.child("status").setValue(0);

    }

    private static class DisarmAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<AlarmActivity> activityWeakReference;

        DisarmAsyncTask(AlarmActivity activity) {
            activityWeakReference = new WeakReference<AlarmActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlarmActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.pleaseWaitDialog.setCancelable(false);
            activity.pleaseWaitDialog.show(activity.getSupportFragmentManager(), null);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            AlarmActivity activity = activityWeakReference.get();
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

            AlarmActivity activity = activityWeakReference.get();
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


