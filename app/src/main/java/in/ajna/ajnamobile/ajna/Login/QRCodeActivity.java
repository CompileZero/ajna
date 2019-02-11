package in.ajna.ajnamobile.ajna.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.Collection;

import javax.annotation.Nullable;

public class QRCodeActivity extends AppCompatActivity {

    private QRCodeActivity activity;

    private MaterialButton btnClickHere;
    private CodeScanner mCodeScanner;

    private String fullName,city,phoneNumberIndia, code;


    private FirebaseFirestore db;
    private CollectionReference collRef;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qrcode);
        db = FirebaseFirestore.getInstance();

        activity=new QRCodeActivity();
        startScanner();

        //Get user values and insert to user object
        fullName = getIntent().getStringExtra("fullName");
        city = getIntent().getStringExtra("city");
        phoneNumberIndia = getIntent().getStringExtra("phoneNumberIndia");
        user = new User(fullName,city, phoneNumberIndia);

        btnClickHere=findViewById(R.id.btnClickHere);

        btnClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToManualCodeEntry();
            }
        });


    }

    public void goToManualCodeEntry(){
        Intent intent=new Intent(QRCodeActivity.this,ManualCodeEntryActivity.class);
        intent.putExtra("fullName",fullName);
        intent.putExtra("city",city);
        intent.putExtra("phoneNumberIndia",phoneNumberIndia);
        startActivity(intent);
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

    public void buildQRErrorDialog(){
        new FancyGifDialog.Builder(QRCodeActivity.this)
                .setTitle("OOPS..")
                .setMessage("The QR Code scanned is invalid! Please try again")
                .setGifResource(R.drawable.dialog1)
                .setPositiveBtnText("Ok")
                .setNegativeBtnText("Dismiss")
                .isCancellable(false)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        mCodeScanner.startPreview();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        mCodeScanner.startPreview();
                    }
                })
                .build();
    }

    public void checkForExistingAccount(){

        collRef=db.collection(code).document("MyFamily").collection("members");
        collRef.whereEqualTo("phoneNumber",phoneNumberIndia)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().getDocuments().isEmpty()){

                                db.collection(code).document("MyFamily").collection("members").document().set(user);
                                Toast.makeText(QRCodeActivity.this, "Device Registered!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String id=task.getResult().getDocuments().get(0).getId();
                                db.collection(code).document("MyFamily").collection("members").document(id).delete();
                                db.collection(code).document("MyFamily").collection("members").document().set(user);
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
        CodeScannerView scannerView=findViewById(R.id.scanner_view);
        mCodeScanner=new CodeScanner(getApplicationContext(),scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        code=result.getText().toString().trim();
                        if(code.contains("AJNA")){
                            SharedPreferences sp = getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);

                            SharedPreferences.Editor edit = sp.edit();

                            String token = sp.getString("token","0");
                            edit.putString("code",code);
                            edit.putString("fullName",fullName);
                            edit.putString("isSignedIn","1");
                            edit.apply();


                            checkForExistingAccount();

                            Intent intent = new Intent(QRCodeActivity.this, LoginOptions.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            buildQRErrorDialog();
                        }
                    }
                });

            }
        });

    }
}
