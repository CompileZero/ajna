package in.ajna.ajnamobile.ajna.Login;

import androidx.appcompat.app.AppCompatActivity;

import in.ajna.ajnamobile.ajna.R;

import android.content.Intent;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity{


    private EditText editTextPhoneNumber,editTextRegisterFullName;
    private Button btnLogin;

    private String phoneNumberIndia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher("IN"));
        editTextRegisterFullName=findViewById(R.id.editTextRegisterFullName);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=editTextPhoneNumber.getText().toString().trim().replaceAll("\\D", "");
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
