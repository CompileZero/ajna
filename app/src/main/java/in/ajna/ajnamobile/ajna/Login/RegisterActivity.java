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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marozzi.roundbutton.RoundButton;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextFieldBoxes etRegisterFullName,etRegisterAddress,
            etRegisterCity;

    ExtendedEditText extendedEditTextRegisterFullName,extendedEditTextRegisterAddress,
            extendedEditTextRegisterCity;

    Button btnRegister;

    FirebaseAuth mAuth;

    FirebaseFirestore db;


    String phoneNumberIndia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterFullName=findViewById(R.id.etRegisterFullName);
        etRegisterAddress=findViewById(R.id.etRegisterAddress);
        etRegisterCity=findViewById(R.id.etRegisterCity);


        extendedEditTextRegisterFullName=findViewById(R.id.extendedEditTextRegisterFullName);
        extendedEditTextRegisterAddress=findViewById(R.id.extendedEditTextRegisterAddress);
        extendedEditTextRegisterCity=findViewById(R.id.extendedEditTextRegisterCity);


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
        String fullName=extendedEditTextRegisterFullName.getText().toString().trim();
        String address=extendedEditTextRegisterAddress.getText().toString().trim();
        String city=extendedEditTextRegisterCity.getText().toString().trim();


        if(fullName.isEmpty()){
            etRegisterFullName.setError("Please enter a valid name",true);
            return;
        }
        if(address.isEmpty()){
            etRegisterAddress.setError("Please enter a valid address",true);
            return;
        }
        if(city.isEmpty()){
            etRegisterCity.setError("Please enter a valid city name",true);
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
