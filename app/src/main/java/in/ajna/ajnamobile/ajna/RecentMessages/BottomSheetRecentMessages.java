package in.ajna.ajnamobile.ajna.RecentMessages;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

public class BottomSheetRecentMessages extends BottomSheetDialogFragment {

    private BottomSheetRecentMessages.BottomSheetListener mListener;

    private SharedPreferences sp;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference messagesRef;

    private RecentMessagesAdapterExpanded adapter;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.bottom_sheet_recent_messages,container,false);

        setUpRecyclerView();

        return view;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    public interface BottomSheetListener{
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (BottomSheetRecentMessages.BottomSheetListener) context;
        }
        catch (ClassCastException e){

            throw new ClassCastException(context.toString()
                    + "must implement BottomSheetListener");

        }
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

        RecyclerView recyclerView= view.findViewById(R.id.rvRecentMessages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }
}

