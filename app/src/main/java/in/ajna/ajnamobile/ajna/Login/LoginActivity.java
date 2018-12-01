package in.ajna.ajnamobile.ajna.Login;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import in.ajna.ajnamobile.ajna.R;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;
import com.marozzi.roundbutton.RoundButton;

public class LoginActivity extends AppCompatActivity {

    TextFieldBoxes etPhoneNumber;
    ExtendedEditText extendedEditTextPhoneNumber;


    Button btnLogin;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        extendedEditTextPhoneNumber = findViewById(R.id.extendedEditTextPhoneNumber);

        btnLogin=findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=extendedEditTextPhoneNumber.getText().toString().trim();

                if(phoneNumber.isEmpty() || phoneNumber.length()<10 || phoneNumber.length()>10){
                    etPhoneNumber.setError("Please enter a valid phone number!",true);
                    return;
                }
                String phoneNumberIndia="+91"+phoneNumber;

                Intent intent=new Intent(LoginActivity.this,VerifyPhoneActivity.class);
                intent.putExtra("phoneNumberIndia",phoneNumberIndia);
                startActivity(intent);
            }
        });







    }
}
