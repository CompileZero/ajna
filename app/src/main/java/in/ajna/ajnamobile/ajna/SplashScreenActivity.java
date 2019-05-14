package in.ajna.ajnamobile.ajna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import in.ajna.ajnamobile.ajna.Login.AgreeContinueActivity;

public class SplashScreenActivity extends AppCompatActivity {


    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);

        final String flagSignedIn = sp.getString("isSignedIn","0");

        if(flagSignedIn.equals("1")){
            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);

            startActivity(intent);
            finish();
        }
        else if(flagSignedIn.equals("0")){
            Intent intent = new Intent(SplashScreenActivity.this,AgreeContinueActivity.class);

            startActivity(intent);
            finish();
        }

    }
}
