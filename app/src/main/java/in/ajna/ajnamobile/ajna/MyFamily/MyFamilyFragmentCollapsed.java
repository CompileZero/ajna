package in.ajna.ajnamobile.ajna.MyFamily;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import in.ajna.ajnamobile.ajna.R;

public class MyFamilyFragmentCollapsed extends Fragment {
    View view;

    public MyFamilyFragmentCollapsed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_family_fragment_collapsed, container, false);


        return view;
    }

}
