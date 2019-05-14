package in.ajna.ajnamobile.ajna.Login;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;

import in.ajna.ajnamobile.ajna.R;

public class LoginActivity extends AppCompatActivity {


    private EditText editTextPhoneNumber,editTextRegisterFullName;
    private Button btnLogin;
    private RelativeLayout layout_1, layout_2;

    private String phoneNumberIndia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher("IN"));
        editTextRegisterFullName = findViewById(R.id.editTextRegisterFullName);
        btnLogin = findViewById(R.id.btnLogin);

        layout_1 = findViewById(R.id.layout_1);
        layout_2 = findViewById(R.id.layout_2);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhoneNumber.getText().toString().trim().replaceAll("\\D", "");
                String fullName = editTextRegisterFullName.getText().toString().trim();

                if (phoneNumber.length() == 10 && !phoneNumber.contains("+") && !fullName.isEmpty()) {
                    phoneNumberIndia = "+91" + phoneNumber;
                    Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                    intent.putExtra("phoneNumberIndia", phoneNumberIndia);
                    intent.putExtra("fullName", fullName);
                    startActivity(intent);
                } else {
                    if (fullName.isEmpty()) {
                        editTextRegisterFullName.setError("Please enter a valid name");
                    }
                    if (phoneNumber.length() != 10 || phoneNumber.contains("+")) {
                        editTextPhoneNumber.setError("Please enter a valid phone Number");
                    }
                    return;
                }
            }
        });
        Tovuti.from(this).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                if (!isConnected) {
                    layout_1.setVisibility(View.GONE);
                    layout_2.setVisibility(View.VISIBLE);
                } else if (isConnected) {
                    layout_2.setVisibility(View.GONE);
                    layout_1.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tovuti.from(this).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tovuti.from(this).stop();
    }
}
