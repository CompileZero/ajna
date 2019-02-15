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

    private EditText editTextPhoneNumber,editTextRegisterFullName;
    private Button btnLogin;

    private String phoneNumberIndia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextRegisterFullName=findViewById(R.id.editTextRegisterFullName);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=editTextPhoneNumber.getText().toString().trim();
                String fullName=editTextRegisterFullName.getText().toString().trim();

                if(phoneNumber.length() == 10 && !phoneNumber.contains("+") && !fullName.isEmpty()){
                    phoneNumberIndia="+91"+phoneNumber;
                    Intent intent=new Intent(LoginActivity.this,VerifyPhoneActivity.class);
                    intent.putExtra("phoneNumberIndia",phoneNumberIndia);
                    intent.putExtra("fullName",fullName);
                    startActivity(intent);
                }
                else{
                    if(fullName.isEmpty()){
                        editTextRegisterFullName.setError("Please enter a valid name");
                    }
                    if(phoneNumber.length()!=10 || phoneNumber.contains("+")){
                        editTextPhoneNumber.setError("Please enter a valid phone Number");
                    }
                    return;
                }
            }
        });
    }
}
