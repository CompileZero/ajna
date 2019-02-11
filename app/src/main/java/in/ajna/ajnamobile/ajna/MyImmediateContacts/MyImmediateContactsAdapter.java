package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

public class MyImmediateContactsAdapter extends FirestoreRecyclerAdapter<MyImmediateContacts,MyImmediateContactsAdapter.MyImmediateContactsHolder> {


    public MyImmediateContactsAdapter(@NonNull FirestoreRecyclerOptions<MyImmediateContacts> options) {
        super(options); }
    @Override
    protected void onBindViewHolder(@NonNull MyImmediateContactsHolder myImmediateContactsHolder, int i, @NonNull MyImmediateContacts myImmediateContacts) {
        myImmediateContactsHolder.tvIconImm.setText(String.valueOf(myImmediateContacts.getNameOfImmediateContact().substring(0,2)));
        myImmediateContactsHolder.chipContact.setText(String.valueOf(myImmediateContacts.getNameOfImmediateContact()));

    }
    @NonNull
    @Override
    public MyImmediateContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_immediate_contacts_list_item_collapsed,parent,false);

        return new MyImmediateContactsHolder(v);
    }

    class MyImmediateContactsHolder extends RecyclerView.ViewHolder{

        TextView tvIconImm;
        Chip chipContact;

        public MyImmediateContactsHolder(@NonNull View itemView) {
            super(itemView);
            tvIconImm=itemView.findViewById(R.id.tvIconImm);
            chipContact=itemView.findViewById(R.id.chipContact);

            chipContact.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(getAdapterPosition());
                }
            });

        }
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }
}
