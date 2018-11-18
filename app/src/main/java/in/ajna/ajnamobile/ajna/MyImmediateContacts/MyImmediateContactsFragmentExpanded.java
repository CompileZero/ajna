package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyImmediateContactsFragmentExpanded extends Fragment {

    private View view;
    MainActivity mainActivity;



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myImmediateContactsRef = db.collection("");



    Button btnBackRvMyImmediateContacts;


    public MyImmediateContactsFragmentExpanded() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_immediate_contacts_fragment_expanded, container, false);



        //Initialise Recycler-View
        RecyclerView recyclerView = view.findViewById(R.id.rvMyImmediateContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


        //mandate return view
        return view;
    }

}
