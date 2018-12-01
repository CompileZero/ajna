package in.ajna.ajnamobile.ajna.MyImmediateContacts;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyImmediateContactsFragmentCollapsed extends Fragment {

    //Objects
    private View view;

    private MainActivity mainActivity;

    private RecyclerView recyclerView;
    private MyImmediateContactsAdapter adapter;


    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myImmediateContactsRef = db.collection("AJNA_12_55_27").document("My Family").collection("members");

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
        Query query=myImmediateContactsRef.orderBy("nameOfImmediateContact");


        FirestoreRecyclerOptions<MyImmediateContacts> options=new FirestoreRecyclerOptions.Builder<MyImmediateContacts>()
                .setQuery(query,MyImmediateContacts.class)
                .build();

        adapter = new MyImmediateContactsAdapter(options);

        recyclerView.setAdapter(adapter);

        //Initialise Recycler-View
        //RecyclerView recyclerView = view.findViewById(R.id.rvMyImmediateContacts);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));



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

}
