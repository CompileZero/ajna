package in.ajna.ajnamobile.ajna.Activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.Locale;

import javax.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import in.ajna.ajnamobile.ajna.MyFamily.MyFamilyFragmentCollapsed;
import in.ajna.ajnamobile.ajna.R;

public class RecentMessagesFragmentCollapsed extends Fragment {
    View view;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    private SharedPreferences sp;
    private DocumentReference latestMessageRef;

    FloatingActionButton fabExpand3;



    TextView tvTime;
    TextView tvRecentMessage;
    public RecentMessagesFragmentCollapsed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recent_messages_fragment_collapsed, container, false);

        tvRecentMessage=view.findViewById(R.id.tvRecentMessage);
        tvTime=view.findViewById(R.id.tvTime);

        sp=this.getActivity().getSharedPreferences("DEVICE_CODE",Context.MODE_PRIVATE);
        String code=sp.getString("code","0");

        fabExpand3=view.findViewById(R.id.fabExpand3);
        fabExpand3.setColorFilter(Color.WHITE);

        fabExpand3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandRecentMessages();
            }
        });

        latestMessageRef=db.collection(code).document("RecentMessages");


        initMyFamily();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        latestMessageRef.addSnapshotListener(getActivity(),new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(getActivity(), "Error while loading!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(documentSnapshot.exists()){
                    String date=getDate(documentSnapshot.getLong("time"));
                    String message=documentSnapshot.getString("message");
                    tvTime.setText(date);
                    tvRecentMessage.setText(message);

                }
            }
        });

    }

    private String getDate(Long timestamp){
        Calendar cal=Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        String date= android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss a",cal).toString();

        return date;
    }

    private void initMyFamily(){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction5=fragmentManager.beginTransaction();
        fragmentTransaction5.add(R.id.fragmentContainerMyFamily2, new MyFamilyFragmentCollapsed())
                .commit();
    }

    public void expandRecentMessages(){
        BottomSheetRecentMessages bottomSheet= new BottomSheetRecentMessages();
        bottomSheet.show(getFragmentManager(),"bottomSheetRecentMessages");

    }
}