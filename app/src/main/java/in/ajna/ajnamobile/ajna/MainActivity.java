package in.ajna.ajnamobile.ajna;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsActivity2;

import android.os.Bundle;

import android.view.Menu;

import android.view.View;

import com.rey.material.widget.Switch;


public class MainActivity extends AppCompatActivity {
    CardView cvMyImmediateContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cvMyImmediateContacts=(CardView) findViewById(R.id.cvMyImmediateContacts);
        cvMyImmediateContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyImmediateContactsActivity2.class);
                startActivity(intent);

            }
        });






        //To set a light status bar with grey icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
