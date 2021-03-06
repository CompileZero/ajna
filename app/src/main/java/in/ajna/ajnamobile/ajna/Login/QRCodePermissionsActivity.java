package in.ajna.ajnamobile.ajna.Login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import in.ajna.ajnamobile.ajna.R;

public class QRCodePermissionsActivity extends AppCompatActivity {

    private int CAMERA_PERMISSION_CODE=1;

    Button btnScanCode;

    private String fullName,city,phoneNumberIndia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_permissions);

        FloatingActionButton fabHelp = findViewById(R.id.fabHelp);
        fabHelp.setColorFilter(Color.BLUE);

        btnScanCode=findViewById(R.id.btnScanCode);

        fullName = getIntent().getStringExtra("fullName");
        city = getIntent().getStringExtra("city");
        phoneNumberIndia = getIntent().getStringExtra("phoneNumberIndia");

        fabHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserGuideActivity.class);
                startActivity(intent);
            }
        });

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


        }
    }
}
