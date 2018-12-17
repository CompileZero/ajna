package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import in.ajna.ajnamobile.ajna.MainActivity;
import in.ajna.ajnamobile.ajna.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddImmediateContactFragment extends AAH_FabulousFragment {

    private MaterialButton btnAddImmediateContact;

    private FirebaseFirestore db;

    private SharedPreferences sp;

    private TextInputLayout etNameImmediateContact,etPhoneNumberImmediateContact;
    private TextInputEditText editTextNameImmediateContact,editTextPhoneNumberImmediateContact;


    public static AddImmediateContactFragment newInstance() {
        AddImmediateContactFragment f = new AddImmediateContactFragment();
        return f;
    }

    @Override

    public void setupDialog(Dialog dialog, int style) {
        final View contentView = View.inflate(getContext(), R.layout.fragment_add_immediate_contact, null);
        RelativeLayout rl_content = (RelativeLayout) contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = (LinearLayout) contentView.findViewById(R.id.ll_buttons);
        contentView.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter("closed");
            }
        });

        //params to set
        setAnimationDuration(600); //optional; default 500ms
        setPeekHeight(300); // optional; default 400dp
        //setCallbacks((AAH_FabulousFragment.Callbacks) getActivity()); //optional; to get back result
        //setAnimationListener((AnimationListener) getActivity()); //optional; to get animation callbacks
        setViewgroupStatic(ll_buttons); // optional; layout to stick at bottom on slide
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last

        btnAddImmediateContact=contentView.findViewById(R.id.btnAddImmediateContact);

        etNameImmediateContact=contentView.findViewById(R.id.etNameImmediateContact);
        etPhoneNumberImmediateContact=contentView.findViewById(R.id.etPhoneNumberImmediateContact);
        editTextNameImmediateContact=contentView.findViewById(R.id.editTextNameImmediateContact);
        editTextPhoneNumberImmediateContact=contentView.findViewById(R.id.editTextPhoneNumberImmediateContact);

        btnAddImmediateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editTextNameImmediateContact.getText().toString().trim();
                String phone="+91"+(editTextPhoneNumberImmediateContact.getText().toString().trim());


                if(name.length()<4){
                    etNameImmediateContact.setError("Name has to have minimum 4 characters");
                    return;
                }
                if(phone.length()!=13){
                    etPhoneNumberImmediateContact.setError("Please enter a valid Phone Number");
                    return;
                }

                db=FirebaseFirestore.getInstance();
                sp=contentView.getContext().getSharedPreferences("DEVICE_CODE",Context.MODE_PRIVATE);
                String code=sp.getString("code","0");
                MyImmediateContacts addContact=new MyImmediateContacts(
                        name,phone);
                db.collection(code).document("MyImmediateContacts").collection("members").document().set(addContact)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(contentView.getContext(), "Contact Added!", Toast.LENGTH_SHORT).show();
                            }

                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(contentView.getContext(), "ERROR:"+e.getMessage().toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });


                closeFilter("closed");

            }
        });
    }

}
