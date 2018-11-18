package in.ajna.ajnamobile.ajna;


import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsFragmentCollapsed;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsFragmentExpanded;

import android.os.Bundle;

import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    CardView cvMyImmediateContacts;
    CardView cvDeviceStatus;

    ImageView btnExpandMyImmediateContacts;

    FragmentManager fragmentManager=getSupportFragmentManager();


    private static final String NAME_KEY = "Name";

    private static final String PHONE_KEY = "Phone";



    FirebaseFirestore db;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        //Set Custom toolbar
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        if(user!=null){
            db = FirebaseFirestore.getInstance();
            Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(this, "Please signup", Toast.LENGTH_SHORT).show();

        }


        //Help Button Pressed


        //Initialise collapsed fragment for Device Status
        cvDeviceStatus =(CardView) findViewById(R.id.cvDeviceStatus);


        //Initialise collapsed fragment for My Immediate Contacts
        cvMyImmediateContacts=(CardView) findViewById(R.id.cvMyImmediateContacts);



         FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.fragmentContainerMyImmediateContacts,new MyImmediateContactsFragmentCollapsed(),"Collapsed")
                .commit();

        //Expand My ImmediateContacts
        btnExpandMyImmediateContacts = (ImageView) findViewById(R.id.btnExpandMyImmediateContacts);
        btnExpandMyImmediateContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction1=fragmentManager.beginTransaction();
                fragmentTransaction1.replace(R.id.fragmentContainerMyImmediateContacts, new MyImmediateContactsFragmentExpanded())
                        .addToBackStack(null)
                        .commit();
                btnExpandMyImmediateContacts.setImageResource(R.drawable.ic_help);

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



    private void addNewContact(){
        Map<String, Object> newContact=new HashMap< >();
        newContact.put(NAME_KEY,"Atharva");
        newContact.put(PHONE_KEY,"9766760151");
        db.collection("MyImmediateContacts").document("Contacts").set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR:"+e.getMessage().toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void addNewFamilyMember(){
        Map<String, Object> newContact=new HashMap< >();
        newContact.put(NAME_KEY,"Sudharm");
        newContact.put(PHONE_KEY,"9766760151");
        db.collection("MyFamily").document("Contacts").set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Registered Family", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR:"+e.getMessage().toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });

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
            mFirebaseAuth.signOut();
            Toast.makeText(this, "SignedOut", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.btnSettings){

            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
