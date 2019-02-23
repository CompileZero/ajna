package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyImmediateContactsFragmentCollapsed extends Fragment{

    private int CONTACT_PERMISSION_CODE=1;

    private View view,emptyView;

    private TextView tvNothing;
    private RecyclerView recyclerView;
    private MyImmediateContactsAdapter adapter;


    String TempNameHolder, TempNumberHolder;
    private ChipsLayoutManager chipsLayoutManager;

    MaterialButton btnAddContact;

    private SharedPreferences sp;

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myImmediateContactsRef;

    //Empty Constructor
    public MyImmediateContactsFragmentCollapsed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_immediate_contacts, container, false);
        recyclerView = view.findViewById(R.id.rvMyImmediateContacts);

        chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.TOP)
                //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(true)
                //set gravity resolver where you can determine gravity for item in position.
                //This method have priority over previous one
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.CENTER;
                    }
                })
                //you are able to break row due to your conditions. Row breaker should return true for that views
                .setRowBreaker(new IRowBreaker() {
                    @Override
                    public boolean isItemBreakRow(@IntRange(from = 0) int position) {
                        return position == 6 || position == 11 || position == 2;
                    }
                })
                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build();

        recyclerView.setLayoutManager(chipsLayoutManager);
        emptyView = getLayoutInflater().inflate(R.layout.view_empty,recyclerView,false);
        btnAddContact=view.findViewById(R.id.btnAddContact);

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                    requestContactPermission();
                }
                else {
                    Intent it = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(it, 7);
                }
            }
        });


        //retrieve the device data from Firebase
        setUpRecyclerView();


        //Mandate return
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        Log.d("FRAGMENT COLLAPSED","on Start");

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.d("FRAGMENT COLLAPSED","on Stop");
        //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

    }


    private void setUpRecyclerView(){
        sp=this.getActivity().getSharedPreferences("DEVICE_CODE",Context.MODE_PRIVATE);
        String code=sp.getString("code","0");
        myImmediateContactsRef= db.collection(code).document("MyImmediateContacts").collection("members");
        Query query=myImmediateContactsRef.orderBy("nameOfImmediateContact",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MyImmediateContacts> options=new FirestoreRecyclerOptions.Builder<MyImmediateContacts>()
                .setQuery(query,MyImmediateContacts.class)
                .build();

        adapter = new MyImmediateContactsAdapter(options);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (7):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = ResultIntent.getData();

                    cursor1 = getActivity().getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            if(cursor2.moveToNext()){
                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                startDialog();

                            }

                            /*while (cursor2.moveToNext()) {



                                Toast.makeText(getContext(), "Name: "+TempNameHolder+" Contact: "+TempNumberHolder, Toast.LENGTH_SHORT).show();


                            }*/
                        }

                    }
                }
                break;
        }
    }

    private void requestContactPermission(){
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CONTACT_PERMISSION_CODE) {

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent it = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(it, 7);
            }


        }
    }

    private void startDialog(){
        AddContactDialog addContactDialog=new AddContactDialog();
        Bundle args = new Bundle();
        args.putString("name",TempNameHolder);
        args.putString("contact",TempNumberHolder);
        addContactDialog.setArguments(args);
        addContactDialog.show(getFragmentManager(),"Add Contact Dialog");
    }
}
