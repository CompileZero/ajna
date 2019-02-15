package in.ajna.ajnamobile.ajna.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.infideap.blockedittext.BlockEditText;

public class ManualCodeEntryActivity extends AppCompatActivity {

    String fullName,city,phoneNumberIndia,token;

    BlockEditText etBlocks;
    Button btnProceed;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    CollectionReference collRef;

    User user;

    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_code_entry);

        etBlocks=findViewById(R.id.etBlocks);
        btnProceed=findViewById(R.id.btnProceed);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        fullName = getIntent().getStringExtra("fullName");
        city = getIntent().getStringExtra("city");
        phoneNumberIndia = getIntent().getStringExtra("phoneNumberIndia");
        token=getIntent().getStringExtra("token");
        user = new User(fullName,phoneNumberIndia,token);

    }

    public void goToHomePage(View view){

        String extractedText=etBlocks.getText().toString().trim();
        if(extractedText.length()!=6) {
            Toast.makeText(this, "Please enter a valid code!", Toast.LENGTH_SHORT).show();
        }
        else {
            code = "AJNA" + etBlocks.getText().toString().trim();

            SharedPreferences sp = getSharedPreferences("DEVICE_CODE", MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("isSignedIn","1");
            edit.putString("fullName",fullName);
            edit.putString("code", code);
            edit.apply();

            checkForExistingAccount();
            DatabaseReference db2 = FirebaseDatabase.getInstance().getReference();
            db2.child(code).child("tokens").child(fullName).setValue(token);
            Intent intent = new Intent(ManualCodeEntryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    public void checkForExistingAccount(){

        collRef=db.collection(code).document("MyFamily").collection("members");
        collRef.whereEqualTo("phoneNumber",phoneNumberIndia)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().getDocuments().isEmpty()){

                                db.collection(code).document("MyFamily").collection("members").document().set(user);
                                Toast.makeText(ManualCodeEntryActivity.this, "Device Registered!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String id=task.getResult().getDocuments().get(0).getId();
                                db.collection(code).document("MyFamily").collection("members").document(id).delete();
                                db.collection(code).document("MyFamily").collection("members").document().set(user);
                                Toast.makeText(ManualCodeEntryActivity.this, "Device Registered!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(ManualCodeEntryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }
}
