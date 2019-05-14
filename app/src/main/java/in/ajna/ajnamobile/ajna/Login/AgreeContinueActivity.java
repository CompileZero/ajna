package in.ajna.ajnamobile.ajna.Login;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import in.ajna.ajnamobile.ajna.AutoStartDialog;
import in.ajna.ajnamobile.ajna.R;

public class AgreeContinueActivity extends AppCompatActivity {

    Button btnAgree;
    SharedPreferences sp;
    Boolean firstOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_continue);

        btnAgree=findViewById(R.id.btnAgree);
        sp = getSharedPreferences("DEVICE_CODE", MODE_PRIVATE);
        firstOpen = sp.getBoolean("firstOpen", true);

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edit = sp.edit();

                if (firstOpen) {
                    String manufacturer = android.os.Build.MANUFACTURER;
                    if (("xiaomi".equalsIgnoreCase(manufacturer) && (!(Build.MODEL.equalsIgnoreCase("MI A1")) || (Build.MODEL.equalsIgnoreCase("MI A2")) || (Build.MODEL.equalsIgnoreCase("A2 LITE"))))
                            || "oppo".equalsIgnoreCase(manufacturer)
                            || "vivo".equalsIgnoreCase(manufacturer)
                            || "Letv".equalsIgnoreCase(manufacturer)
                            || "Honor".equalsIgnoreCase(manufacturer)) {
                        AutoStartDialog dialog = new AutoStartDialog();
                        dialog.show(getSupportFragmentManager(), "Auto Start Dialog");
                        edit.putBoolean("firstOpen", false);
                        edit.apply();
                    } else {
                        edit.putBoolean("firstOpen", false);
                        edit.apply();
                        Intent intent = new Intent(AgreeContinueActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(AgreeContinueActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
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
            if (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }
}
