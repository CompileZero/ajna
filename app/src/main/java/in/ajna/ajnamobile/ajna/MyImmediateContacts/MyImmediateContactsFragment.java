package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.ajna.ajnamobile.ajna.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyImmediateContactsFragment extends Fragment {


    public MyImmediateContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_immediate_contacts, container, false);
    }

}
