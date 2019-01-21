package in.ajna.ajnamobile.ajna.RecentMessages;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

public class RecentMessagesFragmentExpanded extends Fragment {
    View view;

    private SharedPreferences sp;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference messagesRef;

    private RecentMessagesAdapterExpanded adapter;

    public RecentMessagesFragmentExpanded() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_recent_messages_fragment_expanded, container, false);


        setUpRecyclerView();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    private Long getCurrentTimeStamp() {
        Long timestamp=System.currentTimeMillis()/1000;
        return timestamp;
    }
    private void setUpRecyclerView() {

        //Get QR Data of the device and retrieve data from firebase

        sp=this.getActivity().getSharedPreferences("DEVICE_CODE",Context.MODE_PRIVATE);
        String code=sp.getString("code","0");

        messagesRef=db.collection(code).document("RecentMessages").collection("Messages");
        Query query=messagesRef.orderBy("time",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<RecentMessages> options=new FirestoreRecyclerOptions.Builder<RecentMessages>()
                .setQuery(query,RecentMessages.class)
                .build();
        adapter = new RecentMessagesAdapterExpanded(options);

        RecyclerView recyclerView= view.findViewById(R.id.rvRecentMessages2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }
}
