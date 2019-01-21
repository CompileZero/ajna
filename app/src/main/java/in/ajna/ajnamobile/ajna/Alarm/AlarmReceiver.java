package in.ajna.ajnamobile.ajna.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

import static android.content.Context.ALARM_SERVICE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent ringer = new Intent(context,AlarmActivity.class);
        ringer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(ringer);


    }


}
