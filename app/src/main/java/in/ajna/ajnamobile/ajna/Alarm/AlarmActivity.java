package in.ajna.ajnamobile.ajna.Alarm;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import in.ajna.ajnamobile.ajna.IntrusionDetected;
import in.ajna.ajnamobile.ajna.R;
import pl.droidsonroids.gif.GifImageView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class AlarmActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    Ringtone ringtone;
    Vibrator v;

    GifImageView gifCancel;

    private SharedPreferences sp;
    private String code;

    private FirebaseDatabase dbRealtime=FirebaseDatabase.getInstance();
    private DatabaseReference deviceRef;

    private int detectedStatus;

    private int ringerMode;

    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        gifCancel=findViewById(R.id.gifCancel);
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

        gifCancel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deviceRef.child("detectedStatus").setValue(0);
                finish();
                return true;
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
        code=sp.getString("code","0");
    }
}
