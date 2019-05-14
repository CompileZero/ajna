package in.ajna.ajnamobile.ajna.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;

import in.ajna.ajnamobile.ajna.Activity.RecentMessages;
import in.ajna.ajnamobile.ajna.Login.AgreeContinueActivity;
import in.ajna.ajnamobile.ajna.Login.DeviceInitActivity;
import in.ajna.ajnamobile.ajna.PleaseWaitDialog;
import in.ajna.ajnamobile.ajna.R;


public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbarSettings;

    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            return false;
        }
    };
    PleaseWaitDialog pleaseWaitDialog = new PleaseWaitDialog();
    Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {


            return true;
        }
    };
    private SharedPreferences sp;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase dbRealtime = FirebaseDatabase.getInstance();
    private DatabaseReference deviceRef;
    private CollectionReference recentMessagesRef, collRef;
    private String fullName;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentSettings, new SettingsFragment())
                .commit();

        toolbarSettings=findViewById(R.id.toolbarSettings);
        toolbarSettings.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSpecificPreferences();

    }

    private void getSpecificPreferences() {
        sp = this.getSharedPreferences("DEVICE_CODE", MODE_PRIVATE);
        fullName = sp.getString("fullName", "User");
        code = sp.getString("code", "0");

    }

     public static class SettingsFragment extends PreferenceFragmentCompat {

         @Override
         public boolean onPreferenceTreeClick(Preference preference) {

             return super.onPreferenceTreeClick(preference);
         }

         @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings, rootKey);

             Preference sign_out = findPreference("sign_out");
             sign_out.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                 @Override
                 public boolean onPreferenceClick(Preference preference) {
                     SignOutAsyncTask signOutAsyncTask = new SignOutAsyncTask(getActivity());
                     signOutAsyncTask.execute();
                     return false;
                 }
             });
             Preference reconfigure_device = findPreference("reconfigure_device");
             reconfigure_device.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                 @Override
                 public boolean onPreferenceClick(Preference preference) {
                     Intent intent = new Intent(getContext(), DeviceInitActivity.class);
                     startActivity(intent);
                     return true;
                 }
             });
         }

     }

    private static class SignOutAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<SettingsActivity> activityWeakReference;

        SignOutAsyncTask(FragmentActivity activity) {
            activityWeakReference = new WeakReference<SettingsActivity>((SettingsActivity) activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SettingsActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.pleaseWaitDialog.setCancelable(false);
            activity.pleaseWaitDialog.show(activity.getSupportFragmentManager(), null);
            RecentMessages recentMessages = new RecentMessages(System.currentTimeMillis(), activity.fullName, "Family Member Left");
            activity.db.collection(activity.code).document("RecentMessages").collection("Messages").document().set(recentMessages);

            SharedPreferences sp = activity.getSharedPreferences("DEVICE_CODE", MODE_PRIVATE);
            //String phoneNumberIndia = sp.getString("phoneNumber","0");
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("fullName", null);
            edit.putString("code", null);
            edit.putString("phoneNumber", null);
            edit.putString("isSignedIn", "0");
            edit.putBoolean("firstOpen", true);
            edit.apply();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            final SettingsActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            activity.deviceRef = activity.dbRealtime.getReference(activity.code);
            activity.deviceRef.child("tokens").child(activity.fullName).removeValue();


            activity.collRef = activity.db.collection(activity.code).document("MyFamily").collection("members");
            activity.collRef.whereEqualTo("fullName", activity.fullName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().isEmpty()) {
                                    return;
                                } else {
                                    String id = task.getResult().getDocuments().get(0).getId();
                                    activity.collRef.document(id).delete();
                                }
                            } else {
                                Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //activity.checkForExistingAccount();

//            activity.db.collection(activity.code).document("MyFamily").collection("members").whereEqualTo("fullName",activity.fullName)
//            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()){
//                        if(task.getResult().getDocuments()!=null){
//                            task.getResult().getDocuments().get(0);
//                        }
//
//
//                    }
//                }
//            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SettingsActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.mAuth.signOut();
            activity.pleaseWaitDialog.dismiss();
            Toast.makeText(activity, "Signed out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity.getApplicationContext(), AgreeContinueActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finishAndRemoveTask();


        }

    }
}
