package in.ajna.ajnamobile.ajna.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

public class LoginOptions extends AppCompatActivity {

    private Button btnSetupDevice,btnHomepage;
    RelativeLayout layout_info;
    private FirebaseFirestore db;
    private ProgressBar progressCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);

        btnHomepage=findViewById(R.id.btnHomepage);
        btnSetupDevice=findViewById(R.id.btnSetupDevice);
        layout_info = findViewById(R.id.layout_info);
        progressCircular = findViewById(R.id.progressCircular);

        db = FirebaseFirestore.getInstance();
        SharedPreferences sp = getSharedPreferences("DEVICE_CODE", MODE_PRIVATE);
        String code = sp.getString("code", null);
        db.collection(code).document("MyFamily").collection("members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int size = task.getResult().size();
                if (size <= 1) {
                    btnSetupDevice.setEnabled(true);
                    progressCircular.setVisibility(View.GONE);
                } else if (size > 1) {
                    btnHomepage.setEnabled(true);
                    layout_info.setVisibility(View.VISIBLE);
                    progressCircular.setVisibility(View.GONE);
                }
            }
        });

        FloatingActionButton fabHelp = findViewById(R.id.fabHelp);
        fabHelp.setColorFilter(Color.BLUE);
        fabHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserGuideActivity.class);
                startActivity(intent);
            }
        });

        btnHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginOptions.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnSetupDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginOptions.this, DeviceInitActivity.class);
                startActivity(intent);
            }
        });


    }

}
