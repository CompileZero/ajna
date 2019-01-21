package in.ajna.ajnamobile.ajna.MyFamily;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import javax.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.ajna.ajnamobile.ajna.Login.User;
import in.ajna.ajnamobile.ajna.R;

public class MyFamilyFragmentCollapsed extends Fragment {
    View view;

    private FloatingActionButton fabExpand2;

    private RecyclerView recyclerView;
    private MyFamilyAdapter adapter;

    private SharedPreferences sp;

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myFamilyRef;
    public MyFamilyFragmentCollapsed() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_family_fragment_collapsed, container, false);

        recyclerView = view.findViewById(R.id.rvMyFamily);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        fabExpand2=view.findViewById(R.id.fabExpand2);
        fabExpand2.setColorFilter(Color.WHITE);

        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView(){
        sp=this.getActivity().getSharedPreferences("DEVICE_CODE",Context.MODE_PRIVATE);
        String code=sp.getString("code","0");
        myFamilyRef= db.collection(code).document("MyFamily").collection("members");

        Query query=myFamilyRef.orderBy("fullName",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<User> options=new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();

        adapter = new MyFamilyAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
