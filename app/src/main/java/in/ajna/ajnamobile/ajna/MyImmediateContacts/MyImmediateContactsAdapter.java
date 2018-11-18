package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

public class MyImmediateContactsAdapter extends FirestoreRecyclerAdapter<MyImmediateContacts,MyImmediateContactsAdapter.MyImmediateContactsHolder> {


    public MyImmediateContactsAdapter(@NonNull FirestoreRecyclerOptions<MyImmediateContacts> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyImmediateContactsHolder myImmediateContactsHolder, int i, @NonNull MyImmediateContacts myImmediateContacts) {
        myImmediateContactsHolder.tvNameOfImmediateContact.setText(myImmediateContacts.getNameOfImmediateContact());
        myImmediateContactsHolder.tvContactNumberOfImmediateContact.setText(myImmediateContacts.getContactNumberOfImmediateContact());
    }

    @NonNull
    @Override
    public MyImmediateContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_immediate_contacts_fragment_expanded,parent,false);

        return new MyImmediateContactsHolder(v);
    }

    class MyImmediateContactsHolder extends RecyclerView.ViewHolder{

        TextView tvNameOfImmediateContact;
        TextView tvContactNumberOfImmediateContact;


        public MyImmediateContactsHolder(@NonNull View itemView) {
            super(itemView);
            tvNameOfImmediateContact=itemView.findViewById(R.id.tvNameOfImmediateContact);
            tvContactNumberOfImmediateContact=itemView.findViewById(R.id.tvContactNumberOfImmediateContact);

        }
    }
}
