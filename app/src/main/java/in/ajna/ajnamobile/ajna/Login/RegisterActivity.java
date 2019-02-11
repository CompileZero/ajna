package in.ajna.ajnamobile.ajna.Login;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.marozzi.roundbutton.RoundButton;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextRegisterFullName, editTextRegisterCity;

    Button btnRegister;

    FirebaseAuth mAuth;

    FirebaseFirestore db;

    CollectionReference collRef;

    User user;
    String phoneNumberIndia;

    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextRegisterFullName=findViewById(R.id.editTextRegisterFullName);

        editTextRegisterCity=findViewById(R.id.editTextRegisterCity);

        btnRegister=findViewById(R.id.btnRegister);

        mAuth=FirebaseAuth.getInstance();

        db=FirebaseFirestore.getInstance();

        phoneNumberIndia=getIntent().getStringExtra("phoneNumberIndia");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });
    }
    private void registerUser(){
        String fullName=editTextRegisterFullName.getText().toString().trim();
        String city=editTextRegisterCity.getText().toString().trim();


        if(fullName.isEmpty()){
            editTextRegisterFullName.setError("Please enter a valid name");
            return;
        }
        if(city.isEmpty()){
            editTextRegisterCity.setError("Please enter a valid city name");
            return;
        }

        //Add details to "users" database
        User user = new User(fullName,city,phoneNumberIndia);
        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user);

        Intent intent=new Intent(RegisterActivity.this,QRCodePermissionsActivity.class);
        intent.putExtra("fullName",fullName);
        intent.putExtra("city",city);
        intent.putExtra("phoneNumberIndia",phoneNumberIndia);
        startActivity(intent);
    }

}
