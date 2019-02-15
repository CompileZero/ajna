package in.ajna.ajnamobile.ajna.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import in.ajna.ajnamobile.ajna.R;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.geniusforapp.fancydialog.FancyAlertDialog;

public class QRCodePermissionsActivity extends AppCompatActivity {

    private int CAMERA_PERMISSION_CODE=1;

    Button btnScanCode;

    private String fullName,city,phoneNumberIndia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_permissions);

        btnScanCode=findViewById(R.id.btnScanCode);

        fullName = getIntent().getStringExtra("fullName");
        city = getIntent().getStringExtra("city");
        phoneNumberIndia = getIntent().getStringExtra("phoneNumberIndia");

        btnScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(QRCodePermissionsActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestCameraPermission();
                }
                else{
                    Intent intent=new Intent(QRCodePermissionsActivity.this,QRCodeActivity.class);
                    intent.putExtra("fullName",fullName);
                    intent.putExtra("city",city);
                    intent.putExtra("phoneNumberIndia",phoneNumberIndia);
                    startActivity(intent);
                }

            }
        });
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(QRCodePermissionsActivity.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(QRCodePermissionsActivity.this,QRCodeActivity.class);
                intent.putExtra("fullName",fullName);
                intent.putExtra("city",city);
                intent.putExtra("phoneNumberIndia",phoneNumberIndia);
                startActivity(intent);
            }
            else finishAndRemoveTask();

        }
    }
}
