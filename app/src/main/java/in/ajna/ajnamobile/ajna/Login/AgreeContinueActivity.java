package in.ajna.ajnamobile.ajna.Login;

import androidx.appcompat.app.AppCompatActivity;
import in.ajna.ajnamobile.ajna.R;
import in.ajna.ajnamobile.ajna.SplashScreenActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AgreeContinueActivity extends AppCompatActivity {

    Button btnAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_continue);

        btnAgree=findViewById(R.id.btnAgree);

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgreeContinueActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
