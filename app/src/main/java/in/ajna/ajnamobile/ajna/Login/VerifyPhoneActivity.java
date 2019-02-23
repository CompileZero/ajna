package in.ajna.ajnamobile.ajna.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import in.ajna.ajnamobile.ajna.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {


    private OtpView otpView;
    private TextView tvTimer;
    private Button btnVerify;
    private ProgressBar progressCircular;

    private String phoneNumberIndia, verificationId, code,fullName;

    private FirebaseAuth mAuth;
    private PhoneAuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        otpView=findViewById(R.id.otpView);
        tvTimer=findViewById(R.id.tvTimer);
        btnVerify=findViewById(R.id.btnVerify);
        progressCircular=findViewById(R.id.progressCircular);

        mAuth=FirebaseAuth.getInstance();

        phoneNumberIndia=getIntent().getStringExtra("phoneNumberIndia");
        fullName=getIntent().getStringExtra("fullName");

        sendVerificationCode(phoneNumberIndia);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressCircular.setVisibility(View.VISIBLE);
                code=otpView.getText().toString().trim();

                verifyCode(code);
            }
        });
    }
    private void verifyCode(String code){
        try{
            credential = PhoneAuthProvider.getCredential(verificationId,code);
            signInWithCredential(credential);
        }
        catch (Exception e){
            Toast.makeText(this, "Error: Please enter a valid OTP and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(VerifyPhoneActivity.this, "Verification Succesful!", Toast.LENGTH_SHORT).show();

                            //Go to QR permission page
                            Intent intent = new Intent(VerifyPhoneActivity.this,QRCodePermissionsActivity.class);
                            intent.putExtra("phoneNumberIndia",phoneNumberIndia);
                            intent.putExtra("fullName",fullName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });


    }

    private void sendVerificationCode(String number){

        //start the minute timer
        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText((millisUntilFinished/1000)+"s");
            }

            @Override
            public void onFinish() {
                btnVerify.setEnabled(true);
            }
        }.start();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                progressCircular.setVisibility(View.VISIBLE);
                otpView.setText(code);
                verifyCode(code);
            }
            else{
                otpView.findFocus();
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, "Error: Please enter a valid number and try again", Toast.LENGTH_SHORT).show();


        }
    };
}
