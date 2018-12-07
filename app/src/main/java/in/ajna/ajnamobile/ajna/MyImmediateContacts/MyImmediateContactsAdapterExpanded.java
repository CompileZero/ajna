package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

public class MyImmediateContactsAdapterExpanded extends FirestoreRecyclerAdapter<MyImmediateContacts,MyImmediateContactsAdapterExpanded.MyImmediateContactsHolder> {


    public MyImmediateContactsAdapterExpanded(@NonNull FirestoreRecyclerOptions<MyImmediateContacts> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyImmediateContactsHolder myImmediateContactsHolder, int i, @NonNull MyImmediateContacts myImmediateContacts) {
        myImmediateContactsHolder.tvNameOfImmediateContact.setText(myImmediateContacts.getNameOfImmediateContact());
        myImmediateContactsHolder.tvContactNumberOfImmediateContact.setText(myImmediateContacts.getContactNumberOfImmediateContact());

        myImmediateContactsHolder.tvIconExpanded.setText(String.valueOf(myImmediateContacts.getNameOfImmediateContact().substring(0,3)));
    }

    @NonNull
    @Override
    public MyImmediateContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_immediate_contacts_list_item_expanded,parent,false);

        return new MyImmediateContactsHolder(v);
    }

    class MyImmediateContactsHolder extends RecyclerView.ViewHolder{

        TextView tvNameOfImmediateContact;
        TextView tvContactNumberOfImmediateContact;
        TextView tvIconExpanded;



        public MyImmediateContactsHolder(@NonNull View itemView) {
            super(itemView);
            tvNameOfImmediateContact=itemView.findViewById(R.id.tvNameOfImmediateContact);
            tvContactNumberOfImmediateContact=itemView.findViewById(R.id.tvContactNumberOfImmediateContact);

            tvIconExpanded=itemView.findViewById(R.id.tvIconExpanded);

        }
    }
}
