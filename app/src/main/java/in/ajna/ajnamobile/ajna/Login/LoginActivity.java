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
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;
import com.marozzi.roundbutton.RoundButton;

public class LoginActivity extends AppCompatActivity {



    EditText editTextPhoneNumber;


    Button btnLogin;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        btnLogin=findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=editTextPhoneNumber.getText().toString().trim();

                if(phoneNumber.isEmpty() || phoneNumber.length()<10 || phoneNumber.length()>10){

                    editTextPhoneNumber.setError("Please enter a valid phone Number!");
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
