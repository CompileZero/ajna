package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    //Empty Constructor
    public MyImmediateContactsFragmentCollapsed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_immediate_contacts, container, false);




        //Initialise Recycler-View
        //RecyclerView recyclerView = view.findViewById(R.id.rvMyImmediateContacts);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));



        //Mandate return
        return view;
    }

}
