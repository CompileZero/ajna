package in.ajna.ajnamobile.ajna.Login;

import androidx.appcompat.app.AppCompatActivity;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginOptions extends AppCompatActivity {

    private Button btnSetupDevice,btnHomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);

        btnHomepage=findViewById(R.id.btnHomepage);
        btnSetupDevice=findViewById(R.id.btnSetupDevice);

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
                Intent intent=new Intent(LoginOptions.this,DeviceSetupSelection.class);
                startActivity(intent);
            }
        });
    }
}
