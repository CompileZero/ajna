package in.ajna.ajnamobile.ajna;


import android.app.Notification;

import android.graphics.Color;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import in.ajna.ajnamobile.ajna.MyFamily.MyFamilyFragmentCollapsed;
import in.ajna.ajnamobile.ajna.MyFamily.MyFamilyFragmentExpanded;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsFragmentCollapsed;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsFragmentExpanded;
import in.ajna.ajnamobile.ajna.Notification.App;
import in.ajna.ajnamobile.ajna.RecentMessages.RecentMessagesFragmentCollapsed;
import in.ajna.ajnamobile.ajna.RecentMessages.RecentMessagesFragmentExpanded;


import android.os.Bundle;

import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.foldingcell.FoldingCell;


public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private static final String NAME_KEY = "Name";

    private static final String PHONE_KEY = "Phone";

    private CardView cvMyImmediateContacts;
    private CardView cvMyImmediateContactsCollapsed;
    private CardView cvDeviceStatus;
    private ImageView btnExpandMyImmediateContacts;

    private FragmentManager fragmentManager=getSupportFragmentManager();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    FloatingActionButton fabExpand1,fabExpand2;
    FoldingCell fc,fc2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Display Main Activity
        setContentView(R.layout.activity_main);

        //Set Custom toolbar
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //To set a light status bar with grey icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        notificationManager=NotificationManagerCompat.from(this);

        //Initialise FAB
        fabExpand1=findViewById(R.id.fabExpand1);
        fabExpand1.setColorFilter(Color.WHITE);
        fabExpand2=findViewById(R.id.fabExpand2);
        fabExpand2.setColorFilter(Color.WHITE);


        //start My Immediate Contacts Fragments
        initMyImmediateContacts();



        //Folding Cell
        fc=findViewById(R.id.folding_cell);
        fc2=findViewById(R.id.folding_cell2);

        final FoldingCell fc3=findViewById(R.id.folding_cell3);
        fc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc3.toggle(false);
            }
        });
        /*final FoldingCell fc4=findViewById(R.id.folding_cell4);
        fc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc4.toggle(false);
            }
        });*/


    }

    private void initMyImmediateContacts() {
        FragmentTransaction fragmentTransaction1=fragmentManager.beginTransaction();
        fragmentTransaction1.add(R.id.fragmentContainerMyImmediateContacts, new MyImmediateContactsFragmentExpanded(),"ImmContactsExpanded")
                .commit();

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.fragmentContainerMyImmediateContacts3,new MyImmediateContactsFragmentCollapsed(),"ImmContactsCollapsed")
                .commit();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.btnHelp){
            mAuth.signOut();
            Toast.makeText(this, "SignedOut", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.btnSettings){

            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void expandImmediateContacts(View view){
        fc.toggle(false);
        sendOnChannel1();

    }
    public void expandRecentMessages(View view){
        fc2.toggle(false);
    }
    public void sendOnChannel1(){
        Notification notification = new NotificationCompat.Builder(this,App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_icon1)
                .setContentTitle("Hello this is a text!")
                .setContentText("This is the description.")
                .build();
        notificationManager.notify(1,notification);
    }

}
