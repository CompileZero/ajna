package in.ajna.ajnamobile.ajna.MyFamily;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.Login.User;
import in.ajna.ajnamobile.ajna.R;

public class FamilyFragment extends Fragment {

    private RecyclerView recyclerView;
    private FamilyAdapter adapter;
    private SharedPreferences sp;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myFamilyRef;

    View view;

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_family, container, false);

        recyclerView = view.findViewById(R.id.rvFamily);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView(){
        sp=this.getActivity().getSharedPreferences("DEVICE_CODE", Context.MODE_PRIVATE);
        String code=sp.getString("code","0");
        myFamilyRef= db.collection(code).document("MyFamily").collection("members");

        Query query=myFamilyRef.orderBy("fullName",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<User> options=new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();

        adapter = new FamilyAdapter(options);
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
