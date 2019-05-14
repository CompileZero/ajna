package in.ajna.ajnamobile.ajna.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import in.ajna.ajnamobile.ajna.Activity.RecentMessages;
import in.ajna.ajnamobile.ajna.R;
import in.ajna.ajnamobile.ajna.WrongQrCodeDialog;


public class QRCodeActivity extends AppCompatActivity implements WrongQrCodeDialog.ExitListener {

    private QRCodeActivity activity;

    private MaterialButton btnClickHere;
    private CodeScanner mCodeScanner;
    RelativeLayout layout_2;
    private String fullName,city,phoneNumberIndia, code,token;

    private FirebaseFirestore db;
    private CollectionReference collRef;
    private User user;
    private CodeScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qrcode);
        db = FirebaseFirestore.getInstance();

        scannerView = findViewById(R.id.scanner_view);

        activity=new QRCodeActivity();
        startScanner();

        //Get user values and insert to user object
        fullName = getIntent().getStringExtra("fullName");
        city = getIntent().getStringExtra("city");
        phoneNumberIndia = getIntent().getStringExtra("phoneNumberIndia");
        SharedPreferences sp=getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);
        token = sp.getString("token","0");
        user = new User(fullName,phoneNumberIndia,token);

        btnClickHere=findViewById(R.id.btnClickHere);

        btnClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToManualCodeEntry();
            }
        });
        layout_2 = findViewById(R.id.layout_2);

        Tovuti.from(this).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                if (!isConnected) {
                    mCodeScanner.releaseResources();
                    scannerView.setVisibility(View.INVISIBLE);
                    layout_2.setVisibility(View.VISIBLE);
                } else if (isConnected) {
                    layout_2.setVisibility(View.GONE);
                    scannerView.setVisibility(View.VISIBLE);
                    mCodeScanner.startPreview();
                }
            }
        });
    }
    public void goToManualCodeEntry(){
        Intent intent=new Intent(QRCodeActivity.this,ManualCodeEntryActivity.class);
        intent.putExtra("fullName",fullName);
        intent.putExtra("city",city);
        intent.putExtra("phoneNumberIndia",phoneNumberIndia);
        intent.putExtra("token",token);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Tovuti.from(this).start();
        mCodeScanner.startPreview();
    }
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tovuti.from(this).stop();
    }

    public void buildQRErrorDialog() {
        WrongQrCodeDialog dialog = new WrongQrCodeDialog();
        dialog.show(getSupportFragmentManager(), "Wrong QR Dialog!");

    }

    public void checkForExistingAccount(){

        collRef = db.collection(code).document("MyFamily").collection("members");
        collRef.whereEqualTo("phoneNumber",phoneNumberIndia)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().getDocuments().isEmpty()){
                                collRef.document().set(user);
                                RecentMessages recentMessages = new RecentMessages(System.currentTimeMillis(), fullName, "Family Member Added");
                                db.collection(code).document("RecentMessages").collection("Messages").document().set(recentMessages);

                                Toast.makeText(QRCodeActivity.this, "Device Registered!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String id=task.getResult().getDocuments().get(0).getId();
                                collRef.document(id).delete();
                                collRef.document().set(user);
                                RecentMessages recentMessages = new RecentMessages(System.currentTimeMillis(), fullName, "Family Member Added");
                                db.collection(code).document("RecentMessages").collection("Messages").document().set(recentMessages);
                                Toast.makeText(QRCodeActivity.this, "Device Registered!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(QRCodeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startScanner(){

        mCodeScanner=new CodeScanner(getApplicationContext(),scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        code = result.getText().trim();
                        if(code.contains("AJNA")){
                            SharedPreferences sp = getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();

                            edit.putString("code",code);
                            edit.putString("fullName",fullName);
                            edit.putString("phoneNumber",phoneNumberIndia);
                            edit.putString("isSignedIn","1");
                            edit.apply();

                            checkForExistingAccount();
                            DatabaseReference db2 = FirebaseDatabase.getInstance().getReference();
                            db2.child(code).child("tokens").child("and" + fullName).setValue(token);

                            Intent intent = new Intent(QRCodeActivity.this, LoginOptions.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            buildQRErrorDialog();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void exitAndStartScanner() {
        mCodeScanner.startPreview();
    }
}
