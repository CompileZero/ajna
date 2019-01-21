package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

public class BottomSheetMyImmediateContacts extends BottomSheetDialogFragment{

    private BottomSheetListener mListener;
    private SharedPreferences sp;
    private String code;
    private MyImmediateContactsAdapterExpanded adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myImmediateContactsRef;
    private RecyclerView recyclerView;

    MaterialButton btnExpandAddImmediateContact;
    Button btnAddImmediateContact;

    RelativeLayout layoutAddContact;

    private EditText etImmediateContactFullname,etImmediateContactPhoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.bottom_sheet_my_immediate_contacts,container,false);

        //Initialise components
        btnExpandAddImmediateContact=view.findViewById(R.id.btnExpandAddImmediateContact);
        btnAddImmediateContact=view.findViewById(R.id.btnAddImmediateContact);

        recyclerView = view.findViewById(R.id.rvMyImmediateContactsExpanded);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        layoutAddContact=view.findViewById(R.id.layoutAddContact);

        etImmediateContactFullname=view.findViewById(R.id.etImmediateContactFullname);
        etImmediateContactPhoneNumber=view.findViewById(R.id.etImmediateContactPhoneNumber);

        //functions
        setUpRecyclerView();

        btnExpandAddImmediateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutAddContact.setVisibility(View.VISIBLE);
                btnExpandAddImmediateContact.setVisibility(View.GONE);
            }
        });

        btnAddImmediateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImmediateContactToFirebase();
            }
        });

        return view;
    }



    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
    /*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getActivity(),getTheme());
    }*/

    public interface BottomSheetListener{
        void onButtonClicked(String text);
        }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        
        try{
            mListener = (BottomSheetListener) context;
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
        Log.d("BottomSheet","started");
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.d("BottomSheet","stopped");
    }

    private void setUpRecyclerView() {
        //Get QR Code data and retrieve the device data from Firebase
        sp=this.getActivity().getSharedPreferences("DEVICE_CODE",Context.MODE_PRIVATE);
        code=sp.getString("code","0");
        myImmediateContactsRef= db.collection(code).document("MyImmediateContacts").collection("members");
        Query query=myImmediateContactsRef.orderBy("nameOfImmediateContact",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MyImmediateContacts> options=new FirestoreRecyclerOptions.Builder<MyImmediateContacts>()
                .setQuery(query,MyImmediateContacts.class)
                .build();

        //Set contents of Firebase on Discrete Scroll View
        adapter = new MyImmediateContactsAdapterExpanded(options,getActivity());
        recyclerView.setAdapter(adapter);

    }

    private void addImmediateContactToFirebase() {

        String name=etImmediateContactFullname.getText().toString().trim();
        String phone="+91"+(etImmediateContactPhoneNumber.getText().toString().trim());


        if(name.length()<4){
            etImmediateContactFullname.setError("Name has to have minimum 4 characters");
            return;
        }
        if(phone.length()!=13){
            etImmediateContactPhoneNumber.setError("Please enter a valid Phone Number");
            return;
        }

        MyImmediateContacts addContact=new MyImmediateContacts(name,phone);
        db.collection(code).document("MyImmediateContacts").collection("members").document().set(addContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Contact Added!", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "ERROR:"+e.getMessage().trim(), Toast.LENGTH_SHORT).show();
                    }
                });
        etImmediateContactFullname.setText("");
        etImmediateContactPhoneNumber.setText("");
        layoutAddContact.setVisibility(View.GONE);
        btnExpandAddImmediateContact.setVisibility(View.VISIBLE);

    }
}
