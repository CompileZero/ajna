package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;
import in.ajna.ajnamobile.ajna.RecentMessages.RecentMessagesFragmentCollapsed;
import in.ajna.ajnamobile.ajna.RecentMessages.RecentMessagesFragmentExpanded;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyImmediateContactsFragmentCollapsed extends Fragment {

    //Objects
    private View view;

    private MainActivity mainActivity;

    private RecyclerView recyclerView;
    private MyImmediateContactsAdapter adapter;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //Get QR Code data and retrieve the device data from Firebase
        sp=this.getActivity().getSharedPreferences("DEVICE_CODE",Context.MODE_PRIVATE);
        String code=sp.getString("code","0");
         myImmediateContactsRef= db.collection(code).document("MyImmediateContacts").collection("members");
        Query query=myImmediateContactsRef.orderBy("nameOfImmediateContact",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MyImmediateContacts> options=new FirestoreRecyclerOptions.Builder<MyImmediateContacts>()
                .setQuery(query,MyImmediateContacts.class)
                .build();

        adapter = new MyImmediateContactsAdapter(options);

        recyclerView.setAdapter(adapter);

        initRecentMessages();

        //Mandate return
        return view;
    }
    private void initRecentMessages(){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction2=fragmentManager.beginTransaction();
        fragmentTransaction2.add(R.id.fragmentContainerRecentMessages,new RecentMessagesFragmentExpanded(),"RecentMessages")
                .commit();

        FragmentTransaction fragmentTransaction3=fragmentManager.beginTransaction();
        fragmentTransaction3.add(R.id.fragmentContainerRecentMessages2,new RecentMessagesFragmentCollapsed(),"RecentMessagesCollapsed")
                .commit();
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

}
