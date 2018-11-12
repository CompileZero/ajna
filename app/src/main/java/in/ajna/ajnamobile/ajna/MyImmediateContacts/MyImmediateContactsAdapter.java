package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

public class MyImmediateContactsAdapter extends RecyclerView.Adapter<MyImmediateContactsAdapter.MyImmediateContactsHolder> {
    private List<MyImmediateContactsEntity> immediateContacts = new ArrayList<>();


    @NonNull
    @Override
    public MyImmediateContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_immediate_contacts_list_item,parent,false);
        return new MyImmediateContactsHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyImmediateContactsHolder holder, int position) {

        MyImmediateContactsEntity currentContact = immediateContacts.get(position);

        holder.tvId.setText(Integer.toString(currentContact.getId()));
        holder.tvName.setText(currentContact.getNameOfImmediateContact());
        holder.tvContactNumber.setText(currentContact.getContactNumberOfImmediateContact());

    }

    @Override
    public int getItemCount() {
        return immediateContacts.size();
    }


    public void setImmediateContacts(List<MyImmediateContactsEntity> immediateContacts) {
        this.immediateContacts = immediateContacts;
        notifyDataSetChanged();
    }

    class MyImmediateContactsHolder extends RecyclerView.ViewHolder{
        private TextView tvId;
        private TextView tvName;
        private TextView tvContactNumber;

        public MyImmediateContactsHolder(@NonNull View itemView) {
            super(itemView);

            tvId=itemView.findViewById(R.id.tvId);
            tvName=itemView.findViewById(R.id.tvName);
            tvContactNumber=itemView.findViewById(R.id.tvContactNumber);

        }
    }
}
