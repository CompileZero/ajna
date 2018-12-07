package in.ajna.ajnamobile.ajna;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import es.dmoral.toasty.Toasty;
import in.ajna.ajnamobile.ajna.Login.LoginActivity;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContacts;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsAdapterExpanded;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsFragmentCollapsed;
import in.ajna.ajnamobile.ajna.MyImmediateContacts.MyImmediateContactsFragmentExpanded;


import android.os.Bundle;

import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.github.florent37.expansionpanel.ExpansionLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String NAME_KEY = "Name";

    private static final String PHONE_KEY = "Phone";

    private CardView cvMyImmediateContacts;
    private CardView cvMyImmediateContactsCollapsed;
    private CardView cvDeviceStatus;
    private ImageView btnExpandMyImmediateContacts;

    private FragmentManager fragmentManager=getSupportFragmentManager();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db;



    private CollectionReference myImmediateContactsRef;


    private MyImmediateContactsAdapterExpanded adapter;


    private ExpansionLayout expansionLayout;





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



        //Initialise collapsed fragment for Device Status
        cvDeviceStatus =findViewById(R.id.cvDeviceStatus);


        //Initialise collapsed fragment for My Immediate Contacts




        FragmentTransaction fragmentTransaction1=fragmentManager.beginTransaction();
        fragmentTransaction1.add(R.id.fragmentContainerMyImmediateContacts, new MyImmediateContactsFragmentExpanded())
                .commit();

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.fragmentContainerMyImmediateContacts3,new MyImmediateContactsFragmentCollapsed(),"Collapsed")
                .commit();

        final RelativeLayout fragExp=findViewById(R.id.fragmentContainerMyImmediateContacts);
        final RelativeLayout fragColl=findViewById(R.id.fragmentContainerMyImmediateContacts3);


        //Folding Cell
        final FoldingCell fc=findViewById(R.id.folding_cell);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });

        final FoldingCell fc2=findViewById(R.id.folding_cell2);
        fc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc2.toggle(false);
            }
        });

        /*final FoldingCell fc3=findViewById(R.id.folding_cell3);
        fc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc3.toggle(false);
            }
        });

        final FoldingCell fc4=findViewById(R.id.folding_cell4);
        fc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc4.toggle(false);
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void addNewContact(String name,String contact,int id){
        MyImmediateContacts newContact = new MyImmediateContacts(name,contact,id);
        db.collection(mAuth.getCurrentUser().getUid()).document().set(newContact)
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
            mAuth.signOut();
            Toast.makeText(this, "SignedOut", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.btnSettings){

            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    public void MyFamilyAnimation()
    {

        RelativeLayout relativeLayout=findViewById(R.id.fragmentContainerExpanded);
        int height=relativeLayout.getHeight();

        ValueAnimator expand = ValueAnimator.ofInt(cvMyImmediateContacts.getMeasuredHeight(),height);

        expand.setDuration(500);
        expand.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int animated=(int) animation.getAnimatedValue();

                cvMyImmediateContacts.getLayoutParams().height = animated;
                cvMyImmediateContacts.requestLayout();

            }

        });


        ValueAnimator collapse=ValueAnimator.ofInt(cvDeviceStatus.getMeasuredHeight(),0);
        collapse.setDuration(500);
        collapse.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int animated=(int) animation.getAnimatedValue();

                cvDeviceStatus.getLayoutParams().height=animated;
                cvDeviceStatus.requestLayout();

                if (cvDeviceStatus.getLayoutParams().height<0.1) {
                    cvDeviceStatus.setVisibility(View.GONE);
                }
            }
        });

        AnimatorSet set =new AnimatorSet();
        set.playTogether(expand,collapse);
        set.start();

    }

    public void animate(View view){
        ImageView v = (ImageView) view;
        Drawable d = v.getDrawable();

        if(d instanceof AnimatedVectorDrawableCompat){
            AnimatedVectorDrawableCompat avd=(AnimatedVectorDrawableCompat) d;
            avd.start();
        }
        else if(d instanceof AnimatedVectorDrawable){
            AnimatedVectorDrawable avd=(AnimatedVectorDrawable) d;
            avd.start();
        }
    }
}
