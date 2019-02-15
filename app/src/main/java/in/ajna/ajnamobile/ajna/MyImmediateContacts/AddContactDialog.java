package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import in.ajna.ajnamobile.ajna.R;

public class AddContactDialog extends AppCompatDialogFragment {

    private TextView tvName,tvContact;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_add_contact,null);

        tvName=view.findViewById(R.id.tvName);
        tvContact=view.findViewById(R.id.tvContact);

        builder.setView(view)
                .setTitle("Login")
                .setNegativeButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public interface AddContactDialogListener{
        void getText(String name,String contact);
    }
}
