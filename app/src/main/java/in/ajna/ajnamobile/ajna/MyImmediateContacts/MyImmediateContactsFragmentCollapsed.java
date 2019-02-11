package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private View view;


    private RecyclerView recyclerView;
    private MyImmediateContactsAdapter adapter;

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

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
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
        btnAddContact=view.findViewById(R.id.btnAddContact);

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
