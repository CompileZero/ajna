package in.ajna.ajnamobile.ajna.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ajna.ajnamobile.ajna.R;

import android.os.Bundle;
import android.view.View;


public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbarSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentSettings, new SettingsFragment())
                .commit();

        toolbarSettings=findViewById(R.id.toolbarSettings);
        toolbarSettings.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
