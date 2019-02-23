package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import in.ajna.ajnamobile.ajna.R;

public class AddContactDialog extends AppCompatDialogFragment {

    private TextView tvName,tvContact;
    private String contact2,name;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference contactsRef;
    private SharedPreferences sp;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_add_contact,null);


        tvName=view.findViewById(R.id.tvName);
        tvContact=view.findViewById(R.id.tvContact);

        name=getArguments().getString("name");
        tvName.setText(name);
        final String contact = getArguments().getString("contact").replace("+91","").replaceAll("\\D", "");
        contact2="+91"+contact;

        final MyImmediateContacts myImmediateContacts=new MyImmediateContacts(name,contact2);
        tvContact.setText("+91"+" "+contact.substring(0,5)+" "+contact.substring(5,10));
        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp=getActivity().getSharedPreferences("DEVICE_CODE", Context.MODE_PRIVATE);
                        String code=sp.getString("code","0");
                        contactsRef=db.collection(code).document("MyImmediateContacts").collection("members");
                        contactsRef.document().set(myImmediateContacts);
                    }
                })
        .setNeutralButton("CANCEL",null)
        .setCancelable(false);
        return builder.create();
    }

}
