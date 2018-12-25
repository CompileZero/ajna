package in.ajna.ajnamobile.ajna.Login;

import androidx.appcompat.app.AppCompatActivity;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.infideap.blockedittext.BlockEditText;

public class ManualCodeEntryActivity extends AppCompatActivity {

    String fullName,address,city,phoneNumberIndia;

    BlockEditText etBlocks;
    MaterialButton btnProceed;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_code_entry);

        etBlocks=findViewById(R.id.etBlocks);
        btnProceed=findViewById(R.id.btnProceed);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        fullName = getIntent().getStringExtra("fullName");
        address = getIntent().getStringExtra("address");
        city = getIntent().getStringExtra("city");
        phoneNumberIndia = getIntent().getStringExtra("phoneNumberIndia");
        user = new User(fullName, address, city, phoneNumberIndia);

    }

    public void goToHomePage(View view){

        String extractedText=etBlocks.getText().toString().trim();
        if(extractedText.length()!=6) {
            Toast.makeText(this, "Please enter a valid code!", Toast.LENGTH_SHORT).show();

        }

        else {

            String code = "AJNA-" + etBlocks.getText().toString().trim();

            SharedPreferences sp = getSharedPreferences("DEVICE_CODE", MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("code", code);
            edit.apply();

            db.collection(code).document("MyFamily").collection("members").document().set(user);
            Toast.makeText(ManualCodeEntryActivity.this, "Device Registered!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManualCodeEntryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
