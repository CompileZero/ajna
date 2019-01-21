package in.ajna.ajnamobile.ajna.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marozzi.roundbutton.RoundButton;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextRegisterFullName,editTextRegisterAddress,
            editTextRegisterCity;

    Button btnRegister;

    FirebaseAuth mAuth;

    FirebaseFirestore db;

    String phoneNumberIndia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextRegisterFullName=findViewById(R.id.editTextRegisterFullName);
        editTextRegisterAddress=findViewById(R.id.editTextRegisterAddress);
        editTextRegisterCity=findViewById(R.id.editTextRegisterCity);

        btnRegister=findViewById(R.id.btnRegister);

        mAuth=FirebaseAuth.getInstance();

        db=FirebaseFirestore.getInstance();

        phoneNumberIndia=getIntent().getStringExtra("phoneNumberIndia");

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{android.Manifest.permission.CAMERA}, 50);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });


    }

    private void registerUser(){
        String fullName=editTextRegisterFullName.getText().toString().trim();
        String address=editTextRegisterAddress.getText().toString().trim();
        String city=editTextRegisterCity.getText().toString().trim();


        if(fullName.isEmpty()){
            editTextRegisterFullName.setError("Please enter a valid name");
            return;
        }
        if(address.isEmpty()){
            editTextRegisterAddress.setError("Please enter a valid address");
            return;
        }
        if(city.isEmpty()){
            editTextRegisterCity.setError("Please enter a valid city name");
            return;
        }

        //Add details to "users" database
        User user = new User(fullName,address,city,phoneNumberIndia);
        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user);


        Intent intent=new Intent(RegisterActivity.this,QRCodeActivity.class);
        intent.putExtra("fullName",fullName);
        intent.putExtra("address",address);
        intent.putExtra("city",city);
        intent.putExtra("phoneNumberIndia",phoneNumberIndia);
        startActivity(intent);
    }


}
