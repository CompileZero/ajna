package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ramotion.foldingcell.FoldingCell;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.w3c.dom.Text;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyImmediateContactsFragmentExpanded extends Fragment {

    private View view;

    private SharedPreferences sp;

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myImmediateContactsRef;

    private MyImmediateContactsAdapterExpanded adapter;

    private FoldingCell foldingCell;

    private DiscreteScrollView picker;

    private Button btnBackRvMyImmediateContacts;

    private TextView tvDisplayName,tvDisplayContactNumber;

    private FloatingActionButton btnAddImmediateContact;

    public MyImmediateContactsFragmentExpanded() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_immediate_contacts_fragment_expanded, container, false);

        tvDisplayName=view.findViewById(R.id.tvDisplayName);
        tvDisplayContactNumber=view.findViewById(R.id.tvDisplayContactNumber);

        btnAddImmediateContact=view.findViewById(R.id.btnAddImmediateContact);

        //Initialise Discrete Scroll view
        picker=view.findViewById(R.id.picker);
        picker.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.65f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.CENTER)
                .build());

        //Get QR Code data and retrieve the device data from Firebase
        sp=this.getActivity().getSharedPreferences("DEVICE_CODE",Context.MODE_PRIVATE);
        String code=sp.getString("code","0");
        myImmediateContactsRef= db.collection(code).document("My Family").collection("members");
        Query query=myImmediateContactsRef.orderBy("nameOfImmediateContact");

        FirestoreRecyclerOptions<MyImmediateContacts> options=new FirestoreRecyclerOptions.Builder<MyImmediateContacts>()
                .setQuery(query,MyImmediateContacts.class)
                .build();

        //Set contents of Firebase on Discrete Scroll View
        adapter = new MyImmediateContactsAdapterExpanded(options);
        picker.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
            @Override
            public void onScrollStart(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void onScrollEnd(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                TextView tvName=viewHolder.itemView.findViewById(R.id.tvNameOfImmediateContact);
                TextView tvContactNumber=viewHolder.itemView.findViewById(R.id.tvContactNumberOfImmediateContact);

                String name=tvName.getText().toString().trim();
                String contactNumber=tvContactNumber.getText().toString().trim();

                tvDisplayName.setText(name);
                tvDisplayContactNumber.setText(contactNumber);

            }

            @Override
            public void onScroll(float v, int i, int i1, @Nullable RecyclerView.ViewHolder viewHolder, @Nullable RecyclerView.ViewHolder t1) {

            }
        });
        picker.setAdapter(adapter);

        btnAddImmediateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddImmediateContactFragment dialogFrag = AddImmediateContactFragment.newInstance();
                dialogFrag.setParentFab(btnAddImmediateContact);
                dialogFrag.show(getFragmentManager(), dialogFrag.getTag());
            }
        });


        //mandate return view
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
        Log.d("FRAGMENT EXPANDED","on Start");

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.d("FRAGMENT EXPANDED","on Stop");

    }
    private void setUpRecyclerView() {

    }

}
