package in.ajna.ajnamobile.ajna.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

public class QRCodeActivity extends AppCompatActivity {


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private CodeScanner mCodeScanner;

    QRCodeActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        activity=new QRCodeActivity();
        //Get user values and insert to user object
        String fullName = getIntent().getStringExtra("fullName");
        String address = getIntent().getStringExtra("address");
        String city = getIntent().getStringExtra("city");
        String phoneNumberIndia = getIntent().getStringExtra("phoneNumberIndia");
        final User user = new User(fullName, address, city, phoneNumberIndia);



        CodeScannerView scannerView=findViewById(R.id.scanner_view);
        mCodeScanner=new CodeScanner(this,scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp = getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("code",result.getText());
                        edit.apply();
                        db.collection(result.getText()).document("My Family").set(user);
                        Toast.makeText(QRCodeActivity.this, "Device Registered!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(QRCodeActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

            }
        });






    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();

    }
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();

    }
}
